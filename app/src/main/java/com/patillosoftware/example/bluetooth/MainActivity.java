package com.patillosoftware.example.bluetooth;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
            new AlertDialog.Builder(this)
                    .setTitle("Bluetooth")
                    .setMessage("This device does not support bluetooth.")
                    .setNeutralButton(R.string.exit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {

            //Second step is to make sure bluetooth is enabled.
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

        }


    }


    @Override
    protected void onStart(){
        super.onStart();

        if(mBluetoothAdapter != null) {
            //Listen for changes in Bluetooth state.
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(detectBTreceiver, filter);
        }

    }

    @Override
    protected void onStop(){
        super.onStop();

        if(mBluetoothAdapter != null) {
            //Make sure Broadcast Receivers are unregistered.
            unregisterReceiver(detectBTreceiver);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ENABLE_BT) {
            //Did the request to enable bluetooth succeed???
            if (resultCode == RESULT_OK) {

            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Bluetooth")
                        .setMessage("Bluetooth is not enabled.")
                        .setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Try enabling bluetooth again.
                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            }
                        })
                        .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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
