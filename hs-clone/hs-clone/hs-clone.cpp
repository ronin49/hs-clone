#include <windows.h>
#include <gdiplus.h>
#include <string>

#pragma comment(lib, "gdiplus.lib")

using namespace Gdiplus;

LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    static Image* image = nullptr;

    switch (uMsg) {
    case WM_CREATE:
        // Load the image
        image = new Image(L"Images\\field.png");
        if (image->GetLastStatus() != Ok) {
            MessageBox(hwnd, L"Не удалось загрузить изображение!", L"Ошибка", MB_OK);
            delete image;
            image = nullptr;
        }
        break;
    case WM_PAINT:
    {
        PAINTSTRUCT ps;
        HDC hdc = BeginPaint(hwnd, &ps);
        if (image) {
            Graphics graphics(hdc);
            // Get client area dimensions
            RECT clientRect;
            GetClientRect(hwnd, &clientRect);
            int width = clientRect.right - clientRect.left;
            int height = clientRect.bottom - clientRect.top;

            // Draw the image scaled to the window size
            graphics.DrawImage(image, 0, 0, width, height);
        }
        EndPaint(hwnd, &ps);
    }
    break;
    case WM_SIZE:
        // Handle window resizing
        InvalidateRect(hwnd, NULL, TRUE); // Request a repaint
        break;
    case WM_DESTROY:
        delete image; // Free memory
        PostQuitMessage(0);
        break;
    default:
        return DefWindowProc(hwnd, uMsg, wParam, lParam);
    }
    return 0;
}

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE, LPSTR, int nShowCmd) {
    GdiplusStartupInput gdiplusStartupInput;
    ULONG_PTR gdiplusToken;
    GdiplusStartup(&gdiplusToken, &gdiplusStartupInput, NULL);

    const wchar_t CLASS_NAME[] = L"Sample Window Class";

    WNDCLASS wc = {};
    wc.lpfnWndProc = WindowProc;
    wc.hInstance = hInstance;
    wc.lpszClassName = CLASS_NAME;

    RegisterClass(&wc);

    HWND hwnd = CreateWindowExW(
        0, CLASS_NAME, L"Image Display", WS_OVERLAPPEDWINDOW,
        CW_USEDEFAULT, CW_USEDEFAULT, 800, 600,
        NULL, NULL, hInstance, NULL
    );

    ShowWindow(hwnd, nShowCmd);

    // Main message loop
    MSG msg;
    while (GetMessage(&msg, NULL, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }

    GdiplusShutdown(gdiplusToken);
    return 0;
}
