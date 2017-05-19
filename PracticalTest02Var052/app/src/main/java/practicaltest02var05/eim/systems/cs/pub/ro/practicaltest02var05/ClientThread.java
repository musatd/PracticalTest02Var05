package practicaltest02var05.eim.systems.cs.pub.ro.practicaltest02var05;


import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String operationType;
    private String key;
    private String value;
    private TextView sendOperationButton;

    private Socket socket;

    public ClientThread(String address, int port, String operationType, String key, String value, TextView sendOperationButton) {
        this.address = address;
        this.port = port;
        this.operationType = operationType;
        this.key = key;
        this.value = value;
        this.sendOperationButton = sendOperationButton;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            printWriter.println(operationType);
            printWriter.flush();
            printWriter.println(key);
            printWriter.flush();
            printWriter.println(value);
            printWriter.flush();
            String operationResult;
            while ((operationResult = bufferedReader.readLine()) != null) {
                final String finalizedOperationResult = operationResult;
                sendOperationButton.post(new Runnable() {
                    @Override
                    public void run() {
                        sendOperationButton.setText(finalizedOperationResult);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
