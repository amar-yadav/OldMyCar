package com.example.amar.mycar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnectActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<list_item_BT_Device> bt_devices;
    private ListArrayAdapter listArrayAdapter;
    private Button back_button;
    private UUID myUUID;
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("BT", "Inside onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);
        init();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(BluetoothConnectActivity.this, MainActivity.class);
                startActivity(i); //BACK_TO_MAIN
                finish(); //kill from stack
            }
        });


        if(this.bluetoothAdapter==null){
            Intent i = new Intent();
            i.setClass(BluetoothConnectActivity.this, MainActivity.class);
            setResult(Constants.noBluetoothAdapterFound, i);
            startActivity(i); //BACK_TO_MAIN
            finish(); //kill from stack
        }

        String str = this.bluetoothAdapter.getName() + "\n" + this.bluetoothAdapter.getAddress();
        Log.i("BT", str);

    }

    private void init(){
        Log.i("BT", "starting init");
        this.listView = (ListView) this.findViewById(R.id.listView);
        this.back_button = (Button) this.findViewById(R.id.back);
        this.myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.bt_devices = new ArrayList<>();
        Log.i("BT", "ending init");
    }

    @Override
    protected void onStart() {
        Log.i("BT", "starting onStart");
        super.onStart();
        if(!this.bluetoothAdapter.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, Constants.BT_ENABLE_REQUEST);
            Log.i("BT", "Didn't reach onActivityResult");
        }else{
            setup();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("BT", "starting onActivityResult in BluetoothConnectActivity");
        if(requestCode == Constants.BT_ENABLE_REQUEST){
            if(resultCode == RESULT_OK){
                Log.i("BT", "bluetooth enable success");
                setup(); //--------------SETUP CALL--------------//**********************************************

            }else{
                Intent i = new Intent();
                i.setClass(BluetoothConnectActivity.this, MainActivity.class);
                setResult(Constants.BT_BACK_TO_MAIN_BT_OFF, i);
                startActivity(i); //BACK_TO_MAIN
                finish();  //kill from stack
            }
        }
        Log.i("BT", "LOL");
    }

    private void setup() {
        this.pairedDevices = this.bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0){
            Log.i("BT", "Devices found");
            for(BluetoothDevice device : pairedDevices){
                Log.i("BT", device.getName()+", "+device.getAddress()+"-------\n");
                this.bt_devices.add(new list_item_BT_Device(device.getName(), device.getAddress()));
            }
        }else{
            Log.i("BT", "Devices NOT found");
            Intent i = new Intent();
            i.setClass(BluetoothConnectActivity.this, MainActivity.class);
            setResult(Constants.FIRST_PAIR, i);
            startActivity(i); //BACK_TO_MAIN
            finish();  //kill from stack
        }

        this.listArrayAdapter = new ListArrayAdapter(BluetoothConnectActivity.this, R.layout.list_item_bt_device, this.bt_devices);
        this.listView.setAdapter(listArrayAdapter);
        Log.i("BT", "Selecting OBD 2 adapter");

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list_item_BT_Device selectedDevice = bt_devices.get(i);
                Log.i("BT", selectedDevice.name+", "+selectedDevice.address+", ready to send\n");
                Intent intent = new Intent();
                intent.setClass(BluetoothConnectActivity.this, MainActivity.class);
                intent.putExtra(Constants.DEVICE_NAME, selectedDevice.name);
                intent.putExtra(Constants.DEVICE_ADDRESS, selectedDevice.address);
                setResult(Constants.DEVICE_SELECTED, intent);
//                startActivity(intent);  //BACK_TO_MAIN
                finish(); //kill from stack
            }
        });

    }


}
