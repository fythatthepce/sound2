package com.example.feez.sound2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import android.widget.Toast;



import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;


import java.io.IOException;
import java.util.UUID;






public class SoundControl extends AppCompatActivity {


    Button disconnect;


    //private Button openMic;
    private TextView showVoiceText;
    private ImageButton openMic;



    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_control);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        openMic = (ImageButton)findViewById(R.id.imageButton);
        showVoiceText = (TextView)findViewById(R.id.showVoice);

        disconnect = (Button)findViewById(R.id.button1);





        //connect bluetooth
        new ConnectBT().execute(); //Call the class to connect

        openMic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btnToOpenMic();
            }
        });



        /*
        on.setOnTouchListener (new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
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
                        break;
                    case MotionEvent.ACTION_UP:
                    {
                        try
                        {
                            btSocket.getOutputStream().write("STOP".toString().getBytes());
                        }
                        catch (IOException e)
                        {
                            msg("Error");
                        }
                    }
                        break;
                }
                return false;
            }
        });*/

        disconnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Disconnect();
            }
        });





    }//main


    //function
    private void btnToOpenMic(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to Control ...");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);

        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case 10:{
                if (resultCode == RESULT_OK && null!= data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //showVoiceText.setText(result.get(0));


                    //if sound = left show welcome left
                    if(result.contains("left") || result.contains("rest") || result.contains("text") || result.contains("yes")) {
                        showVoiceText.setText("Left");

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



                    }else if(result.contains("right") || result.contains("light") ||result.contains("flight") ){

                        showVoiceText.setText("Right");

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

                    }else if(result.contains("go") || result.contains("call") ){

                        showVoiceText.setText("Go");

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

                    else if(result.contains("back") || result.contains("black")) {
                        showVoiceText.setText("Back");

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



                    else
                        //if(result.contains("break") || result.contains("bet") || result.contains("brick") || result.contains("brett") || result.contains("bear") || result.contains("babe") || result.contains("brea") || result.contains("wreck") || result.contains("bed") ||   result.contains("bitch") ||  result.contains("make") ||  result.contains("brick") )  {
                        if(result.contains("stop") || result.contains("sup") || result.contains("stock") || result.contains("stuff") || result.contains("so")  ){
                        showVoiceText.setText("STOP");

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

                    }else{
                        showVoiceText.setText("No command : " + result.get(0));
                    }
                }
                break;
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
            progress = ProgressDialog.show(SoundControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
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
