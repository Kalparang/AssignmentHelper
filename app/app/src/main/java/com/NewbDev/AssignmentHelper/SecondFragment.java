package com.NewbDev.AssignmentHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    private KeyCodeStruct KeyMap[][][];
    private static Button VBList[];
    ConnectWindow cw;

    private View.OnTouchListener VBListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            String sp[] = ((String)v.getTag()).split("_");
            boolean IsKeyDown = (event.getAction() == MotionEvent.ACTION_DOWN);
            if(!IsKeyDown)
                return false;
            if(sp.length == 4)
            {
                int index = Integer.parseInt(sp[1]) - 1;
                int row = Integer.parseInt(sp[2]) - 1;
                int col = Integer.parseInt(sp[3]) - 1;

                if(KeyMap[row][col][0].KeyCode == 0)
                    return true;

                cw.SendData(KeyMap[row][col]);
            }

            return true;
        }
    };

    public static void ButtonClickable(boolean able){
        for(int i = 0; i < ManageKey.col * ManageKey.row; i++)
            VBList[i].setClickable(able);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        InitButton(view);
        cw = new ConnectWindow();

        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    private void InitButton(View view)
    {
        ManageKey mKey = new ManageKey();
        KeyMap = mKey.getKeyMap(2);

        String KeyLabel[][] = mKey.getKeyLabel(2);
        VBList = new Button[mKey.row * mKey.col];

        for(int i = 0; i < mKey.row; i++)
        {
            for(int j = 0; j < mKey.col; j++)
            {
                int index = j;
                if(i > 0)
                    index += i * 4;

                VBList[index] = view.findViewWithTag("VB_2_" + (i + 1) + "_" + (j + 1));

                VBList[index].setText(KeyLabel[i][j]);
                VBList[index].setOnTouchListener(VBListener);
            }
        }

//        view.findViewById(R.id.VB_2_1_1).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_1_2).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_1_3).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_1_4).setOnTouchListener(VBListener);
//
//        view.findViewById(R.id.VB_2_2_1).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_2_2).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_2_3).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_2_4).setOnTouchListener(VBListener);
//
//        view.findViewById(R.id.VB_2_3_1).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_3_2).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_3_3).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_3_4).setOnTouchListener(VBListener);
//
//        view.findViewById(R.id.VB_2_4_1).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_4_2).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_4_3).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_4_4).setOnTouchListener(VBListener);
//
//        view.findViewById(R.id.VB_2_5_1).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_5_2).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_5_3).setOnTouchListener(VBListener);
//        view.findViewById(R.id.VB_2_5_4).setOnTouchListener(VBListener);
    }
}