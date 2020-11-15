using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Threading;
using System.Windows.Forms;

public class KeyboardInput
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

    bool ChangeIME = false;

    public enum KeyCode
    {
        VK_SHIFT = 0x10,
        VK_CONTROL = 0x11,
        VK_ALT = 0x12,
        VK_HANGEUL = 0x15,
    }

    public struct VK
    {
        public bool IsShiftDown;
        public bool IsCtrlDown;
        public bool IsAltDown;
        public int KeyCode;
    }

    private bool IsEnglish()
    {
        IntPtr hwnd = GetForegroundWindow();
        IntPtr hime = ImmGetDefaultIMEWnd(hwnd);
        IntPtr status = SendMessage(hime, WM_IME_CONTROL, new IntPtr(0x5), new IntPtr(0));

        if (status.ToInt32() != 0)
            return false;
        else
            return true;
    }

    public void Presskey(KeyCode vk, bool shift = false, bool ctrl = false)
    {
        Presskey((int)vk, shift, ctrl);
    }

    public void Presskey(int vk, bool shift = false, bool ctrl = false)
    {
        if (shift)
            keybd_event((uint)KeyCode.VK_SHIFT, MapVirtualKey((int)KeyCode.VK_SHIFT, 0), 0, 0);
        if(ctrl)
            keybd_event((uint)KeyCode.VK_CONTROL, MapVirtualKey((int)KeyCode.VK_CONTROL, 0), 0, 0);

        if (vk >= 0xF1)
        {
            switch(vk)
            {
                case 0xF1:
                    SendKeys.SendWait("√");
                    break;
                case 0xF2:
                    SendKeys.SendWait("π");
                    break;
                default:
                    break;
            }
        }
        else
        {
            keybd_event((uint)vk, MapVirtualKey(vk, 0), 0, 0);
            keybd_event((uint)vk, MapVirtualKey(vk, 0), KEYEVENTF_KEYUP, 0);
        }

        if (ctrl)
            keybd_event((uint)KeyCode.VK_CONTROL, MapVirtualKey((int)KeyCode.VK_CONTROL, 0), KEYEVENTF_KEYUP, 0);
        if (shift)
            keybd_event((uint)KeyCode.VK_SHIFT, MapVirtualKey((int)KeyCode.VK_SHIFT, 0), KEYEVENTF_KEYUP, 0);
    }

    public void ReadyKey(bool IsStart)
    {
        if (IsStart)
        {
            ChangeIME = !IsEnglish();

            if (ChangeIME)
            {
                keybd_event((uint)KeyCode.VK_HANGEUL, MapVirtualKey((int)KeyCode.VK_HANGEUL, 0), 0, 0);
                keybd_event((uint)KeyCode.VK_HANGEUL, MapVirtualKey((int)KeyCode.VK_HANGEUL, 0), KEYEVENTF_KEYUP, 0);

                Thread.Sleep(50);
            }
        }
        else
        {
            if(ChangeIME)
            {
                if (ChangeIME)
                {
                    keybd_event((uint)KeyCode.VK_HANGEUL, MapVirtualKey((int)KeyCode.VK_HANGEUL, 0), 0, 0);
                    keybd_event((uint)KeyCode.VK_HANGEUL, MapVirtualKey((int)KeyCode.VK_HANGEUL, 0), KEYEVENTF_KEYUP, 0);

                    Thread.Sleep(50);
                }
            }
        }    
    }
}