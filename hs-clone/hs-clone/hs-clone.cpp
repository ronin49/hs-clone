#include <windows.h>
#include <gdiplus.h>
#include <string>

#pragma comment(lib, "gdiplus.lib")

using namespace Gdiplus;

// Глобальные переменные
Image* image = nullptr; // Изображение поля
Image* cardImage = nullptr; // Изображение карты
POINT cardPos; // Позиция карты
bool isDragging = false; // Флаг для перетаскивания карты

// Устанавливаем размеры карты
const int cardWidth = 115; // Ширина карты
const int cardHeight = 270; // Высота карты

// Функция для загрузки изображений
void LoadResources() {
    image = new Image(L"Images\\field.png");
    cardImage = new Image(L"Images\\card.png");
}

// Обработчик сообщений
LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    static int scaledCardWidth = 0; // Ширина карты после масштабирования
    static int scaledCardHeight = 0; // Высота карты после масштабирования

    switch (uMsg) {
    case WM_CREATE:
        LoadResources(); // Загрузка ресурсов
        cardPos.x = -63;//Установите начальную позицию по оси X
        cardPos.y = 700;// Установите начальную позицию по оси Y
        SetTimer(hwnd, 1, 1000, NULL); // Устанавливаем таймер для обновления каждые 2 секунды
        break;
    case WM_PAINT:
    {
        PAINTSTRUCT ps;
        HDC hdc = BeginPaint(hwnd, &ps);

        // Получаем размеры клиентской области
        RECT clientRect;
        GetClientRect(hwnd, &clientRect);
        int width = clientRect.right - clientRect.left;
        int height = clientRect.bottom - clientRect.top;

        // Создаем буфер для отрисовки
        HDC hdcMem = CreateCompatibleDC(hdc);
        HBITMAP hBitmap = CreateCompatibleBitmap(hdc, width, height);
        SelectObject(hdcMem, hBitmap);

        Graphics graphics(hdcMem);

        // Рисуем изображение поля
        if (image) {
            graphics.DrawImage(image, 0, 0, width, height);
        }

        // Масштабируем карту
        float scaleX = static_cast<float>(width) / 800;
        float scaleY = static_cast<float>(height) / 600;

        scaledCardWidth = static_cast<int>(cardWidth * scaleX);
        scaledCardHeight = static_cast<int>(cardHeight * scaleY);

        // Рисуем изображение карты
        if (cardImage) {
            graphics.DrawImage(cardImage, static_cast<float>(cardPos.x), static_cast<float>(cardPos.y), static_cast<float>(scaledCardWidth), static_cast<float>(scaledCardHeight));
        }

        // Копируем буфер на экран
        BitBlt(hdc, 0, 0, width, height, hdcMem, 0, 0, SRCCOPY);

        // Освобождаем ресурсы
        DeleteObject(hBitmap);
        DeleteDC(hdcMem);

        EndPaint(hwnd, &ps);
    }
    break;
    case WM_TIMER:
        InvalidateRect(hwnd, NULL, TRUE); // Запрашиваем перерисовку
        break;
    case WM_SIZE:
        InvalidateRect(hwnd, NULL, TRUE);
        break;
    case WM_DESTROY:
        delete image;
        delete cardImage;
        KillTimer(hwnd, 1); // Останавливаем таймер
        PostQuitMessage(0);
        break;
    case WM_LBUTTONDOWN:
        // Проверяем, находится ли курсор над картой
        if (LOWORD(lParam) >= cardPos.x && LOWORD(lParam) <= cardPos.x + scaledCardWidth &&
            HIWORD(lParam) >= cardPos.y && HIWORD(lParam) <= cardPos.y + scaledCardHeight) {
            isDragging = true; // Начинаем перетаскивание
        }
        break;
    case WM_LBUTTONUP:
        isDragging = false; // Заканчиваем перетаскивание
        break;
    case WM_MOUSEMOVE:
        if (isDragging) {
            cardPos.x = LOWORD(lParam) - (scaledCardWidth / 2); // Центрируем карту под курсором
            cardPos.y = HIWORD(lParam) - (scaledCardHeight / 2);
            InvalidateRect(hwnd, NULL, TRUE); // Запрашиваем перерисовку
        }
        break;
    case WM_SETCURSOR:
    {
        HCURSOR hCursor = LoadCursorFromFile(L"Images//hs-pointer.ani"); // Замените на путь к вашему курсору
        if (hCursor) {
            SetCursor(hCursor);
        }
        else {
            SetCursor(LoadCursor(NULL, IDC_ARROW)); // Если курсор не загружен, используем стандартный
        }
        return TRUE; // Указываем, что курсор установлен
    }
    case WM_KEYDOWN:
        if (wParam == VK_ESCAPE) {
            ShowWindow(hwnd, SW_RESTORE); // Выход из полноэкранного режима
        }
        break;
    default:
        return DefWindowProc(hwnd, uMsg, wParam, lParam);
    }
    return 0;
}

int WINAPI wWinMain(HINSTANCE hInstance, HINSTANCE, PWSTR, int nCmdShow) {
    GdiplusStartupInput gdiplusStartupInput;
    ULONG_PTR gdiplusToken;
    GdiplusStartup(&gdiplusToken, &gdiplusStartupInput, NULL);

    const wchar_t CLASS_NAME[] = L"Sample Window Class";

    WNDCLASS wc = {};
    wc.lpfnWndProc = WindowProc;
    wc.hInstance = hInstance;
    wc.lpszClassName = CLASS_NAME;

    RegisterClass(&wc);

    int screenWidth = GetSystemMetrics(SM_CXSCREEN);
    int screenHeight = GetSystemMetrics(SM_CYSCREEN);

    HWND hwnd = CreateWindowExW(
        0, CLASS_NAME, L"Image Display", WS_POPUP,
        0, 0, screenWidth, screenHeight,
        NULL, NULL, hInstance, NULL
    );

    ShowWindow(hwnd, nCmdShow);

    MSG msg;
    while (GetMessage(&msg, NULL, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }

    GdiplusShutdown(gdiplusToken);
    return 0;
}
