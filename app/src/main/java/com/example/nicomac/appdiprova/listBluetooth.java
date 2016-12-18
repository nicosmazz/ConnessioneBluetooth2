package com.example.nicomac.appdiprova;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


/**
 * Created by NicoMac on 17/12/16.
 */

public class listBluetooth  extends AppCompatActivity {
    protected ArrayList<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();
    private static final int BLUETOOTH_ON = 1000;
    private ArrayAdapter<String> btArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listbluetooth);
        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        final ListView lv = (ListView) findViewById(R.id.listViewBluetooth);
        final Button scanBluetooth = (Button) findViewById(R.id.scan);
        btArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(btArrayAdapter);

        if (btAdapter == null)
            Toast.makeText(listBluetooth.this, "Il tuo dispositivo non supporta il Bluetooth", Toast.LENGTH_LONG).show();
        else if (!btAdapter.isEnabled()) {
            Intent BtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(BtIntent, 0);
            Toast.makeText(listBluetooth.this, "Accensione Bluetooth", Toast.LENGTH_LONG).show();
        }

        // Check sui permessi
        int permissionCheck = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        }
        if (permissionCheck != 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }

        scanBluetooth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btArrayAdapter.clear();

                btAdapter.startDiscovery();

                Toast.makeText(listBluetooth.this, "Scanning Devices", Toast.LENGTH_LONG).show();

            }
        });

        registerReceiver(FoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(FoundReceiver, filter);

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(FoundReceiver);
    }


    private final BroadcastReceiver FoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // Ciclo in cui entro se trovo un nuovo dispositivo bluetooth
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // prendo il device bluetooth dall'Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!foundDevices.contains(device)) {
                    foundDevices.add(device);
                    btArrayAdapter.add("name: " + device.getName() + " " + device.getAddress());
                    Toast.makeText(listBluetooth.this, "name: " + device.getName() + " " + device.getAddress(), Toast.LENGTH_LONG).show();
                    btArrayAdapter.notifyDataSetChanged();
                }

            }

            // fine ciclo ricerca dispositivi bluetooth
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (foundDevices == null || foundDevices.isEmpty()) {
                    Toast.makeText(listBluetooth.this, "No Devices", Toast.LENGTH_LONG).show();
                }
            }

        }
    };


}