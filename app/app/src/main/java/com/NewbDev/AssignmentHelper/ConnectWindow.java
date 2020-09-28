package com.NewbDev.AssignmentHelper;

import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectWindow {
    private static TextView ConnStat;

    public static ServerSocket serverSocket;
    public static Socket socket = null;
    public static DataInputStream inputStream;
    public static DataOutputStream outputStream;

    private ServerThread serverThread;
    private Thread socketThread;

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

    public void SendData(String msg)
    {
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

                //socket = new Socket("127.0.0.1", 9000);

                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
