package com.NewbDev.AssignmentHelper;

import java.io.Serializable;

public class KeyCodeStruct implements Serializable {
    boolean IsShiftDown;
    boolean IsCtrlDown;
    boolean IsAltDown;
    int KeyCode;

    KeyCodeStruct(){
        KeyCode = 0;
        IsShiftDown = false;
        IsCtrlDown = false;
        IsAltDown = false;
    }
    KeyCodeStruct(int KeyCode) {
        this.KeyCode = KeyCode;

        IsShiftDown = false;
        IsCtrlDown = false;
        IsAltDown = false;
    }
    KeyCodeStruct(
            int KeyCode,
            boolean IsShiftDown,
            boolean IsCtrlDown,
            boolean IsAltDown
    ){
        this.KeyCode = KeyCode;
        this.IsShiftDown = IsShiftDown;
        this.IsCtrlDown = IsCtrlDown;
        this.IsAltDown = IsAltDown;
    }
}
