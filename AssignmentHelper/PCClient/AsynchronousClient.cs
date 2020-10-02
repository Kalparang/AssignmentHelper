using System;
using System.Data;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Windows.Forms;

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

    static Thread socketThread;
    static Socket client;

    public static bool IsExit = false;

    public static void StartClient()
    {
        socketThread = new Thread(new ThreadStart(SocketThread));
        socketThread.Start();
    }

    public static void SocketThread()
    {
        // Connect to a remote device.  
        try
        {
            InitSocket();

            // Receive the response from the remote device.  
            Receive(client);
            receiveDone.WaitOne();

            Console.WriteLine("End Receive");

            // Write the response to the console.  
            //Console.WriteLine("Response received : {0}", response);

            // Release the socket.  
            //client.Shutdown(SocketShutdown.Both);
            //client.Close();

        }
        catch (Exception e)
        {
            MessageBox.Show("SocketThread Error\n" + e.ToString());
            Application.Exit();
        }
    }

    public static void InitSocket()
    {
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

            client.BeginDisconnect(true, new AsyncCallback(DisconnectCallback), client);
        }
        catch (Exception e)
        {
            MessageBox.Show("InitSocket Error\n" + e.ToString());
            Application.Exit();
        }
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

            string ConnectCheck = "AssignmentHelperApplication";
            byte[] Connectbyte = new byte[Encoding.UTF8.GetBytes(ConnectCheck).Length];
            
            client.Receive(Connectbyte, Connectbyte.Length, SocketFlags.None);

            if(Connectbyte[0] == 0)
            {
                Thread.Sleep(1000);

                socketThread.Abort();

                socketThread = new Thread(new ThreadStart(SocketThread));
                socketThread.Start();

                return;
            }

            if (ConnectCheck != Encoding.UTF8.GetString(Connectbyte))
            {
                connectDone.Reset();
                client.Shutdown(SocketShutdown.Both);
                client.Close();

                Thread.Sleep(500);

                socketThread.Abort();

                socketThread = new Thread(new ThreadStart(SocketThread));
                socketThread.Start();

                return;
            }

            ConnectCheck = "AssistKeyPad";
            Connectbyte = Encoding.UTF8.GetBytes(ConnectCheck);

            client.Send(Connectbyte, Connectbyte.Length, SocketFlags.None);

            Thread.Sleep(500);

            // Signal that the connection has been made.  
            connectDone.Set();
        }
        catch (Exception e)
        {
            MessageBox.Show("ConnectCallback Error\n" + e.ToString());
            Application.Exit();
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
                // There might be more data, so store the data received so far.  
                state.sb.Append(Encoding.UTF8.GetString(state.buffer, 0, bytesRead));
                Console.WriteLine(Encoding.UTF8.GetString(state.buffer, 0, bytesRead));
                //bool IsShiftDown = state.buffer[0] == 1 ? true : false;
                //bool IsCtrlDown = state.buffer[1] == 1 ? true : false;
                //bool IsAltDown = state.buffer[2] == 1 ? true : false ;

                //int KeyCode = state.buffer[3];

                //Keys k = (Keys)KeyCode;
                //Console.WriteLine(k.ToString());

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
