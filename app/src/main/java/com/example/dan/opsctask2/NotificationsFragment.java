package com.example.dan.opsctask2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";

    Button btnWeight;
    float weights = 1f;

    EditText weightEditT;
    ArrayList<Entry> weightList = new ArrayList<>();

    float x = 1f;
    boolean clicked = false;

    private LineChart lineChart;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment,container,false);

//        final ListView lv = (ListView) view.findViewById(R.id.listview_weight);
//        final ListView lv = (ListView) getView().findViewById(R.id.listview_weight);

        weightEditT = (EditText) view.findViewById(R.id.weightEditText);
        btnWeight = (Button) view.findViewById(R.id.buttonSubmit);
        lineChart = (LineChart) view.findViewById(R.id.lineGraph);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        btnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId()==R.id.buttonSubmit) {
                    weights = Float.valueOf(weightEditT.getText().toString());
                    Entry weight = new Entry(x, weights);
                    weightList.add(weight);

                    LineDataSet setWeight = new LineDataSet(weightList, "Weight");
                    setWeight.setAxisDependency(YAxis.AxisDependency.RIGHT);



                    List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                    dataSets.add(setWeight);

                    LineData data = new LineData(dataSets);
                    lineChart.setData(data);
                    lineChart.invalidate();
                    x += 1f;
                    weightEditT.setText("");
                }
            }
        });



        return view;
    }

}