package com.patillosoftware.example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //Result Codes
    final int REQUEST_ENABLE_BT = 2003;

    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First step is to get a bluetooth adapter.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        //Second step is to make sure bluetooth is enabled.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


    }

    @Override
    protected void onStart(){
        super.onStart();

        //Listen for changes in Bluetooth state.
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(detectBTreceiver, filter);

    }

    @Override
    protected void onStop(){
        super.onStop();

        //Make sure Broadcast Receivers are unregistered.
        unregisterReceiver(detectBTreceiver);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ENABLE_BT) {
            //Did the request to enable bluetooth succeed???
            if (resultCode == RESULT_OK) {

            }
        }

    }


    /**
     * Broadcast Receiver that is notified when the Bluetooth state has changed.
     *  -STATE_TURNING_ON
     *  -STATE_ON
     *  -STATE_TURNING_OFF
     *  -STATE_OFF
     */
    private BroadcastReceiver detectBTreceiver = new BroadcastReceiver (){
        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO handle it
        }
    };


}
