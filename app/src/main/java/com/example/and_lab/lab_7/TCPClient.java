package com.example.and_lab.lab_7;
import java.io.*;
import java.net.*;

class TCPClient {
    private int port_number;

    public TCPClient(int port_number){
        this.port_number = port_number;
    }

    public void Start() throws IOException {
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

       Socket clientSocket = new Socket("10.0.2.2", port_number);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sentence = inFromUser.readLine();
        outToServer.writeBytes(sentence + 'n');
        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);
        clientSocket.close();

    }

    public static void main(String argv[]) throws Exception {
        }
}