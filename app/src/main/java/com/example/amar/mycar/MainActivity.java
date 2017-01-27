package com.example.amar.mycar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button connectAdapter;
    Button liveMonitoring;
    Button Help;
    Button myLocation;
    Button sendBird;
    list_item_BT_Device selectedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        connectAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, BluetoothConnectActivity.class);
                startActivityForResult(i, Constants.MainToBluAct);
//                finish();
            }
        });

        liveMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Live Monitoring was tapped", Toast.LENGTH_SHORT).show();
                if(selectedDevice == null){
                    Toast.makeText(MainActivity.this, "First connect OBD Bluetooth Adapter", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LiveMonitoring.class);
                intent.putExtra(Constants.DEVICE_ADDRESS, selectedDevice.address);
                startActivity(intent);
            }
        });

        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Help was tapped", Toast.LENGTH_SHORT).show();
            }
        });

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        sendBird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SendDataActivity.class);
                startActivity(intent);
            }
        });

//        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//        if(status == ConnectionResult.SUCCESS) {
//            Toast.makeText(this, "HURRAH", Toast.LENGTH_SHORT).show();
//            //Success! Do what you want
//        }



    }

    private void init(){
        this.connectAdapter = (Button) this.findViewById(R.id.button);
        this.liveMonitoring = (Button) this.findViewById(R.id.button2);
        this.Help = (Button) this.findViewById(R.id.button3);
        this.myLocation = (Button) this.findViewById(R.id.button4);
        this.sendBird = (Button) this.findViewById(R.id.button5);
        Typeface mofo = Typeface.createFromAsset(getAssets(), "BadMofo.ttf");
        Typeface digital = Typeface.createFromAsset(getAssets(), "TickingTimebombBB.ttf");
        connectAdapter.setTypeface(mofo);
        liveMonitoring.setTypeface(digital);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.i("BT", "back to main's onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("BT", "back to main's onActivityResult");
        if(requestCode == Constants.MainToBluAct){
            if(resultCode == Constants.noBluetoothAdapterFound){
                Toast.makeText(MainActivity.this, "Local Bluetooth Adapter was not found.", Toast.LENGTH_SHORT).show();
            }else if(resultCode == Constants.BT_BACK_TO_MAIN_BT_OFF){
                Toast.makeText(MainActivity.this, "BT must be enabled.", Toast.LENGTH_SHORT).show();
            }else if(resultCode == Constants.FIRST_PAIR){
                Toast.makeText(MainActivity.this, "Pair with OBD adapter first.", Toast.LENGTH_SHORT).show();
            }else if(resultCode == Constants.NO_DEVICE_SELECTED){
                Toast.makeText(MainActivity.this, "No Device was selected.", Toast.LENGTH_SHORT).show();
            }else if(resultCode == Constants.DEVICE_SELECTED){
                String name = data.getStringExtra(Constants.DEVICE_NAME);
                String address = data.getStringExtra(Constants.DEVICE_ADDRESS);
                this.selectedDevice = new list_item_BT_Device(name, address);
                Log.i("BT", name+", lalala "+address);
            }
        }

        Toast.makeText(MainActivity.this, "WTF", Toast.LENGTH_SHORT).show();
    }
}
