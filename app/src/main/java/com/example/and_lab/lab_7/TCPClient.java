package com.example.and_lab.lab_7;
import android.util.Log;

import java.io.*;
import java.net.*;

class TCPClient implements Runnable {
    private int port_number;
    private boolean isRunning = false;
    private DataOutputStream outToServer;
    public TCPClient(int port_number){
        this.port_number = port_number;
    }

    public void start() {
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void run() {

        try {
            Socket clientSocket = null;
            clientSocket = new Socket("10.0.2.2", port_number);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            Log.e("clientSocket", "connection broke");
        }

    }
    public void sendMessage(String message) throws IOException {
        outToServer.write(message.getBytes());
        outToServer.flush();
    }
}