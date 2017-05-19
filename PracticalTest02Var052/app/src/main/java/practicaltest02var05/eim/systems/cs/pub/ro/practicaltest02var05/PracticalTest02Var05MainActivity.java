package practicaltest02var05.eim.systems.cs.pub.ro.practicaltest02var05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var05MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText getOperation = null;
    private EditText putOperation = null;
    private Button sendOperationButton = null;

    private TextView operationResult = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (serverThread != null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server is already running!", Toast.LENGTH_SHORT).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private ExecuteOperation executeOperation = new ExecuteOperation();
    private class ExecuteOperation implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            String getEditText = getOperation.getText().toString();
            String putEditText = putOperation.getText().toString();

            if ((getEditText == null || getEditText.isEmpty())
                    && (putEditText == null || putEditText.isEmpty())) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (put/get information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            String operationType = null;
            String key = null;
            String value = null;
            if (getEditText != null && !getEditText.isEmpty()) {
                operationType = "GET";
                key = getEditText;
                value = "";
            } else {
                operationType = "PUT";

                String values[] = putEditText.split(",");
                Log.e(Constants.TAG, values.toString());
                key = values[0];
                value = values[1];
            }

            operationResult.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), operationType, key, value, operationResult
            );
            clientThread.start();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_var05_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_server_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.client_address);
        clientPortEditText = (EditText)findViewById(R.id.client_port);
        getOperation = (EditText)findViewById(R.id.get_editText);
        putOperation = (EditText)findViewById(R.id.put_editText);
        sendOperationButton = (Button)findViewById(R.id.send_operation);
        sendOperationButton.setOnClickListener(executeOperation);

        operationResult = (TextView)findViewById(R.id.operation_result);
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
