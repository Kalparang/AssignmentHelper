package com.NewbDev;

import java.io.*;
import java.net.*;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.io.EOFException;

public class PCClient {

	private static Socket socket;
	//public static ServerSocket serverSocket;
	public static DataInputStream dis;
	public static DataOutputStream dos;
	public static Robot robot;
	public static Boolean EOFMsg = false;
	
	private static boolean startConnect()
	{
		try
		{
			socket = new Socket("127.0.0.1", 8000);
			System.out.println("connect");
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		}
		catch(ConnectException ce){
			System.out.println("Error - can't connect server");
			ce.printStackTrace();
			System.out.println("Check ADB forward");
			
			return false;
		}
		catch(Exception e)
		{
			System.out.println("Error - can't connect server");
			e.printStackTrace();
			
			return false;
		}
		
		return true;
	}
	

    public static void main(String[] args) throws IOException {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try{
					Process p = Runtime.getRuntime().exec("adb forward --remove tcp:8000");
					p.waitFor();
				} catch(Exception e) { e.printStackTrace(); }
				try { socket.close(); } catch(Exception e) {}
			}
		});
		Process p = Runtime.getRuntime().exec("adb forward tcp:8000 tcp:9000");
		try{
		p.waitFor();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		if(p.exitValue() != 0)
		{
			System.out.println("Fail - ADB Forwarding");
			System.out.println("Check Android is Debugging mode");
			System.out.println("and connect only one Debugging device");
			
			return;
		}
				
        System.out.println("ready");
		Scanner scanner = new Scanner(System.in);

		if(!startConnect())
		{
			System.out.println("Fail to connect server");
			
			return;
		}
		
		try
		{
		robot = new Robot();
		}
		catch(Exception e)
		{
			System.out.println("Init Robot class fail");
			e.printStackTrace();
		}
			
        while (true) {
			try
			{
				sendToast(dis.readUTF());
			}
			catch(EOFException eof)
			{
				if(!EOFMsg) {
					EOFMsg = true;
					System.out.println("Error - Socket EOF");
					System.out.println("Please run Android app");
					System.out.println("try reconnect");
				}
				
				try { Thread.sleep(1000); } catch(Exception te) {}
		
				if(!startConnect())
				{
					System.out.println("Error - can't reconnect server");
					break;
				}
			}
			catch(Exception e)
			{
				System.out.println("Error - socket");
				e.printStackTrace();
			}
        }
    }

    public static void sendToast(String msg) {
		System.out.println(msg);
	
		String KeyList[] = msg.split(",");
		
		for(int i = 0; i < KeyList.length; i++)
		{
			int targetKey = Integer.parseInt(KeyList[i]);
			
			switch(targetKey){
				case 0x6C: // Num Enter
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
				break;
				case 0xbd: // -
					robot.keyPress(KeyEvent.VK_SUBTRACT);
					robot.keyRelease(KeyEvent.VK_SUBTRACT);
				break;
				case 0xdb:// {
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_OPEN_BRACKET);
					robot.keyRelease(KeyEvent.VK_OPEN_BRACKET);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				break;
				case 0xdd:// }
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_CLOSE_BRACKET);
					robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				break;
				case 0x39:// (
				case 0x30:// )
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(targetKey);
					robot.keyRelease(targetKey);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				break;
				default:
					robot.keyPress(targetKey);
					robot.keyRelease(targetKey);
				break;
			}
		}
    }
}