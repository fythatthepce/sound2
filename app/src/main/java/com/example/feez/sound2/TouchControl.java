package com.example.feez.sound2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.app.ProgressDialog;


import java.io.IOException;
import java.util.UUID;


import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import android.os.AsyncTask;
import android.widget.ProgressBar;



public class TouchControl extends AppCompatActivity {

    ImageButton btnForward;
    ImageButton btnLeft;
    ImageButton btnRight;
    ImageButton btnBack;
    Button btnDis;
    //Button btnBreak;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //Progress bar
    private ProgressBar pg1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_control);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //call the widgets
        btnForward = (ImageButton)findViewById(R.id.up);
        btnLeft = (ImageButton)findViewById(R.id.left);
        btnBack = (ImageButton)findViewById(R.id.down);
        btnRight = (ImageButton)findViewById(R.id.right);
        btnDis = (Button)findViewById(R.id.button_disconnect);
        //btnBreak = (Button)findViewById(R.id.break_motor);
        pg1 = (ProgressBar)findViewById(R.id.progressBar);



        //connect bluetooth
        new ConnectBT().execute(); //Call the class to connect


        /*
        //commands to be sent to bluetooth

        btnForward.setOnTouchListener (new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Bm="1";
                        //SendBluetooth(Bm);
                        break;
                    case MotionEvent.ACTION_UP:
                        Bm="0";
                        break;
                }
                return false;
            }
        });



        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Bm = "2";
                        break;
                    case MotionEvent.ACTION_UP:
                        Bm = "0";
                        break;
                }
                return false;
            }
        });

        btnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Bm = "3";
                        break;
                    case MotionEvent.ACTION_UP:
                        Bm = "0";
                        break;
                }
                return false;
            }
        });

        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Bm = "4";
                        break;
                    case MotionEvent.ACTION_UP:
                        Bm = "0";
                        break;
                }
                return false;
            }
        });
        */


        //test control v2 smart control
        btnForward.setOnTouchListener (new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        forward();
                        pg1.setProgress(100);
                        break;
                    case MotionEvent.ACTION_UP:
                        break_motor();
                        pg1.setProgress(0);
                        break;
                }
                return false;
            }
        });


        btnBack.setOnTouchListener (new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        backward();
                        pg1.setProgress(100);
                        break;
                    case MotionEvent.ACTION_UP:
                        break_motor();
                        pg1.setProgress(0);
                        break;
                }
                return false;
            }
        });

        btnLeft.setOnTouchListener (new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        left();
                        pg1.setProgress(100);
                        break;
                    case MotionEvent.ACTION_UP:
                        pg1.setProgress(0);
                        break_motor();
                        break;
                }
                return false;
            }
        });

        btnRight.setOnTouchListener (new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pg1.setProgress(100);
                        right();
                        break;
                    case MotionEvent.ACTION_UP:
                        pg1.setProgress(0);
                        break_motor();
                        break;
                }
                return false;
            }
        });




        //control v1 have  btnBreak
        /*
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forward();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backward();
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                left();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backward();
            }
        });

        btnBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                break_motor();
            }
        });*/

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect(); //close connection
            }
        });
    }//main



    //function
    private void forward(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("GOt".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void backward(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("BACKt".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void left(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("LEFTt".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void right(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("RIGHTt".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void break_motor(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("BREAK".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }






    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.getOutputStream().write("Disconnect".toString().getBytes());
                btSocket.close(); //close connection
            }
            catch (IOException e)
            {
                //msg("Error");
            }
        }
        finish(); //return to the first layout

    }


    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected
        // BluetoothDevice dispositivo;
        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(TouchControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {

            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available

                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}//scope