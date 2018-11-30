package com.example.and_lab.lab_7;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.and_lab.lab_7.MainActivity.galleryAddPic;

public class SimpleWebServer implements Runnable{
    private static final String TAG = "SimpleWebServer";

    /**
     * The port number we listen to
     */
    private final int mPort;

    /**
     * {@link android.content.res.AssetManager} for loading files to serve.
     */
    private final AssetManager mAssets;

    /**
     * True if the server is running.
     */
    private boolean mIsRunning;

    /**
     * The {@link java.net.ServerSocket} that we listen to.
     */
    private ServerSocket mServerSocket;

    private Context context;
    private boolean cameraUsed = false;
    private boolean pictureTaken = false;

    public SimpleWebServer(int port, AssetManager assets, Context context) {
        mPort = port;
        mAssets = assets;
        this.context = context;
    }

    public boolean isCameraUsed(){
        return cameraUsed;
    }
    public boolean isPictureTaken(){
        return pictureTaken;
    }
    public void setPictureTaken(boolean pictureTaken){
        this.pictureTaken = pictureTaken;
    }

    /**
     * This method starts the web server listening to the specified port.
     */
    public void start() {
        mIsRunning = true;
        new Thread(this).start();
    }

    /**
     * This method stops the web server
     */
    public void stop() {
        try {
            mIsRunning = false;
            if (null != mServerSocket) {
                mServerSocket.close();
                mServerSocket = null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error closing the server socket.", e);
        }
    }

    public int getPort() {
        return mPort;
    }

    @Override
    public void run() {
        try {
            mServerSocket = new ServerSocket(mPort);
            while (mIsRunning) {
                Socket socket = mServerSocket.accept();
                handle(socket);
                socket.close();
            }
        } catch (SocketException e) {
            // The server was stopped; ignore.
        } catch (IOException e) {
            Log.e(TAG, "Web server error.", e);
        }
    }

    /**
     * Respond to a request from a client.
     *
     * @param socket The client socket.
     * @throws IOException
     */
    private void handle(Socket socket) throws IOException {

        if(socket != null && !cameraUsed) {
            cameraUsed = true;
            ((MainActivity)context).dispatchTakePictureIntent(context);
            ((MainActivity)context).galleryAddPic(context);
            pictureTaken = true;
//            cameraUsed = false;
        }

//        BufferedReader reader = null;
//        PrintStream output = null;
//        try {
//            String route = null;
//
//            // Read HTTP headers and parse out the route.
//            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String line;
//            while (!TextUtils.isEmpty(line = reader.readLine())) {
//                if (line.startsWith("GET /")) {
//                    int start = line.indexOf('/') + 1;
//                    int end = line.indexOf(' ', start);
//                    route = line.substring(start, end);
//                    break;
//                }
//            }
//
//            // Output stream that we send the response to
//            output = new PrintStream(socket.getOutputStream());
//
//            // Prepare the content to send.
//            if (null == route) {
//                writeServerError(output);
//                return;
//            }
//            byte[] bytes = loadContent(route);
//            if (null == bytes) {
//                writeServerError(output);
//                return;
//            }
//
//            // Send out the content.
//            output.println("HTTP/1.0 200 OK");
//            output.println("Content-Type: " + detectMimeType(route));
//            output.println("Content-Length: " + bytes.length);
//            output.println();
//            output.write(bytes);
//            output.flush();
//        } finally {
//            if (null != output) {
//                output.close();
//            }
//            if (null != reader) {
//                reader.close();
//            }
//        }
    }

    /**
     * Writes a server error response (HTTP/1.0 500) to the given output stream.
     *
     * @param output The output stream.
     */
    private void writeServerError(PrintStream output) {
        output.println("HTTP/1.0 500 Internal Server Error");
        output.flush();
    }

    /**
     * Loads all the content of {@code fileName}.
     *
     * @param fileName The name of the file.
     * @return The content of the file.
     * @throws IOException
     */
    private byte[] loadContent(String fileName) throws IOException {
        InputStream input = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            input = mAssets.open(fileName);
            byte[] buffer = new byte[1024];
            int size;
            while (-1 != (size = input.read(buffer))) {
                output.write(buffer, 0, size);
            }
            output.flush();
            return output.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (null != input) {
                input.close();
            }
        }
    }

    /**
     * Detects the MIME type from the {@code fileName}.
     *
     * @param fileName The name of the file.
     * @return A MIME type.
     */
    private String detectMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        } else if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else {
            return "application/octet-stream";
        }
    }


}
