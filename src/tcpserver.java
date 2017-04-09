/**
 * Created by Murat Demirtaş on 9.04.2017.
 * Simple java tcp server example
 */

import java.io.*;
import java.net.*;

public class tcpserver
{
    private static final int PORT_NUMBER = 5555;


    public enum serverStatus {
        SERVER_IDLE,
        SERVER_PORT_BUSY,
        SERVER_STARTED,
        SERVER_UNKNOW_ERROR,
        SERVER_HAS_NEW_CLIENT,
        SERVER_GOING_DOWN
    }

    public static void main(String argv[]) throws Exception
    {

        String receivedData;
        serverStatus m_serverStatus;
        String responseData = "SYSTEM VERSION IS 1.0";
        int checksum = 0;
        ServerSocket tcpserver = new ServerSocket(PORT_NUMBER);
        m_serverStatus = serverStatus.SERVER_IDLE;

        if(tcpserver.isBound())
            System.out.println("SERVER IS CREATED ON PORT:"+ PORT_NUMBER + "\n");

        else {
            System.out.println("SERVER CANT BIND ON PORT:"+ PORT_NUMBER + "ANOTHER SERVICE IS RUNNING ON " +
                    "THIS PORT?..\n");
            m_serverStatus = serverStatus.SERVER_PORT_BUSY;
            System.exit(1);
        }

        m_serverStatus = serverStatus.SERVER_STARTED;

        while(true)
        {
            Socket tcpsocket = tcpserver.accept();
            m_serverStatus = serverStatus.SERVER_HAS_NEW_CLIENT;
            System.out.println("New Connection, IP Adress Is : " + tcpsocket.getInetAddress());
            BufferedReader readChannelForTCPClient =
                    new BufferedReader(new InputStreamReader(tcpsocket.getInputStream()));

            m_serverStatus = serverStatus.SERVER_HAS_NEW_CLIENT;
            DataOutputStream sendChannelForTCPClient = new DataOutputStream(tcpsocket.getOutputStream());
            receivedData = readChannelForTCPClient.readLine();

            System.out.println("Received From TCP Client: " + receivedData);
            int received_data_checksum = checkChecksum(receivedData);
            int response_data_checksum = checkChecksum(responseData);

            sendChannelForTCPClient.writeBytes("HELLO MY FRIEND, I RECEIVED YOUR DATA\n");
            sendChannelForTCPClient.writeBytes("YOUR DATA CHECKSUM IS :"+ received_data_checksum + "\n");
            sendChannelForTCPClient.writeBytes("I'M SENDIND TO MY DATA WITH CHECKSUM : "+ response_data_checksum + "\n");
            sendChannelForTCPClient.writeBytes("BYE BYE MY FRIEND.\n");

            Thread.sleep(2000);
            tcpsocket.close();
            m_serverStatus = serverStatus.SERVER_GOING_DOWN;
            System.exit(0);
        }
    }

    public static int checkChecksum(String data){
        int sum_of_recv_byts = 0;

        for(int i =0; i < data.length(); i++)
            sum_of_recv_byts = sum_of_recv_byts + (int)data.charAt(i);

        return (sum_of_recv_byts % 256);

    }

}

