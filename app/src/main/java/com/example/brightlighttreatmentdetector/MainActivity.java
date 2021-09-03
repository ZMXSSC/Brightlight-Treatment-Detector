package com.example.brightlighttreatmentdetector;

import android.os.CountDownTimer;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private EditText l;
    private Button mButtonStart;
    private Button mButtonStop;
    private boolean flag = false;
//    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    LinkedHashMap<Float, Float> timeLux = new LinkedHashMap<Float, Float>();
    private Chronometer mChronometer;
//    private ArrayList<String> xaxes = new ArrayList<>();
    private float temp;
    private float time;

    LineChart lineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = (EditText) findViewById(R.id.lux);
        mButtonStart = findViewById(R.id.start);
        mButtonStop = findViewById(R.id.stop);
        mChronometer = (Chronometer) findViewById(R.id.cTimer);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        lineChart = (LineChart) findViewById(R.id.lineChart);
        ArrayList<String> xAXES = new ArrayList<>();
        ArrayList<Float> yAXES = new ArrayList<>();
        ArrayList<Entry> yAXES_entry = new ArrayList<>();


        if (lightSensor == null) {
            Toast.makeText(this, "The device has no light sensor !", Toast.LENGTH_SHORT).show();
            finish();
        }

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                mChronometer.setBase(SystemClock.elapsedRealtime()); //Reset the timer
                mChronometer.start();
            }
        });

        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
                mChronometer.stop();
                for(Map.Entry m:timeLux.entrySet()){
                    System.out.println(m.getKey()+" "+m.getValue());
                }
                ArrayList<Float> xAXES = new ArrayList<>(timeLux.keySet());
                ArrayList<Float> yAXES = new ArrayList<>(timeLux.values());
                for(int i = 0; i < yAXES.size(); ++i){
                    yAXES_entry.add(new Entry(xAXES.get(i), yAXES.get(i)));
                }

                final LineDataSet sett = new LineDataSet(yAXES_entry,"Illuminance vs time");
                sett.setDrawCircles(false);
                sett.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                sett.setDrawFilled(true);
                sett.setFillColor(-16711936);
                final LineData group = new LineData(sett);

                lineChart.setData(group);
                lineChart.setVisibleXRangeMaximum(65f);
                timeLux.clear();
            }
        });

        lightEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float value = sensorEvent.values[0];
//                String time = dateFormat.format(new Date());
                String v = String.valueOf(value);
                l.setText(v);

                if(flag){
                    time = (float)(SystemClock.elapsedRealtime() - mChronometer.getBase());
                    time = time / 1000;
                    if(!timeLux.isEmpty()) {
                        float theLastKey = new ArrayList<>(timeLux.keySet()).get(timeLux.size() - 1);
//                        System.out.println("the last key is " + theLastKey);
//                        System.out.println("the current time is " + time);
                        while(time - theLastKey > 5){
                            theLastKey += 5;
                            timeLux.put(theLastKey,temp);
                        }
                    }
                    timeLux.put(time,value);
                }
                temp = value;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
    }
}