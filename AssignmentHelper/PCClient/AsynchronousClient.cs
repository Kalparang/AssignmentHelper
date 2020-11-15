using PCClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Net;
using System.Net.Sockets;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using System.Windows.Threading;

// State object for receiving data from remote device.  
public class StateObject
{
    // Client socket.  
    public Socket workSocket = null;
    // Size of receive buffer.  
    public const int BufferSize = 256;
    // Receive buffer.  
    public byte[] buffer = new byte[BufferSize];
    // Received data string.  
    public StringBuilder sb = new StringBuilder();
}

public static class AsynchronousClient
{
    // The port number for the remote device.  
    private const int port = 8000;

    // ManualResetEvent instances signal completion.  
    private static ManualResetEvent connectDone =
        new ManualResetEvent(false);
    private static ManualResetEvent sendDone =
        new ManualResetEvent(false);
    private static ManualResetEvent receiveDone =
        new ManualResetEvent(false);

    // The response from the remote device.  
    private static String response = String.Empty;

    private static List<byte> Savebyte = new List<byte>();

    public static Thread socketThread;
    static Socket client;

    static MainWindow mw;

    public static bool IsExit = false;

    public static void StartClient(MainWindow mainwindow)
    {
        mw = mainwindow;
        socketThread = new Thread(new ThreadStart(SocketThread));
        socketThread.Start();
    }

    public static void SocketThread()
    {
        // Connect to a remote device.  
        try
        {
            System.Windows.Application.Current.Dispatcher.BeginInvoke(new Action(() => mw.Label_Status.Content = "연결 대기 중")).Wait();

            while (true)
            {
                if (InitSocket())
                {
                    System.Windows.Application.Current.Dispatcher.BeginInvoke(new Action(() => mw.Label_Status.Content = "연결되었습니다.")).Wait();

                    // Receive the response from the remote device.  
                    Receive(client);
                    receiveDone.WaitOne();
                }

                // Release the socket.  
                client.Shutdown(SocketShutdown.Both);
                client.Close();

                connectDone.Reset();
                receiveDone.Reset();

                System.Windows.Application.Current.Dispatcher.BeginInvoke(new Action(() => mw.Label_Status.Content = "연결이 끊어졌습니다. 재연결 중")).Wait();

                Thread.Sleep(1000);
            }
        }
        catch(ThreadAbortException tae)
        {

        }
        catch (Exception e)
        {
            MessageBox.Show("SocketThread Error\n" + e.ToString());
            Environment.Exit(0);
        }
    }

    public static bool InitSocket()
    {
        bool result = false;

        try
        {
            // Establish the remote endpoint for the socket.  
            IPAddress ipAddress = IPAddress.Loopback;
            IPEndPoint remoteEP = new IPEndPoint(ipAddress, port);

            // Create a TCP/IP socket.  
            client = new Socket(ipAddress.AddressFamily,
                SocketType.Stream, ProtocolType.Tcp);

            // Connect to the remote endpoint.  
            client.BeginConnect(remoteEP,
                new AsyncCallback(ConnectCallback), client);
            connectDone.WaitOne();

            result = VerifyConnect(client);
        }
        catch (Exception e)
        {
            MessageBox.Show("InitSocket Error\n" + e.ToString());
            Environment.Exit(0);
        }

        return result;
    }

    private static bool VerifyConnect(Socket client)
    {
        string ConnectCheck = "AssignmentHelperApplication";
        byte[] Connectbyte = new byte[Encoding.UTF8.GetBytes(ConnectCheck).Length];

        client.ReceiveTimeout = 2000;
        client.SendTimeout = 2000;

        try
        {
            client.Receive(Connectbyte, Connectbyte.Length, SocketFlags.None);

            if (Connectbyte[0] == 0)
            {
                return false;
            }

            if (ConnectCheck != Encoding.UTF8.GetString(Connectbyte))
            {
                return false;
            }

            ConnectCheck = "AssistKeyPad";
            Connectbyte = Encoding.UTF8.GetBytes(ConnectCheck);

            client.Send(Connectbyte, Connectbyte.Length, SocketFlags.None);
        }
        catch(SocketException se)
        {
            return false;
        }

        client.ReceiveTimeout = 0;
        client.SendTimeout = 0;

        Thread.Sleep(500);

        return true;
    }

