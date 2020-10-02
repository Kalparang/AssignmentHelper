using System;
using System.Runtime.InteropServices;
using System.Windows.Forms;

public static class KeyboardInput
{
    [DllImport("user32.dll")]
    private static extern void keybd_event(uint vk, uint scan, uint flags, uint extraInfo);

    [DllImport("user32.dll")]
    private static extern uint MapVirtualKey(int wCode, int wMapType);

    [DllImport("imm32.dll")]
    private static extern IntPtr ImmGetDefaultIMEWnd(IntPtr hWnd);

    [DllImport("user32.dll", CharSet = CharSet.Auto)]
    private static extern IntPtr SendMessage(IntPtr hWnd, UInt32 Msg, IntPtr wParam, IntPtr IParam);

    [DllImport("user32")]
    private static extern IntPtr GetForegroundWindow();

    private const int WM_IME_CONTROL = 643;
    private const int KEYEVENTF_KEYUP = 0x0002;
    
    public enum KeyCode
    {
        VK_SHIFT = 0x10,
        VK_CONTROL = 0x11,
        VK_ALT = 0x12,
        VK_HANGEUL = 0x15,
    }

    private static bool IsEnglish()
    {
        IntPtr hwnd = GetForegroundWindow();
        IntPtr hime = ImmGetDefaultIMEWnd(hwnd);
        IntPtr status = SendMessage(hime, WM_IME_CONTROL, new IntPtr(0x5), new IntPtr(0));

        if (status.ToInt32() != 0)
            return false;
        else
            return true;
    }

    public static void Presskey(KeyCode vk, bool shift = false)
    {
        Presskey((int)vk, shift);
    }

    public static void Presskey(int vk, bool shift = false)
    {
        bool ChangeIME = !IsEnglish();

        if (ChangeIME)
        {
            keybd_event((uint)KeyCode.VK_HANGEUL, MapVirtualKey((int)KeyCode.VK_HANGEUL, 0), 0, 0);
            keybd_event((uint)KeyCode.VK_HANGEUL, MapVirtualKey((int)KeyCode.VK_HANGEUL, 0), KEYEVENTF_KEYUP, 0);
        }

        if (shift)
            keybd_event((uint)KeyCode.VK_SHIFT, MapVirtualKey((int)KeyCode.VK_SHIFT, 0), 0, 0);

        keybd_event((uint)vk, MapVirtualKey(vk, 0), 0, 0);
        keybd_event((uint)vk, MapVirtualKey(vk, 0), KEYEVENTF_KEYUP, 0);

        if (shift)
            keybd_event((uint)KeyCode.VK_SHIFT, MapVirtualKey((int)KeyCode.VK_SHIFT, 0), KEYEVENTF_KEYUP, 0);

        if (ChangeIME)
        {
            keybd_event((uint)KeyCode.VK_HANGEUL, MapVirtualKey((int)KeyCode.VK_HANGEUL, 0), 0, 0);
            keybd_event((uint)KeyCode.VK_HANGEUL, MapVirtualKey((int)KeyCode.VK_HANGEUL, 0), KEYEVENTF_KEYUP, 0);
        }
    }
}