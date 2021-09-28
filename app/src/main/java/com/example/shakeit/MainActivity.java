package com.example.shakeit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.format.DateUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tv_shake;
    private SensorManager mSensorMgr;
    private Vibrator mVibrator;
    private Ringtone mRingtone; //Ringtone object
    private int RING_TYPE = AudioManager.STREAM_RING;

    //When the activity is created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_shake = findViewById(R.id.tv_shake);
        // Get sensor manager object from system service
        mSensorMgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // Get vibrator sensor from system service
        mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        // Get Audio Manager from system service
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // Get voice file
        mRingtone = RingtoneManager.getRingtone(MainActivity.this, Uri.parse("/storage/emulated/0/system/media/voice.m4a"));
    }

    // When the activity is hidden
    @Override
    protected void onPause(){
        super.onPause();
        //cancel the listener of acc sensor
        mSensorMgr.unregisterListener(this);
    }

    //When the activity is able to interact wuith user.
    @Override
    protected void onResume(){
        super.onResume();
        // Regist listener for acc sensor
        mSensorMgr.registerListener(this, mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    //Call the function when sensor data is changed.
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //If acc sensor is changed.
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //values[0] refers to x; values[1] refers to y; values[2] refers to z
            float[] values = sensorEvent.values;
            //Set the range of the event could be triggered.
            if(Math.abs(values[0])>15||Math.abs(values[1])>15||Math.abs(values[2])>15){
                tv_shake.setText(DateUtil.getNowTime()+" 您摇了摇！");
                // Vibrate to remind the user
                mVibrator.vibrate(500);
                mRingtone.play();
            }
        }
    }

    //Call the function when sensor's precision is changed.
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Initialize volume
    private void initVolumeInfo(){
        // Get Audio Manager from system service
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // Get the max volume
        int maxVolume = audio.getStreamMaxVolume(RING_TYPE);

    }
}