package com.NewbDev.AssignmentHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

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
        int KeyMap[][][] = mKey.getKeyMap(2);
        String KeyLabel[][] = mKey.getKeyLabel(2);

        for(int i = 0; i < mKey.row; i++)
        {
            for(int j = 0; j < mKey.col; j++)
            {
                Button b = view.findViewWithTag("VB_2_" + (i + 1) + "_" + (j + 1));

                b.setText(KeyLabel[i][j]);
            }
        }
    }
}