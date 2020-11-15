package com.NewbDev.AssignmentHelper;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Vibrator;
import android.view.View;

import android.view.WindowManager;
import android.widget.TextView;

import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static ManageKey mKey;
    public static KeyCodeStruct KeyMap[][][][];
    public static HashMap<Integer, String> KeyCode;
    public static View HelpButton;
    public static Vibrator vibrator;
    public static AudioManager audioManager;
    public static TextView ConnStat;
    public static int InitPageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mKey = new ManageKey();
        mKey.Init();
        KeyCode = mKey.getKeyCode();

        KeyMap = new KeyCodeStruct[2][][][];

        KeyMap[0] = mKey.getKeyMap(1);
        KeyMap[1] = mKey.getKeyMap(2);

        ConnStat = (TextView) findViewById(R.id.TV_ConnStat);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), HelpPage.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}