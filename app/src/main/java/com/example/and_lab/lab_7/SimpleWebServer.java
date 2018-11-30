package com.example.and_lab.lab_7;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Camera;
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
    private String mCurrentPhotoPath;
    private boolean isCameraUsed = false;
    private boolean isCameraUsed2 = false;
    private boolean isCameraUsed3 = false;
    static final int REQUEST_TAKE_PHOTO = 1;


    public SimpleWebServer(int port, AssetManager assets, Context context) {
        mPort = port;
        mAssets = assets;
        this.context = context;
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
            int i = 0;
            while (mIsRunning) {
                if(!isCameraUsed && !isCameraUsed2 && !isCameraUsed3) {
                    if(!isCameraUsebyApp()) {
                        Socket socket = mServerSocket.accept();
                        i = 0;
                        while (i < 100000000) {
                            i++;
                        }
                        if (!isCameraUsed) {
                            isCameraUsed = true;
                            if (!isCameraUsed2) {
                                if (!isCameraUsed3) {
                                    isCameraUsed3 = true;
                                    isCameraUsed2 = !isCameraUsed2;
                                }
                                if (isCameraUsed2)
                                    handle(socket);
                            }
                        }
                        socket.close();
                    }
                    isCameraUsed = false;
                    isCameraUsed2 = false;
                    isCameraUsed3 = false;
                    i = 0;
                    while (i < 100000000) {
                        i++;
                    }
                }
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

        if(socket != null) {
            dispatchTakePictureIntent();
            galleryAddPic();
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("photo", "Error occurred while creating the File");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.example.android.fileprovider",
                        photoFile);
                Log.i("filePath", "Uri: " + photoURI.toString() + "\n");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                ((Activity) context).startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        Log.i("filePath", "image path: " + image.getAbsolutePath() + "\n");
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public boolean isCameraUsebyApp() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            return true;
        } finally {
            if (camera != null) camera.release();
        }
        return false;
    }
}
