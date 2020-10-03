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

    private View.OnClickListener VBListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String sp[] = ((String)v.getTag()).split("_");
            if(sp.length == 4)
            {
                int index = Integer.parseInt(sp[1]) - 1;
                int row = Integer.parseInt(sp[2]) - 1;
                int col = Integer.parseInt(sp[3]) - 1;

                if(KeyMap[row][col][0].KeyCode == 0)
                    return;

                cw.SendData(KeyMap[row][col]);
            }
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
                VBList[index].setOnClickListener(VBListener);
            }
        }
    }
}