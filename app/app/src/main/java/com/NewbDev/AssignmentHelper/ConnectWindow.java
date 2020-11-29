package com.NewbDev.AssignmentHelper;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class ConnectWindow {
    private static TextView ConnStat;

    public static ServerSocket serverSocket;
    public static Socket socket = null;
    public static DataInputStream inputStream;
    public static DataOutputStream outputStream;

    private ServerThread serverThread;
    private Thread socketThread;

    public static boolean IsConnected = false;

    public static byte[] toByteArray (Object obj)
    {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            bytes = bos.toByteArray();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    public void StartServer()
    {
        serverThread = new ServerThread();
        serverThread.start();
    }

    public void StopServer()
    {
        try {
            if(socket != null) socket.close();
            if(serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler();

    private void disconnected()
    {
        IsConnected = false;
        FirstFragment.ButtonClickable(false);

        try {
            socket.close();
            serverSocket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        handler.post(new Runnable() {
            public void run() {
                ConnStat.setText("윈도우와 연결이 끊어졌습니다.\n연결 시도 중...");
                ConnStat.setVisibility(View.VISIBLE);
            }
        });
        StartServer();
    }

    public void SendData(KeyCodeStruct msg[])
    {
        new Thread(new SocketThread(msg)).start();
    }

    public void setConnStat(TextView ConnStat){
        this.ConnStat = ConnStat;
        this.ConnStat.setVisibility(View.VISIBLE);
    }

    class SocketThread implements Runnable {
        private KeyCodeStruct parameter[];
        public SocketThread(KeyCodeStruct parameter[]){
            this.parameter = parameter;
        }

        public void run(){
            synchronized (this) {
                if (IsConnected) {
                    try {
                        for(int i = 0; i < parameter.length; i++) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(7);
                            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

                            byte byteCode[] = new byte[4];
                            byteCode[0] = (byte)(parameter[i].KeyCode);
                            byteCode[1] = (byte)(parameter[i].KeyCode >> 8);
                            byteCode[2] = (byte)(parameter[i].KeyCode >> 16);
                            byteCode[3] = (byte)(parameter[i].KeyCode >> 24);

                            byteBuffer.put(parameter[i].IsShiftDown ? (byte)1 : (byte)0);
                            byteBuffer.put(parameter[i].IsCtrlDown ? (byte)1 : (byte)0);
                            byteBuffer.put(parameter[i].IsAltDown ? (byte)1 : (byte)0);
                            byteBuffer.put(byteCode);

                            outputStream.write(byteBuffer.array());
                        }
                        outputStream.flush();
                    } catch (SocketException se) {
                        disconnected();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class ServerThread extends Thread {
        private boolean VerifyConnect(DataInputStream inputStream, DataOutputStream outputStream){
            try {
                String ConnectCheck = "AssignmentHelperApplication";
                byte Connectbyte[] = ConnectCheck.getBytes("utf-8");
                ByteBuffer byteBuffer = ByteBuffer.allocate(Connectbyte.length);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                byteBuffer.put(Connectbyte);

                outputStream.write(byteBuffer.array(), 0, byteBuffer.array().length);
                outputStream.flush();

                Thread.sleep(500);

                ConnectCheck = "AssistKeyPad";
                Connectbyte = new byte[ConnectCheck.getBytes("utf-8").length];

                inputStream.read(Connectbyte, 0, Connectbyte.length);
                byteBuffer.clear();
                byteBuffer = ByteBuffer.allocate(Connectbyte.length);
                byteBuffer.put(Connectbyte);
                byteBuffer.order(ByteOrder.BIG_ENDIAN);

                String inputString = new String(byteBuffer.array(), StandardCharsets.UTF_8);

                if (ConnectCheck.equals(inputString)) {
                    Thread.sleep(500);
                    return true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public void run() {
            try {
                while(true)
                {
                    serverSocket = new ServerSocket(9000);

                    socket = serverSocket.accept();

                    socket.setReceiveBufferSize(256);
                    socket.setSendBufferSize(256);

                    inputStream = new DataInputStream(socket.getInputStream());
                    outputStream = new DataOutputStream(socket.getOutputStream());

                    socket.setSoTimeout(2000);

                    if(VerifyConnect(inputStream, outputStream))
                        break;

                    serverSocket.close();
                    socket.close();
                    Thread.sleep(1000);
                    continue;
                }

                socket.setSoTimeout(0);

                IsConnected = true;

                ConnStat.setVisibility(View.INVISIBLE);
                FirstFragment.ButtonClickable(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
