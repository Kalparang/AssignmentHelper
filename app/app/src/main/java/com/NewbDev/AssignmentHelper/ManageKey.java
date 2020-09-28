package com.NewbDev.AssignmentHelper;

import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import java.util.HashMap;

public class ManageKey {
    public static final int row = 6, col = 4;
    private static int KeyMap[][][][];
    private static String KeyLabel[][][];
    private static HashMap<Integer, String> KeyCode;

    public void Init()
    {
        KeyCode = new HashMap<Integer, String>();
        {
            KeyCode.put(0x01, "LMB");
            KeyCode.put(0x02, "RMB");
            KeyCode.put(0x03, "Cancel");
            KeyCode.put(0x04, "MMB");
            KeyCode.put(0x05, "X1");
            KeyCode.put(0x06, "X2");
            KeyCode.put(0x08, "Backspace");
            KeyCode.put(0x09, "Tab");
            KeyCode.put(0x0C, "Clear");
            KeyCode.put(0x0D, "Enter");
            KeyCode.put(0x10, "Shift");
            KeyCode.put(0x11, "Ctrl");
            KeyCode.put(0x12, "Alt");
            KeyCode.put(0x13, "Pause");
            KeyCode.put(0x14, "CapsLock");
            KeyCode.put(0x15, "Kana");
            KeyCode.put(0x15, "한/영");
            KeyCode.put(0x16, "On");
            KeyCode.put(0x17, "Junja");
            KeyCode.put(0x18, "Final");
            KeyCode.put(0x19, "한자");
            KeyCode.put(0x1A, "Off");
            KeyCode.put(0x1B, "ESC");
            KeyCode.put(0x1C, "Convert");
            KeyCode.put(0x1D, "nonconvert");
            KeyCode.put(0x1E, "Accept");
            KeyCode.put(0x1F, "ModeChange");
            KeyCode.put(0x20, "Space");
            KeyCode.put(0x21, "PgUp");
            KeyCode.put(0x22, "PgDn");
            KeyCode.put(0x23, "End");
            KeyCode.put(0x24, "Home");
            KeyCode.put(0x25, "←");
            KeyCode.put(0x26, "↑");
            KeyCode.put(0x27, "→");
            KeyCode.put(0x28, "↓");
            KeyCode.put(0x29, "Select");
            KeyCode.put(0x2A, "Print");
            KeyCode.put(0x2B, "Execute");
            KeyCode.put(0x2C, "PrintScr");
            KeyCode.put(0x2D, "Insert");
            KeyCode.put(0x2E, "Del");
            KeyCode.put(0x2F, "Help");
            KeyCode.put(0x30, "0");
            KeyCode.put(0x31, "1");
            KeyCode.put(0x32, "2");
            KeyCode.put(0x33, "3");
            KeyCode.put(0x34, "4");
            KeyCode.put(0x35, "5");
            KeyCode.put(0x36, "6");
            KeyCode.put(0x37, "7");
            KeyCode.put(0x38, "8");
            KeyCode.put(0x39, "9");
            KeyCode.put(0x41, "A");
            KeyCode.put(0x42, "B");
            KeyCode.put(0x43, "C");
            KeyCode.put(0x44, "D");
            KeyCode.put(0x45, "E");
            KeyCode.put(0x46, "F");
            KeyCode.put(0x47, "G");
            KeyCode.put(0x48, "H");
            KeyCode.put(0x49, "I");
            KeyCode.put(0x4A, "J");
            KeyCode.put(0x4B, "K");
            KeyCode.put(0x4C, "L");
            KeyCode.put(0x4D, "M");
            KeyCode.put(0x4E, "N");
            KeyCode.put(0x4F, "O");
            KeyCode.put(0x50, "P");
            KeyCode.put(0x51, "Q");
            KeyCode.put(0x52, "R");
            KeyCode.put(0x53, "S");
            KeyCode.put(0x54, "T");
            KeyCode.put(0x55, "U");
            KeyCode.put(0x56, "V");
            KeyCode.put(0x57, "W");
            KeyCode.put(0x58, "X");
            KeyCode.put(0x59, "Y");
            KeyCode.put(0x5A, "Z");
            KeyCode.put(0x5B, "LWin");
            KeyCode.put(0x5C, "RWin");
            KeyCode.put(0x5D, "Apps");
            KeyCode.put(0x5F, "Sleep");
            KeyCode.put(0x60, "Num0");
            KeyCode.put(0x61, "Num1");
            KeyCode.put(0x62, "Num2");
            KeyCode.put(0x63, "Num3");
            KeyCode.put(0x64, "Num4");
            KeyCode.put(0x65, "Num5");
            KeyCode.put(0x66, "Num6");
            KeyCode.put(0x67, "Num7");
            KeyCode.put(0x68, "Num8");
            KeyCode.put(0x69, "Num9");
            KeyCode.put(0x6A, "*");
            KeyCode.put(0x6B, "+");
            KeyCode.put(0x6C, "NumEnter");
            KeyCode.put(0x6D, "-");
            KeyCode.put(0x6E, ".");
            KeyCode.put(0x6F, "/");
            KeyCode.put(0x70, "F1");
            KeyCode.put(0x71, "F2");
            KeyCode.put(0x72, "F3");
            KeyCode.put(0x73, "F4");
            KeyCode.put(0x74, "F6");
            KeyCode.put(0x75, "F7");
            KeyCode.put(0x76, "F8");
            KeyCode.put(0x77, "F9");
            KeyCode.put(0x78, "F10");
            KeyCode.put(0x79, "F11");
            KeyCode.put(0x7A, "F11");
            KeyCode.put(0x7B, "F12");
            KeyCode.put(0x7C, "F13");
            KeyCode.put(0x7D, "F14");
            KeyCode.put(0x7E, "F15");
            KeyCode.put(0x7F, "F16");
            KeyCode.put(0x80, "F17");
            KeyCode.put(0x81, "F18");
            KeyCode.put(0x82, "F19");
            KeyCode.put(0x83, "F20");
            KeyCode.put(0x84, "F21");
            KeyCode.put(0x85, "F22");
            KeyCode.put(0x86, "F23");
            KeyCode.put(0x87, "F24");
            KeyCode.put(0x90, "NumLock");
            KeyCode.put(0x91, "ScrollLock");
            KeyCode.put(0xA0, "LShift");
            KeyCode.put(0xA1, "RShift");
            KeyCode.put(0xA2, "LCtrl");
            KeyCode.put(0xA3, "RCtrl");
            KeyCode.put(0xA4, "LMenu");
            KeyCode.put(0xA5, "RMenu");
            KeyCode.put(0xA6, "BrowserBack");
            KeyCode.put(0xA7, "BrowserForward");
            KeyCode.put(0xA8, "BrowserRefresh");
            KeyCode.put(0xA9, "BrowserStop");
            KeyCode.put(0xAA, "BrowserSearch");
            KeyCode.put(0xAB, "BrowserFavorites");
            KeyCode.put(0xAC, "BrowserHome");
            KeyCode.put(0xAD, "VolMute");
            KeyCode.put(0xAE, "VolDown");
            KeyCode.put(0xAF, "VolUp");
            KeyCode.put(0xB0, "Next Track");
            KeyCode.put(0xB1, "Prev Track");
            KeyCode.put(0xB2, "Stop Media");
            KeyCode.put(0xB3, "Paly/Pause");
            KeyCode.put(0xB4, "Mail");
            KeyCode.put(0xB5, "Select Media");
            KeyCode.put(0xB6, "App1");
            KeyCode.put(0xB7, "App2");
        }

        InitKeyMap();
        InitKeyLabel();
    }

