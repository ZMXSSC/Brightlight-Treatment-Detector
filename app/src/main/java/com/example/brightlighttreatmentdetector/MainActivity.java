package com.example.brightlighttreatmentdetector;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
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
    private EditText l; // Display Lux
    private TextView r; // Display the result on top of the screen
    private TextView p; // Display the recording period on the right of the screen
    private Button mButtonStart; // Start Button
    private Button mButtonStop; // Stop Button
    private boolean flag = false; // It's not starting yet so set it to false
    private boolean stopFlag = true; // By default it's already "stopped" so true
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
    LinkedHashMap<Float, Float> timeLux = new LinkedHashMap<Float, Float>();
    private Chronometer mChronometer;
    //    private ArrayList<String> xaxes = new ArrayList<>();
    private float temp;
    private float time;
    private int numberOfDuplicates;
    private String currSystemTimeStart;
    private String currSystemTimeEnd;

    LineChart lineChart;

    /* Everything here is depreciate as the screenshot button is no longer needed
    Button click;
    private static final int REQUEST_EXTERNAL_STORAGe = 1;
    private static String[] permissionstorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = (EditText) findViewById(R.id.lux);
        r = (TextView) findViewById(R.id.result);
        p = (TextView) findViewById(R.id.period);

        // To keep the phone awake while the app is running:
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mButtonStart = findViewById(R.id.start);
        mButtonStop = findViewById(R.id.stop);
        mChronometer = (Chronometer) findViewById(R.id.cTimer);

/* Everything here is depreciate as the screenshot button is no longer needed
     click = findViewById(R.id.screenshot);
     verifystoragepermissions(this);
 */

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        lineChart = (LineChart) findViewById(R.id.lineChart);

        ArrayList<Entry> yAXES_entry = new ArrayList<>();



        if (lightSensor == null) {
            Toast.makeText(this, "The device has no light sensor !", Toast.LENGTH_SHORT).show();
            finish();
        }

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                yAXES_entry.clear();
                mChronometer.setBase(SystemClock.elapsedRealtime()); //Reset the timer
                mChronometer.start();
                currSystemTimeStart = dateFormat.format(new Date());
                stopFlag = false;
            }
        });

        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
                if(!stopFlag) {
                    mChronometer.stop();
                    currSystemTimeEnd = dateFormat.format(new Date());
                    for (Map.Entry m : timeLux.entrySet()) {
                        System.out.println(m.getKey() + " " + m.getValue());
                    }
                    ArrayList<Float> xAXES = new ArrayList<>(timeLux.keySet());
                    ArrayList<Float> yAXES = new ArrayList<>(timeLux.values());
                    for (int i = 0; i < yAXES.size(); ++i) {
                        if (i != 0) {
                            if (yAXES.get(i).equals(yAXES.get(i - 1))) {
                                numberOfDuplicates++;
                            }
                        }
                        yAXES_entry.add(new Entry(xAXES.get(i), yAXES.get(i)));
                    }

                    final LineDataSet sett = new LineDataSet(yAXES_entry, "Illuminance vs time");
                    sett.setDrawCircles(false);
                    sett.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    sett.setDrawFilled(true);
                    sett.setFillColor(-16711936); //green
                    final LineData group = new LineData(sett);

                    lineChart.setData(group);
                    lineChart.setVisibleXRangeMaximum(65f);

                    float maxTime = xAXES.get(xAXES.size() - 1);
                    float percentage = numberOfDuplicates * 5 / maxTime * 100;
                    DecimalFormat df = new DecimalFormat("0.00");
                    String myword = "It seems like you are sitting in front of the lamp for \"only\" " + numberOfDuplicates * 5 + " seconds, which is " + df.format(percentage) + "% of the entire time";
                    r.setText(myword);
                    String periodTime = "Recording Period: " + currSystemTimeStart + " to " + currSystemTimeEnd;
                    p.setText(periodTime);

                    xAXES.clear();
                    yAXES.clear();
                    timeLux.clear();
                    stopFlag = true;
                }
            }
        });

        lightEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float value = sensorEvent.values[0];
//                String time = dateFormat.format(new Date());
                String v = String.valueOf(value);
                l.setText(v); // Display lux value onto the textview

                /*
                Because of the hardware restriction, the sensor value won't be updated(more specifically,
                onSensorChanged function won't be called) if the the lux value stay the same. However,
                when we are plotting, it's crucial to record the time where the lux value stays the same.
                Thus the following code block allow us to keep track of the time where the lux value stays
                the same, by counting the time between the last time onSensorChanged is called and the
                current time onSensorChanged is called.
                 */
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
//        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beep);
//        click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "You just Captured a Screenshot," +
//                        " Open Gallery/ File Storage to view your captured Screenshot", Toast.LENGTH_SHORT).show();
//                screenshot(getWindow().getDecorView().getRootView(), "result");
//
//                mediaPlayer.start();
//            }
//        });
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

/*
Everything below is depreciate as the screenshot button is no longer needed
 */
//    protected static File screenshot(View view, String filename) {
//        Date date = new Date();
//
//        // Here we are initialising the format of our image name
//        CharSequence format = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
//        try {
//            // Initialising the directory of storage
//            String dirpath = Environment.getExternalStorageDirectory() + "";
//            File file = new File(dirpath);
//            if (!file.exists()) {
//                boolean mkdir = file.mkdir();
//            }
//
//            // File name
//            String path = dirpath + "/" + filename + "-" + format + ".jpeg";
//            view.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
//            view.setDrawingCacheEnabled(false);
//            File imageurl = new File(path);
//            FileOutputStream outputStream = new FileOutputStream(imageurl);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
//            outputStream.flush();
//            outputStream.close();
//            return imageurl;
//
//        } catch (FileNotFoundException io) {
//            io.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    // verifying if storage permission is given or not
//    public static void verifystoragepermissions(Activity activity) {
//
//        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        // If storage permission is not given then request for External Storage Permission
//        if (permissions != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGe);
//        }
//    }
}

