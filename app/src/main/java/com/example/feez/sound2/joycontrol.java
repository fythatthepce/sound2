package com.example.feez.sound2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;


import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import android.os.AsyncTask;
import android.widget.ProgressBar;


public class joycontrol extends Activity  {


    Button btndisconnect;

    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    RelativeLayout layout_joystick;
    ImageView image_joystick, image_border;
    //TextView textView1, textView2, textView3, textView4, textView5;
    TextView textView5;

    JoyStickClass js;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joystick);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        btndisconnect = (Button)findViewById(R.id.button_disconnect);

        btndisconnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Disconnect();
            }
        });



        //connect bluetooth
        new joycontrol.ConnectBT().execute(); //Call the class to connect

        //textView1 = (TextView)findViewById(R.id.textView1);
        //textView2 = (TextView)findViewById(R.id.textView2);
        //textView3 = (TextView)findViewById(R.id.textView3);
        //textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);

        layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

        js = new JoyStickClass(getApplicationContext()
                , layout_joystick, R.drawable.image_button);
        js.setStickSize(200, 200);
        //js.setLayoutSize(500, 500);
        js.setLayoutSize(450, 450);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);

        //js.setOffset(90);
        js.setOffset(170);
        js.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE)
                {
                    //textView1.setText("X : " + String.valueOf(js.getX()));
                    //textView2.setText("Y : " + String.valueOf(js.getY()));
                    //textView3.setText("Angle : " + String.valueOf(js.getAngle()));
                    //textView4.setText("Distance : " + String.valueOf(js.getDistance()));

                    int direction = js.get4Direction();
                    if(direction == JoyStickClass.STICK_UP) {

                        textView5.setText("GO");
                        forward();


                    } else if(direction == JoyStickClass.STICK_RIGHT) {

                        textView5.setText("RIGHT");
                        right();



                    } else if(direction == JoyStickClass.STICK_DOWN) {

                        textView5.setText("DOWN");
                        backward();



                    } else if(direction == JoyStickClass.STICK_LEFT) {

                        textView5.setText("LEFT");
                        left();


                    } else if(direction == JoyStickClass.STICK_NONE) {

                        textView5.setText("STOP");
                        break_motor();
                    }

                    /*
                    else if(direction != JoyStickClass.STICK_NONE  && direction != JoyStickClass.STICK_LEFT && direction != JoyStickClass.STICK_RIGHT && direction != JoyStickClass.STICK_UP && direction != JoyStickClass.STICK_DOWN) {
                        break_motor();
                    }
                    */



                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    //textView1.setText("X :");
                    //textView2.setText("Y :");
                    //textView3.setText("Angle :");
                    //textView4.setText("Distance :");
                    textView5.setText("");
                    break_motor();

                }


                return true;
            }
        });

    }//main


    //function
    private void forward(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("GO".toString().getBytes());
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
                btSocket.getOutputStream().write("BACK".toString().getBytes());
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
                btSocket.getOutputStream().write("LEFT".toString().getBytes());
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
                btSocket.getOutputStream().write("RIGHT".toString().getBytes());
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
            progress = ProgressDialog.show(joycontrol.this, "Connecting...", "Please wait!!!");  //show a progress dialog
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


}//scope