    private static void ConnectCallback(IAsyncResult ar)
    {
        try
        {
            // Retrieve the socket from the state object.  
            Socket client = (Socket)ar.AsyncState;

            // Complete the connection.  
            client.EndConnect(ar);

            Console.WriteLine("Socket connected to {0}",
                client.RemoteEndPoint.ToString());

            // Signal that the connection has been made.  
            connectDone.Set();
        }
        catch (Exception e)
        {
            MessageBox.Show(null,"ConnectCallback Error\n" + e.ToString(), "Error", MessageBoxButtons.OK);
            Environment.Exit(0);
        }
    }

    private static void DisconnectCallback(IAsyncResult ar)
    {
        if (IsExit)
            return;

        socketThread.Start();
    }

    private static void Receive(Socket client)
    {
        try
        {
            // Create the state object.  
            StateObject state = new StateObject();
            state.workSocket = client;

            // Begin receiving the data from the remote device.  
            client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                new AsyncCallback(ReceiveCallback), state);
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }

    private static void ReceiveCallback(IAsyncResult ar)
    {
        try
        {
            // Retrieve the state object and the client socket
            // from the asynchronous state object.  
            StateObject state = (StateObject)ar.AsyncState;
            Socket client = state.workSocket;
            
            // Read data from the remote device.  
            int bytesRead = client.EndReceive(ar);

            if (bytesRead > 0)
            {
                if(bytesRead % 7 != 0)
                {
                    if (bytesRead + Savebyte.Count % 7 != 0)
                    {
                        for (int i = 0; i < bytesRead; i++)
                            Savebyte.Add(state.buffer[i]);

                        client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                            new AsyncCallback(ReceiveCallback), state);

                        return;
                    }

                    Savebyte.AddRange(state.buffer);
                    state.buffer = new byte[Savebyte.Count];
                    state.buffer = Savebyte.ToArray();
                    bytesRead = state.buffer.Length * 7;
                }
                else
                {
                    KeyboardInput ki = new KeyboardInput();

                    //Console.WriteLine(Encoding.UTF8.GetString(state.buffer, 0, bytesRead));
                    bool IsShiftDown = state.buffer[0] == 1 ? true : false;
                    bool IsCtrlDown = state.buffer[1] == 1 ? true : false;
                    bool IsAltDown = state.buffer[2] == 1 ? true : false;

                    int KeyCode = BitConverter.ToInt32(state.buffer, 3);
                    
                    ki.ReadyKey(true);
                    ki.Presskey(KeyCode, IsShiftDown, IsCtrlDown);

                    for (int i = 1; i < (int)(bytesRead / 7); i++)
                    {
                        // There might be more data, so store the data received so far.  
                        //state.sb.Append(Encoding.UTF8.GetString(state.buffer, 0, bytesRead));
                        //Console.WriteLine(Encoding.UTF8.GetString(state.buffer, 0, bytesRead));
                        IsShiftDown = state.buffer[i * 7] == 1 ? true : false;
                        IsCtrlDown = state.buffer[i * 7 + 1] == 1 ? true : false;
                        IsAltDown = state.buffer[i * 7 + 2] == 1 ? true : false;

                        KeyCode = BitConverter.ToInt32(state.buffer, i * 7 + 3);

                        ki.Presskey(KeyCode, IsShiftDown);
                    }
                    ki.ReadyKey(false);
                }
                
                state.buffer = new byte[StateObject.BufferSize];

                // Get the rest of the data.  
                client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                    new AsyncCallback(ReceiveCallback), state);
            }
            else
            {
                // All the data has arrived; put it in response.  
                if (state.sb.Length > 1)
                {
                    response = state.sb.ToString();
                }
                // Signal that all bytes have been received.  
                receiveDone.Set();
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }

    private static void Send(Socket client, String data)
    {
        // Convert the string data to byte data using ASCII encoding.  
        byte[] byteData = Encoding.ASCII.GetBytes(data);

        // Begin sending the data to the remote device.  
        client.BeginSend(byteData, 0, byteData.Length, 0,
            new AsyncCallback(SendCallback), client);
    }

    private static void SendCallback(IAsyncResult ar)
    {
        try
        {
            // Retrieve the socket from the state object.  
            Socket client = (Socket)ar.AsyncState;

            // Complete sending the data to the remote device.  
            int bytesSent = client.EndSend(ar);
            Console.WriteLine("Sent {0} bytes to server.", bytesSent);

            // Signal that all bytes have been sent.  
            sendDone.Set();
        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }
    }
}
