package com.example.dan.opsctask2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dan on 5/12/2018.
 */

public class NotificationsFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "NotificationsFragment";

    Button btnWeight;
    EditText weightEditT;
    ArrayList<Entry> weightList = new ArrayList<>();

    TextView unit;

    float y =1f;

    boolean metric = true;
    String target;
    Float targetF;
    SharedPreferences sharedPreferences;
    private LineChart lineChart;

    public File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    public String fileName = "weight.txt";
    File log = new File(filePath, fileName);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment,container,false);

        weightEditT = (EditText) view.findViewById(R.id.weightEditText);
        btnWeight = (Button) view.findViewById(R.id.buttonSubmit);
        unit = (TextView) view.findViewById(R.id.unit);
        lineChart = (LineChart) view.findViewById(R.id.lineGraph);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        updateUnit();
        setTarget();
        drawGraph();


        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        btnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String w = weightEditT.getText().toString();
                saveToFile(w);

                if (v.getId()==R.id.buttonSubmit) {
                    drawGraph();
                }
            }
        });

    //axis thing
       return view;
    }

    public void drawGraph(){

        lineChart.clear();
        ArrayList<LogWeight> logArray = readFile();
        ArrayList<Entry> weightList = new ArrayList<>();

        y = 0;
        for (LogWeight logWeight : logArray){

            weightList.add(new Entry(y ,logWeight.getWeight()));
            y += 1f;
        }

        LineDataSet setWeight = new LineDataSet(weightList, "Weights");
        setWeight.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setWeight);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);


        XAxis x = lineChart.getXAxis();
        x.setAxisMaximum(30);
        x.setAxisMinimum(0);

        YAxis left = lineChart.getAxisLeft();
        left.setAxisMinimum(30f);
        left.setAxisMaximum(130f);

        LimitLine limit = new LimitLine(targetF,"target weight");
        limit.setLineColor(Color.BLUE);
        left.addLimitLine(limit);

//        left.setDrawLabels(false);
        Description description = new Description();
        description.setText("lbs");
        lineChart.setDescription(description);
        lineChart.setNoDataText("Please input today's weight to view graph");


        YAxis right = lineChart.getAxisRight();
        right.setAxisMaximum(286f);
        right.setAxisMinimum(66f);

        lineChart.invalidate();
        weightEditT.setText("");
    }

    public void updateUnit(){

        metric = sharedPreferences.getBoolean("enable_imperial", true);


        if (metric){
            unit.setText("KGS");
        }else {
            unit.setText("LBS");
        }
    }

    public void setTarget(){
        target = sharedPreferences.getString("key_weight_goal", "");
        targetF = Float.valueOf(target);
    }

    public void saveToFile(String text){

        try {
            log.getParentFile().mkdirs();
            SimpleDateFormat dateFormat = new SimpleDateFormat(("dd/mm/yyyy"));
            String current = dateFormat.format(new Date());
            text += " " + current;
            FileOutputStream fileOutputStream = new FileOutputStream(log, true);
            fileOutputStream.write(text.getBytes());
            fileOutputStream.write("\n".getBytes());
            fileOutputStream.close();

            Toast.makeText(getContext().getApplicationContext(),"saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
                e.printStackTrace();
            Toast.makeText(getContext().getApplicationContext(),"error", Toast.LENGTH_LONG).show();

        }
    }

    public ArrayList<LogWeight> readFile(){

        int w;
        Date d;
        ArrayList<LogWeight> logWeight= new ArrayList<>();

        File file = new File(filePath, "weight.txt");

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String row;

            while ((row = bufferedReader.readLine()) != null){
                String[]tokens = row.split(" ");
                w = Integer.valueOf(tokens[0]);
                d = new Date(tokens[1]);

                LogWeight log = new LogWeight(d, w);
                logWeight.add(log);
            }

            bufferedReader.close();

             } catch (IOException e) {
            e.printStackTrace();
        }

        return logWeight;

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateUnit();
        setTarget();
        drawGraph();

    }
}