    public int[][][] getKeyMap(int index)
    {
        if(index < 0)
            return null;
        if(index > KeyMap.length)
            return null;

        return KeyMap[index - 1];
    }

    public String[][] getKeyLabel(int index)
    {
        if(index < 0)
            return null;
        if(index > KeyLabel.length)
            return null;

        return KeyLabel[index - 1];
    }

    public HashMap<Integer, String> getKeyCode()
    {
        return KeyCode;
    }

    private void InitKeyMap()
    {
        //키맵 초기화
        KeyMap = new int[2][row][col][1];

        //저장된 키맵 가져오기
        //미구현

        //기본 키맵
        //1번 키패드
        {
            KeyMap[0][0][0][0] = 0x4A;
            KeyMap[0][0][1][0] = 0x6F;
            KeyMap[0][0][2][0] = 0x6A;
            KeyMap[0][0][3][0] = 0x6D;

            KeyMap[0][1][0][0] = 0x67;
            KeyMap[0][1][1][0] = 0x68;
            KeyMap[0][1][2][0] = 0x69;
            KeyMap[0][1][3][0] = 0x6B;

            KeyMap[0][2][0][0] = 0x64;
            KeyMap[0][2][1][0] = 0x65;
            KeyMap[0][2][2][0] = 0x66;
            KeyMap[0][2][3][0] = 0x6B;

            KeyMap[0][3][0][0] = 0x61;
            KeyMap[0][3][1][0] = 0x62;
            KeyMap[0][3][2][0] = 0x63;
            KeyMap[0][3][3][0] = 0x6C;

            KeyMap[0][4][0][0] = 0x60;

            //00
            KeyMap[0][4][1] = new int[2];
            KeyMap[0][4][1][0] = 0x60;
            KeyMap[0][4][1][1] = 0x60;

            KeyMap[0][4][2][0] = 0x6E;
            KeyMap[0][4][3][0] = 0x6C;

            KeyMap[0][5][0][0] = 0x4A;

            //3.14
            KeyMap[0][5][1] = new int[4];
            KeyMap[0][5][1][0] = 0x63;
            KeyMap[0][5][1][1] = 0x6E;
            KeyMap[0][5][1][2] = 0x61;
            KeyMap[0][5][1][3] = 0x64;

            //exp() 후 왼쪽 한칸
            KeyMap[0][5][2] = new int[6];
            KeyMap[0][5][2][0] = 0x45;
            KeyMap[0][5][2][1] = 0x58;
            KeyMap[0][5][2][2] = 0x50;
            KeyMap[0][5][2][3] = 0x39; //Shift
            KeyMap[0][5][2][4] = 0x30; //Shift
            KeyMap[0][5][2][5] = 0x25;

            KeyMap[0][5][3][0] = 0x6C;

        }

        //2번 키패드
        {
            KeyMap[1][0][0][0] = 'j';
            KeyMap[1][0][1][0] = 111;
            KeyMap[1][0][2][0] = 106;
            KeyMap[1][0][3][0] = 109;

            KeyMap[1][1][0][0] = 120;
            KeyMap[1][1][1][0] = 121;
            KeyMap[1][1][2][0] = 122;
            KeyMap[1][1][3][0] = 123;

            KeyMap[1][2][0][0] = 116;
            KeyMap[1][2][1][0] = 117;
            KeyMap[1][2][2][0] = 118;
            KeyMap[1][2][3][0] = 119;

            KeyMap[1][3][0][0] = 112;
            KeyMap[1][3][1][0] = 113;
            KeyMap[1][3][2][0] = 114;
            KeyMap[1][3][3][0] = 115;

            KeyMap[1][4][0][0] = 96;
            KeyMap[1][4][1] = new int[2];
            KeyMap[1][4][1][0] = 96;
            KeyMap[1][4][1][1] = 96;
            KeyMap[1][4][2][0] = 110;
            KeyMap[1][4][3][0] = 13;
        }
    }

