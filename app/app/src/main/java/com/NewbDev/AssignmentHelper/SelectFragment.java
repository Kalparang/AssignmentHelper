package com.NewbDev.AssignmentHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SelectFragment extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.Default1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.InitPageNum = 1;
                NavHostFragment.findNavController(SelectFragment.this)
                        .navigate(R.id.action_SelectFragment_to_FirstFragment);
            }
        });
        view.findViewById(R.id.Default2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.InitPageNum = 2;
                NavHostFragment.findNavController(SelectFragment.this)
                        .navigate(R.id.action_SelectFragment_to_FirstFragment);
            }
        });
        view.findViewById(R.id.Custom1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.InitPageNum = 1;
                NavHostFragment.findNavController(SelectFragment.this)
                        .navigate(R.id.action_SelectFragment_to_FirstFragment);
            }
        });
        view.findViewById(R.id.Custom2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.InitPageNum = 2;
                NavHostFragment.findNavController(SelectFragment.this)
                        .navigate(R.id.action_SelectFragment_to_FirstFragment);
            }
        });
    }
}
