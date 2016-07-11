package com.iot3.pirdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import net.calit2.mooc.iot_db410c.db410c_gpiolib.GpioProcessor;

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
