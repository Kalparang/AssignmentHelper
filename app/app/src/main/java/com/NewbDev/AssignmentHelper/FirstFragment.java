package com.NewbDev.AssignmentHelper;

import android.content.Context;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static androidx.core.content.ContextCompat.getSystemService;

public class FirstFragment extends Fragment {

    private KeyCodeStruct KeyMap[][][];
    private static Button VBList[];
    private int PageNum;
    ConnectWindow cw;
    private static Vibrator mVibe;

    private View.OnClickListener VBListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String sp[] = ((String)v.getTag()).split("_");

            if(sp.length == 3)
            {
                int row = Integer.parseInt(sp[1]) - 1;
                int col = Integer.parseInt(sp[2]) - 1;

                if(KeyMap[row][col][0] == null)
                    return;
                if(KeyMap[row][col][0].KeyCode == 0)
                    return;

                cw.SendData(KeyMap[row][col]);

                MainActivity.vibrator.vibrate(VibrationEffect.createOneShot(40, 30));
                MainActivity.audioManager.playSoundEffect(SoundEffectConstants.CLICK);
            }
        }
    };

    public static void ButtonClickable(boolean able){
        for(int i = 0; i < ManageKey.col * ManageKey.row; i++) {
            if(VBList[i] == null)
                continue;
            VBList[i].setClickable(able);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cw.StopServer();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        PageNum = MainActivity.InitPageNum;
        InitButton(view);
        cw = new ConnectWindow();

        super.onViewCreated(view, savedInstanceState);

        cw = new ConnectWindow();
        cw.StartServer();

        cw.setConnStat(MainActivity.ConnStat);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                if(PageNum == 1) {
                    PageNum = 2;
                    ChangePage(view, PageNum);
                }
                else
                    PageNum = 1;
                    ChangePage(view, PageNum);
            }
        });
    }

    private void InitButton(View view)
    {
        ManageKey mKey = new ManageKey();
        KeyMap = mKey.getKeyMap(PageNum);

        String KeyLabel[][] = mKey.getKeyLabel(PageNum);
        VBList = new Button[mKey.row * mKey.col];

        for(int i = 0; i < ManageKey.row; i++)
        {
            for(int j = 0; j < ManageKey.col; j++)
            {
                    int index = j;
                    if (i > 0)
                        index += i * 4;

                    VBList[index] = view.findViewWithTag("VB_" + (i + 1) + "_" + (j + 1));
                    if(VBList[index] == null){
                        continue;
                    }
                    VBList[index].setText(KeyLabel[i][j]);
                    VBList[index].setOnClickListener(VBListener);
                    VBList[index].setClickable(false);
            }
        }
    }

    private void ChangePage(View view, int pagenum) {
        ButtonClickable(false);

        ManageKey mKey = new ManageKey();
        KeyMap = mKey.getKeyMap(pagenum);

        String KeyLabel[][] = mKey.getKeyLabel(pagenum);

        for (int i = 0; i < ManageKey.row; i++) {
            for (int j = 0; j < ManageKey.col; j++) {
                int index = j;
                if(i > 0)
                    index += i * 4;

                if(VBList[index] == null)
                    continue;

                VBList[index].setText(KeyLabel[i][j]);
            }
        }

        if(ConnectWindow.IsConnected)
            ButtonClickable(true);
    }
}