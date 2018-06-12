package com.example.dan.opsctask2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 5/12/2018.
 */

public class HomeFragment extends Fragment implements SensorEventListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "HomeFragment";
    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;
    private ArrayList sensorData;
    private LineChart lineChart;
    Button btnSteps;
    ArrayList<Entry> stepList = new ArrayList<>();
    ArrayList<Entry> targets =  new ArrayList<>();
    float x;
    float y = 1f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
        sensorData = new ArrayList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment,container,false);

        count = (TextView) view.findViewById(R.id.count);
        btnSteps = (Button) view.findViewById(R.id.buttonSubmit);
        lineChart = (LineChart) view.findViewById(R.id.lineGraph);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

       btnSteps.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if (v.getId()==R.id.buttonSubmit) {

                   Entry steps = new Entry(x, y);
                   stepList.add(steps);

                   Entry target =  new Entry(500f,0f);
                   targets.add(target);

                   LineDataSet setSteps = new LineDataSet(stepList, "Steps");
                   setSteps.setAxisDependency(YAxis.AxisDependency.RIGHT);

                   LineDataSet setTarget =  new LineDataSet(targets, "Goal");

                   List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                   dataSets.add(setSteps);
                   dataSets.add(setTarget);

                   LineData data = new LineData(dataSets);
                   lineChart.setData(data);
                   lineChart.invalidate();
                   y += 1f;
               }

           }

       });

        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(getActivity(), "Count sensor not available!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
        // sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            count.setText(String.valueOf(event.values[0]));
            x = (Float.valueOf(event.values[0]));
//            float y = event.values[1];
//            float z = event.values[2];
//            long timestamp = System.currentTimeMillis();
//            StepData data = new StepData(timestamp, x, y, z);
//            sensorData.add(data);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}