    private void InitKeyLabel()
    {
        KeyLabel = new String[2][row][col];

        //저장된 라벨 가져오기
        //미구현
        KeyLabel[0][1][0] = "7";
        KeyLabel[0][1][1] = "8";
        KeyLabel[0][1][2] = "9";

        KeyLabel[0][2][0] = "4";
        KeyLabel[0][2][1] = "5";
        KeyLabel[0][2][2] = "6";

        KeyLabel[0][3][0] = "1";
        KeyLabel[0][3][1] = "2";
        KeyLabel[0][3][2] = "3";

        KeyLabel[0][4][1] = "00";
        KeyLabel[0][5][0] = "j";
        KeyLabel[0][5][1] = "pi";
        KeyLabel[0][5][2] = "e";

        //저장된 라벨이 없다면 기본 라벨 지정
        for(int k = 0; k < 2; k++) {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if(KeyLabel[k][i][j] != null)
                        continue;

                    if (KeyMap[k][i][j].length > 1)
                        KeyLabel[k][i][j] = "매크로";
                    else {
                        String value = KeyCode.get(KeyMap[k][i][j][0]);
                        if(value == null)
                            value = KeyEvent.keyCodeToString(KeyMap[k][i][j][0]);
                        KeyLabel[k][i][j] = value;
                    }
                }
            }
        }
    }
}
