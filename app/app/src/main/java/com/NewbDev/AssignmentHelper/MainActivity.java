package com.NewbDev.AssignmentHelper;

import com.NewbDev.AssignmentHelper.ManageKey;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.net.ssl.ManagerFactoryParameters;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public ServerSocket serverSocket;
    public static Socket socket = null;
    public static DataInputStream inputStream;
    public static DataOutputStream outputStream;

    public static ManageKey mKey;
    public static int KeyMap[][][][];
    public static HashMap<Integer, String> KeyCode;
    ServerThread serverThread;
    Thread socketThread;


    public void VB_OnClick(View view){
        String sp[] = ((String)view.getTag()).split("_");
        if(sp.length == 4)
        {
            int index = Integer.parseInt(sp[1]) - 1;
            int row = Integer.parseInt(sp[2]) - 1;
            int col = Integer.parseInt(sp[3]) - 1;

            String output = String.valueOf(KeyMap[index][row][col][0]);

            for(int i = 1; i < KeyMap[index][row][col].length; i++)
                output = output + "," + String.valueOf(KeyMap[index][row][col][i]);

            new Thread(new SocketThread(output)).start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mKey = new ManageKey();
        mKey.Init();
        KeyCode = mKey.getKeyCode();

        KeyMap = new int[2][][][];

        KeyMap[0] = mKey.getKeyMap(1);
        KeyMap[1] = mKey.getKeyMap(2);

        serverThread = new ServerThread();
        serverThread.start();
    }

    private void InitButton()
    {
        findViewById(R.id.VB_1_1_1).setOnClickListener(this);
        findViewById(R.id.VB_1_1_2).setOnClickListener(this);
        findViewById(R.id.VB_1_1_3).setOnClickListener(this);
        findViewById(R.id.VB_1_1_4).setOnClickListener(this);

        findViewById(R.id.VB_1_2_1).setOnClickListener(this);
        findViewById(R.id.VB_1_2_2).setOnClickListener(this);
        findViewById(R.id.VB_1_2_3).setOnClickListener(this);
        findViewById(R.id.VB_1_2_4).setOnClickListener(this);

        findViewById(R.id.VB_1_3_1).setOnClickListener(this);
        findViewById(R.id.VB_1_3_2).setOnClickListener(this);
        findViewById(R.id.VB_1_3_3).setOnClickListener(this);
        findViewById(R.id.VB_1_3_4).setOnClickListener(this);

        findViewById(R.id.VB_1_4_1).setOnClickListener(this);
        findViewById(R.id.VB_1_4_2).setOnClickListener(this);
        findViewById(R.id.VB_1_4_3).setOnClickListener(this);
        findViewById(R.id.VB_1_4_4).setOnClickListener(this);

        findViewById(R.id.VB_1_5_1).setOnClickListener(this);
        findViewById(R.id.VB_1_5_2).setOnClickListener(this);
        findViewById(R.id.VB_1_5_3).setOnClickListener(this);
        findViewById(R.id.VB_1_5_4).setOnClickListener(this);


        findViewById(R.id.VB_2_1_1).setOnClickListener(this);
        findViewById(R.id.VB_2_1_2).setOnClickListener(this);
        findViewById(R.id.VB_2_1_3).setOnClickListener(this);
        findViewById(R.id.VB_2_1_4).setOnClickListener(this);
        findViewById(R.id.VB_2_2_1).setOnClickListener(this);
        findViewById(R.id.VB_2_2_2).setOnClickListener(this);
        findViewById(R.id.VB_2_2_3).setOnClickListener(this);
        findViewById(R.id.VB_2_2_4).setOnClickListener(this);
        findViewById(R.id.VB_2_3_1).setOnClickListener(this);
        findViewById(R.id.VB_2_3_2).setOnClickListener(this);
        findViewById(R.id.VB_2_3_3).setOnClickListener(this);
        findViewById(R.id.VB_2_3_4).setOnClickListener(this);
        findViewById(R.id.VB_2_4_1).setOnClickListener(this);
        findViewById(R.id.VB_2_4_2).setOnClickListener(this);
        findViewById(R.id.VB_2_4_3).setOnClickListener(this);
        findViewById(R.id.VB_2_4_4).setOnClickListener(this);
        findViewById(R.id.VB_2_5_1).setOnClickListener(this);
        findViewById(R.id.VB_2_5_2).setOnClickListener(this);
        findViewById(R.id.VB_2_5_3).setOnClickListener(this);
        findViewById(R.id.VB_2_5_4).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        String sp[] = ((String)view.getTag()).split("_");
        if(sp.length == 3)
        {
            int index = sp[0].charAt(sp.length - 1);
            int row = sp[1].charAt(0);
            int col = sp[2].charAt(0);

            String output = String.valueOf(KeyMap[index][row][col][0]);

            for(int i = 1; i < KeyMap[index][row][col].length; i++)
                output = output + " " + String.valueOf(KeyMap[index][row][col][i]);


            Toast.makeText(MainActivity.this, output, Toast.LENGTH_LONG).show();
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SocketThread implements Runnable {
        private String parameter;
        public SocketThread(String parameter){
            this.parameter = parameter;
        }

        public void run(){
            try {
                outputStream.writeUTF(parameter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(9000);

                socket = serverSocket.accept();
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}