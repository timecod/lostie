package com.example.lostie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class BluetoothActivity extends AppCompatActivity {
    private static final String TAG = "bluetooth";

    Button search_button, connect_button, repeat_search_button;
    TextView textView;
    Handler h;

    private static final int REQUEST_ENABLE_BT = 1;
    final int RECIEVE_MESSAGE = 1;        // Статус для Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    private boolean repeat = false, isConnected = false;
    private Timer timer;

    private ConnectedThread mConnectedThread;

    // SPP UUID сервиса
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-адрес Bluetooth модуля
    private static String address = "98:D3:51:F9:6D:7C";

    /** Called when the activity is first created. */
    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bluetooth);

        search_button = (Button) findViewById(R.id.buttonSearch);
        connect_button = (Button) findViewById(R.id.buttonConnect);
        textView = (TextView) findViewById(R.id.textView2);      // для вывода текста, полученного от Arduino
        repeat_search_button = (Button) findViewById(R.id.buttonSearchRepeat);
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                   // если приняли сообщение в Handler
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);                                                // формируем строку
                        int endOfLineIndex = sb.indexOf("\r");
                        if (endOfLineIndex > 0 ) {                                            // если встречаем конец строки,
                            String sbprint = sb.substring(1, endOfLineIndex);// то извлекаем строку
                            Log.d(TAG, sbprint);
                            sb.delete(0, sb.length());
                            if (sbprint.charAt(0) == 'U')
                                textView.setText(sbprint);             // обновляем TextView

                        }
                        Log.d(TAG, "...Строка:"+ sb.toString() +  "Байт:" + msg.arg1 + "...");
                        break;
                }
            }

            ;
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // получаем локальный Bluetooth адаптер
        checkBTState();

        search_button.setOnClickListener(new OnClickListener() {        // определяем обработчик при нажатии на кнопку
            public void onClick(View v) {
                if (!isConnected) {
                    Toast.makeText(getBaseContext(), "Устройство не подключено", Toast.LENGTH_SHORT).show();
                    return;
                }
                //btnOn.setEnabled(false);
                mConnectedThread.write("U");    // Отправляем через Bluetooth цифру 1

            }
        });

        repeat_search_button.setOnClickListener(new OnClickListener() {        // определяем обработчик при нажатии на кнопку
            public void onClick(View v) {
                if (!isConnected) {
                    Toast.makeText(getBaseContext(), "Устройство не подключено", Toast.LENGTH_SHORT).show();
                    repeat = false;
                    return;
                }
                repeat = !repeat;
                if (repeat)
                    Toast.makeText(getBaseContext(), "Повторная отправка включена", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), "Повторная отправка выключена", Toast.LENGTH_SHORT).show();


            }
        });

        connect_button.setOnClickListener(new OnClickListener() {        // определяем обработчик при нажатии на кнопку
            public void onClick(View v) {


                Log.d(TAG, "...onResume - попытка соединения...");

                // Set up a pointer to the remote node using it's address.
                BluetoothDevice device = btAdapter.getRemoteDevice(address);

                // Two things are needed to make a connection:
                //   A MAC address, which we got above.
                //   A Service ID or UUID.  In this case we are using the
                //     UUID for SPP.
                try {
                    if (ActivityCompat.checkSelfPermission(BluetoothActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e) {
                    errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
                }

                // Discovery is resource intensive.  Make sure it isn't going on
                // when you attempt to connect and pass your message.
                btAdapter.cancelDiscovery();

                // Establish the connection.  This will block until it connects.
                Log.d(TAG, "...Соединяемся...");
                try {
                    btSocket.connect();
                    isConnected = true;
                    Log.d(TAG, "...Соединение установлено и готово к передачи данных...");
                    Toast.makeText(getBaseContext(), "Подключено", Toast.LENGTH_SHORT).show();
                    connect_button.setEnabled(false);
                } catch (IOException e) {
                    try {
                        btSocket.close();
                        isConnected = false;
                        Toast.makeText(getBaseContext(), "Не удалось подключиться", Toast.LENGTH_SHORT).show();
                    } catch (IOException e2) {
                        errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                    }
                }

                // Create a data stream so we can talk to server.
                Log.d(TAG, "...Создание Socket...");

                mConnectedThread = new ConnectedThread(btSocket);
                mConnectedThread.start();

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (repeat ){
                    mConnectedThread.write("U");
                }

            }
        }, 0, 1000);



    }


    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        Log.d(TAG, "...In onPause()...");

        try {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth не поддерживается");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth включен...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        }

        private void errorExit(String title, String message){
            Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
            finish();
        }

        private class ConnectedThread extends Thread {
            private final BluetoothSocket mmSocket;
            private final InputStream mmInStream;
            private final OutputStream mmOutStream;

            public ConnectedThread(BluetoothSocket socket) {
                mmSocket = socket;
                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                // Get the input and output streams, using temp objects because
                // member streams are final
                try {
                    tmpIn = socket.getInputStream();
                    tmpOut = socket.getOutputStream();
                } catch (IOException e) { }

                mmInStream = tmpIn;
                mmOutStream = tmpOut;
            }

            public void run() {
                byte[] buffer = new byte[256];  // buffer store for the stream
                int bytes; // bytes returned from read()

                // Keep listening to the InputStream until an exception occurs
                while (true) {
                    try {
                        // Read from the InputStream
                        bytes = mmInStream.read(buffer);        // Получаем кол-во байт и само собщение в байтовый массив "buffer"
                        h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Отправляем в очередь сообщений Handler
                    } catch (IOException e) {
                        break;
                    }
                }
            }

            /* Call this from the main activity to send data to the remote device */
            public void write(String message) {
                Log.d(TAG, "...Данные для отправки: " + message + "...");
                byte[] msgBuffer = message.getBytes();
                try {
                    mmOutStream.write(msgBuffer);
                } catch (IOException e) {
                    Log.d(TAG, "...Ошибка отправки данных: " + e.getMessage() + "...");
                }
            }

            /* Call this from the main activity to shutdown the connection */
            public void cancel() {
                try {
                    mmSocket.close();
                } catch (IOException e) { }
            }
        }
    }



