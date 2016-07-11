package com.iot3.pirdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import net.calit2.mooc.iot_db410c.db410c_gpiolib.GpioProcessor;

/*
 *  Code created by Susan Marosek
 *  Simple Android code to demonstrate Passive Infrared sensor for UCSD Course:
 *      IoT - Sensing and Actuation from Devices
 *      Module 6, Lesson 2.
 *
 * This code creates a simple UI to allow the user to turn on / off the PIR sensor.
 * GPIO 34 of the DragonBoard is set to control the the PIR sensor's VCC (power).
 *
 * GPIO 34 output goes through the Op-Amp that was created in an earlier assignment in this course.
 * An indicator LED is connected to the Op-Amp's output (and VCC). Once the sensor is turned
 * on via the UI, PIR's power and the power indicator LED will also turn on.
 * The output pin of the PIR sensor is connected to a separate LED. If the sensor is turned on,
 * movement in front of the PIR sensor should cause the second LED to turn on. When the motion has
 * stopped, this LED will turn off.
 *
 * NOTE: for me, the motion detection was a little sketchy. Sometimes it took a few seconds for
 * my mothion indicator LED to turn on.
 */

public class PIRActivity extends Activity
{
    static final String tag = "IOT3";
    static final boolean DEBUG = true;     // Set to true to run in emulator mode & test logic only

    GpioProcessor gpioProcessor;
    GpioProcessor.Gpio vcc;

    Switch pirSwitch;
    TextView pirStatus;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_pir );

        gpioProcessor = new GpioProcessor();

        // Initialize gpio
        if ( !DEBUG )
        {
            vcc = gpioProcessor.getPin34();     // Wire connecting pin34 to VCC of PIR
            vcc.out();
        }

        // Initialize UI
        pirSwitch = (Switch) findViewById( R.id.pir_switch );
        pirStatus = (TextView) findViewById( R.id.pir_status );


        // Define listener
        pirSwitch.setChecked( false );
        pirSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton, boolean bChecked )
            {
                if ( bChecked )
                {
                    pirStatus.setText( R.string.switch_on_status );
                    if ( !DEBUG )
                        vcc.high();
                }
                else
                {
                    pirStatus.setText( R.string.switch_off_status );
                    if ( !DEBUG )
                        vcc.low();
                }
            }
        });

        if ( pirSwitch.isChecked())
        {
            pirStatus.setText( R.string.switch_on_status );
            if ( !DEBUG )
                vcc.high();
        }
        else
        {
            pirStatus.setText( R.string.switch_off_status );
            if ( !DEBUG )
                vcc.low();
        }
    }


    /**
     * Name:        onResume
     * Description: Issues a call to the helper method beginNewGame().
     *              We begin the game here because this method is called when
     *              the screen is live.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d( tag, "In onResume" );
    }

    /**
     * Name:        onPause
     * Description: Issues a call to the helper method endGame().
     *              We end the game here because this method is called when
     *              the screen is dead.
     */
    @Override
    protected void onPause()
    {
        // When app in pause state, turn off sensor
        super.onPause();
        cleanup();
    }

    // cleanup
    public void cleanup()
    {
        if ( !DEBUG )
        {
            // Set all pins off/low
            vcc.low();

        }
        Log.d( tag, "All CLEAN" );
    }
}
