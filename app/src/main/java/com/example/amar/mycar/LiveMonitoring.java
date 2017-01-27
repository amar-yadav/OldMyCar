package com.example.amar.mycar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amar.mycar.commands.SpeedCommand;
import com.example.amar.mycar.commands.engine.MassAirFlowCommand;
import com.example.amar.mycar.commands.engine.RPMCommand;
import com.example.amar.mycar.commands.protocol.EchoOffCommand;
import com.example.amar.mycar.commands.protocol.LineFeedOffCommand;
import com.example.amar.mycar.commands.protocol.SelectProtocolCommand;
import com.example.amar.mycar.enums.ObdProtocols;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class LiveMonitoring extends AppCompatActivity {

    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice OBDDevice;
    private BluetoothAdapter bluetoothAdapter;
    Float speed = 0f;
    Double maf = 0.0;
    private LocationManager mLocationManager;
    Thread speedAndKmplthread;
    Thread rpmThread;
    double distanceSinceLMON = 0.0; //in meters
    double fuelConsumedSinceLMON = 0.0;
    int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_monitoring);
        Intent intent = getIntent();
        if (intent == null) {
            Log.i("lm", "no intent received");
            setResult(Constants.LM_Called_Without_Intent);
            finish();
        }

        LinearLayout ll_left = (LinearLayout) this.findViewById(R.id.ll_left);
        LinearLayout ll_right = (LinearLayout) this.findViewById(R.id.ll_right);
        final TextView speed_meter = (TextView) ll_right.findViewById(R.id.speed_meter);
        final TextView rpm_meter = (TextView) ll_right.findViewById(R.id.rpm_meter);
        Typeface mofo = Typeface.createFromAsset(getAssets(), "BadMofo.ttf");
        Typeface digital = Typeface.createFromAsset(getAssets(), "TickingTimebombBB.ttf");

//        ((TextView) ll_left.findViewById(R.id.speed_textVu)).setTypeface(mofo);
//        ((TextView) ll_left.findViewById(R.id.rpm_textVu)).setTypeface(mofo);
        speed_meter.setTypeface(digital);
        rpm_meter.setTypeface(digital);

        try {
            this.mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        } catch (Exception ex) {
            Log.i("location", "locationManager exception");
        }

        String address = intent.getStringExtra(Constants.DEVICE_ADDRESS);

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        this.OBDDevice = bluetoothAdapter.getRemoteDevice(address);


        try {
            this.bluetoothSocket = OBDDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
        } catch (IOException e) {
            e.printStackTrace();
            setResult(Constants.adapter_RFCOMM_fail);
            finish();
        }

        if (this.bluetoothSocket == null) {
            setResult(Constants.socket_null);
            finish();
        }

        try {
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            setResult(Constants.adapter_connect_fail);
            finish();
        }

        try {
            new EchoOffCommand().run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new LineFeedOffCommand().run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try{
//            new TimeoutCommand(10).run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            new SelectProtocolCommand(ObdProtocols.AUTO).run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final RPMCommand rpmCommand = new RPMCommand();
        this.rpmThread = new Thread() {

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted() && flag!=0) {
                    try {
                        rpmCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                        String rpm = rpmCommand.getFormattedResult();
//                        rpm_meter.setText(rpm);
                        Log.i("RPM", rpm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(flag == 0){
                        return;
                    }
                }
            }
        };
        rpmThread.start();

        final SpeedCommand speedCommand = new SpeedCommand();
        final MassAirFlowCommand massAirFlowCommand = new MassAirFlowCommand();
        final ArrayList<Double> mafArrayList = new ArrayList<>();

        this.speedAndKmplthread = new Thread() {

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted() && flag!=0) {
                    try {
                        speedCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                        massAirFlowCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                        String speed = speedCommand.getFormattedResult();
//                        speed_meter.setText(speed);
                        Log.i("Speed", speed+"\n");
                        maf = massAirFlowCommand.getMAF();
                        LiveMonitoring.this.makeChangesToArrayList(mafArrayList, 1, maf);
                        Log.i("Kmpl", (2.98022 * speedCommand.getMetricSpeed()) / maf + "kmpl");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(flag == 0){
                    return;
                }
            }
        };
        speedAndKmplthread.start();
        final long[] t1 = new long[1];
        t1[0] = System.currentTimeMillis();
        final long[] t2 = new long[1];

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        android.location.LocationListener locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                t2[0] = System.currentTimeMillis();
                Double[] maf_tempArray = (Double []) mafArrayList.toArray();
                LiveMonitoring.this.makeChangesToArrayList(mafArrayList, -1, 0.0);
                long time_lapsed = 0;
                if(t2[0] > t1[0]){
                    time_lapsed = t2[0] - t1[0];
                }else{
                    Log.i("Long KMPL", "returned as time_lapsed = 0");
                    return;
                }
                t1[0] = t2[0];
                long avg_t = (time_lapsed/maf_tempArray.length)/1000; //to convert into seconds per maf reading in the time elapsed
                Double total_fuel_consumed_in20m = 0.0;
                for(Double maf_element : maf_tempArray ){
                    total_fuel_consumed_in20m+=maf_element;
                }
                maf_tempArray = null;
                total_fuel_consumed_in20m = total_fuel_consumed_in20m*avg_t;
                distanceSinceLMON+=0.02;
                Log.i("Long KMPL", (distanceSinceLMON/total_fuel_consumed_in20m)+"");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mLocationManager.requestLocationUpdates("gps", 0, 50, locationListener);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    public synchronized void makeChangesToArrayList(ArrayList<Double> mafArrayList, int req, double mafVal){
        if(req == 1){ //add
            mafArrayList.add(maf);
        }else if(req == -1){
            mafArrayList.clear();
        }else{
            Log.i("liveMonitoring", "sync make changes to arraylist, wtf");
        }
    }

    @Override
    public void finish() {
        this.flag = 0;
        super.finish();
    }

    /*
    Double[] maf_tempArray = (Double []) mafArrayList.toArray();
                LiveMonitoring.this.makeChangesToArrayList(mafArrayList, -1, 0.0);
                t2[0] = System.currentTimeMillis();
                long time_lapsed = 0;
                if(t2[0] > t1[0]){
                    time_lapsed = t2[0] - t1[0];
                }else{
                    Log.i("Long KMPL", "returned as time_lapsed = 0");
                    return;
                }
                t1[0] = t2[0];
                long avg_t = (time_lapsed/maf_tempArray.length)/1000; //to convert into seconds per maf reading in the time elapsed
                Double total_fuel_consumed_in20m = 0.0;
                for(Double maf_element : maf_tempArray ){
                    total_fuel_consumed_in20m+=maf_element;
                }
                maf_tempArray = null;
                total_fuel_consumed_in20m = total_fuel_consumed_in20m*avg_t;
                distanceSinceLMON+=0.02;
                Log.i("Long KMPL", (distanceSinceLMON/total_fuel_consumed_in20m)+"");
     */
}
