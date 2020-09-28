package com.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;

public class PCClient {

	private static Socket socket;
	public static DataInputStream dis;
	public static DataOutputStream dos;
	public static Robot robot;
	
    public static void main(String[] args) throws IOException {
        System.out.println("Toast");
        Scanner scanner = new Scanner(System.in);
		socket = new Socket("127.0.0.1", 8000);
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		try
		{
		robot = new Robot();
		}
		catch(Exception e)
		{}
			
        while (true) {
            //String msg = scanner.next();
			
            sendToast(dis.readUTF());
        }
    }

    public static void sendToast(String msg) {
		System.out.println(msg);
		try
		{
		Thread.sleep(1000);
		}
		catch(Exception e)
		{}
	
		String KeyList[] = msg.split(",");
		
		for(int i = 0; i < KeyList.length; i++)
		{
			robot.keyPress(Integer.parseInt(KeyList[i]));
			robot.keyRelease(Integer.parseInt(KeyList[i]));
		}
    }
}