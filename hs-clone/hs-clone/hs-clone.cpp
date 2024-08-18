#include <windows.h>
#include <gdiplus.h>
#include <string>

#pragma comment(lib, "gdiplus.lib")

using namespace Gdiplus;

// Глобальные переменные
Image* image = nullptr; // Изображение поля
Image* cardImage = nullptr; // Изображение карты
POINT cursorPos; // Позиция курсора

LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    switch (uMsg) {
    case WM_CREATE:
        // Загружаем изображение поля
        image = new Image(L"Images\\field.png");
        if (image->GetLastStatus() != Ok) {
            delete image;
            image = nullptr;
        }
        // Загружаем изображение карты
        cardImage = new Image(L"Images\\card.png");
        if (cardImage->GetLastStatus() != Ok) {
            delete cardImage;
            cardImage = nullptr;
        }
        break;
    case WM_PAINT:
    {
        PAINTSTRUCT ps;
        HDC hdc = BeginPaint(hwnd, &ps);
        Graphics graphics(hdc);

        // Получаем размеры клиентской области
        RECT clientRect;
        GetClientRect(hwnd, &clientRect);
        int width = clientRect.right - clientRect.left;
        int height = clientRect.bottom - clientRect.top;

        // Рисуем изображение поля, масштабируя его под размеры окна
        if (image) {
            graphics.DrawImage(image, 0, 0, width, height);
        }

        // Рисуем изображение карты под курсором
        if (cardImage) {
            // Явное преобразование типов для устранения неоднозначности
            int cardX = cursorPos.x - cardImage->GetWidth() / 2;
            int cardY = cursorPos.y - cardImage->GetHeight() / 2;
            graphics.DrawImage(cardImage, static_cast<float>(cardX), static_cast<float>(cardY), static_cast<float>(cardImage->GetWidth()), static_cast<float>(cardImage->GetHeight()));
        }

        EndPaint(hwnd, &ps);
    }
    break;
    case WM_SIZE:
        // Обрабатываем изменение размера окна
        InvalidateRect(hwnd, NULL, TRUE); // Запрашиваем перерисовку
        break;
    case WM_DESTROY:
        delete image; // Освобождаем память
        delete cardImage; // Освобождаем память для карты
        PostQuitMessage(0);
        break;
    case WM_MOUSEMOVE:
        // Обновляем позицию курсора
        cursorPos.x = LOWORD(lParam);
        cursorPos.y = HIWORD(lParam);
        InvalidateRect(hwnd, NULL, TRUE); // Запрашиваем перерисовку
        break;
    default:
        return DefWindowProc(hwnd, uMsg, wParam, lParam);
    }
    return 0;
}

int WINAPI wWinMain(HINSTANCE hInstance, HINSTANCE, PWSTR, int nShowCmd) {
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

    // Основной цикл сообщений
    MSG msg;
    while (GetMessage(&msg, NULL, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }

    GdiplusShutdown(gdiplusToken);
    return 0;
}
