package com.example.feez.sound2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import android.content.Intent;
import android.widget.ImageButton;

public class SelectControl extends AppCompatActivity {

    ImageButton sound;
    ImageButton touch;
    ImageButton joy;
    ImageButton acc;

    String address = null;

    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_control);

        sound = (ImageButton)findViewById(R.id.sound);
        touch = (ImageButton)findViewById(R.id.touch);
        joy = (ImageButton)findViewById(R.id.joy);
        acc = (ImageButton)findViewById(R.id.acc);




        touch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //get address
                Intent newint = getIntent();
                address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

                //send address
                Intent i = new Intent(SelectControl.this, TouchControl.class);
                //Change the activity.
                i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
                startActivity(i);
            }
        });



        sound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //get address
                Intent newint = getIntent();
                address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

                //send address
                Intent i = new Intent(SelectControl.this, SoundControl.class);
                //Change the activity.
                i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
                startActivity(i);
            }
        });


        joy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {



                //get address
                Intent newint = getIntent();
                address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

                //send address
                Intent i = new Intent(SelectControl.this, joycontrol.class);
                //Change the activity.
                i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
                startActivity(i);

            }
        });


        acc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {



                //get address
                Intent newint = getIntent();
                address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

                //send address
                Intent i = new Intent(SelectControl.this, AccControl.class);
                //Change the activity.
                i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
                startActivity(i);

            }
        });



    }//main

}//scope
