package com.NewbDev.AssignmentHelper;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static android.app.PendingIntent.getActivity;

public class ConnectWindow {
    private static TextView ConnStat;

    public static ServerSocket serverSocket;
    public static Socket socket = null;
    public static DataInputStream inputStream;
    public static DataOutputStream outputStream;

    private ServerThread serverThread;
    private Thread socketThread;

    private static boolean IsConnected = false;

    public void StartServer()
    {
        serverThread = new ServerThread();
        serverThread.start();
    }

    public void StopServer()
    {
        try {
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler();

    private void disconnected()
    {
        IsConnected = false;

        try {
            socket.close();
            serverSocket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        FirstFragment.ButtonClickable(false);

        handler.post(new Runnable() {
            public void run() {
                ConnStat.setText("윈도우와 연결이 끊어졌습니다.\n연결 시도 중...");
                ConnStat.setVisibility(View.VISIBLE);
            }
        });
        StartServer();
    }

    public void SendData(String msg)
    {
        if(msg.charAt(0) == '0')
            return;
        new Thread(new SocketThread(msg)).start();
    }

    public void setConnStat(TextView ConnStat){
        this.ConnStat = ConnStat;
    }

    class SocketThread implements Runnable {
        private String parameter;
        public SocketThread(String parameter){
            this.parameter = parameter;
        }

        public void run(){
            if(IsConnected) {
                try {
                    outputStream.writeUTF(parameter);
                    outputStream.flush();
                } catch (SocketException se) {
                    disconnected();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(9000);
                socket = serverSocket.accept();
                //socket = new Socket("127.0.0.1", 9000);

                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());

                IsConnected = true;

                ConnStat.setVisibility(View.INVISIBLE);
                FirstFragment.ButtonClickable(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
