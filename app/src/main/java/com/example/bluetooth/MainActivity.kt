package com.example.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var text:TextView
    lateinit var button:Button
    lateinit var listView:ListView
    lateinit var bluetoothAdapter:BluetoothAdapter
    lateinit var arrayList: MutableList<String>
    lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.textView)
        button = findViewById(R.id.button)
        listView = findViewById(R.id.listView)

        arrayList = ArrayList<String>()
        arrayAdapter = ArrayAdapter(this, android.R.layout.activity_list_item, arrayList)
        listView.adapter = arrayAdapter

        //the getDefaultAdapter will return null, since the emulator does not support Bluetooth
        //therefore, the action to discover devices will also fail when running on an emulator
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        var intentFilter: IntentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
       //intentFilter.addAction(BluetoothAdapter.ACTION_FOUND)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    val broadcastReceiver = object : BroadcastReceiver(){

        lateinit var name: String
        lateinit var address: String
        lateinit var rssi: String

        override fun onReceive(p0: Context?, p1: Intent?){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var action: String = intent.action.toString()
        Log.i("Action", action)

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action){
                text.setText("Finished")
                button.isEnabled = true
            }
            else if(BluetoothDevice.ACTION_FOUND == action){
                var device:BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                name = device.name
                address = device.address
                rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt())

                //returns all the bluetooth enabled devices discovered by the phone
                Log.i("Device Found", "Name " + name + "Address " + address + "RSSI " + rssi)

                var deviceString: String
                if(name == "" || name == null){
                    deviceString = address + " - RSSI " + rssi + "dBm"
                } else {
                    deviceString = name + " - RSSI " + rssi + "dBm"
                }
                if(!arrayList.contains(deviceString)){
                    arrayList.add(deviceString)
                }
                arrayAdapter.notifyDataSetChanged()
            }
        }
    }

    fun searchClicked(view: View){
        text.setText("Searching ...")
        button.isEnabled = false
        arrayList.clear()
        bluetoothAdapter.startDiscovery()
    }
}