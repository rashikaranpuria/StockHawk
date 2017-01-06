package com.udacity.stockhawk.ui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rashi on 27/12/16.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;

    @BindView(R.id.textDetail)
    TextView detail;

    @BindView(R.id.line_chart)
    com.db.chart.view.LineChartView line_chart;

    private LineChartView lineChartView;
    private LineSet mLineSet;
    int maxRange,minRange,step;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mLineSet = new LineSet();
        lineChartView = line_chart;

        Intent intent = getIntent();

        String history = intent.getStringExtra("history");
        String history_ins[] = history.split("\\r?\\n");
        fillLineSet(history_ins);
    }

    private void fillLineSet(String[] history_ins) {
        ArrayList<Float> arrayListPrice = new ArrayList<Float>();
        for (int i = 0; i < history_ins.length; i++){
            String pair[] = history_ins[i].split(", ");
            float price = Float.parseFloat(pair[1]);
            mLineSet.addPoint(pair[0], price);
            arrayListPrice.add(price);
        }
        findRange(arrayListPrice);
        mLineSet.setColor(getResources().getColor(R.color.line_set))
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(getResources().getColor(R.color.line_stroke))
                .setDotsColor(getResources().getColor(R.color.line_dots));
        initLineChart();
        lineChartView.addData(mLineSet);
        Log.d("step.minrange.maxrange", String.valueOf(step) + " " + String.valueOf(minRange) + " " + String.valueOf(maxRange));
        lineChartView.setAxisBorderValues(minRange,maxRange,maxRange-minRange-10);
        lineChartView.show();
    }

    private void initLineChart() {
        Paint gridPaint = new Paint();
        gridPaint.setColor(getResources().getColor(R.color.line_paint));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1f));
        lineChartView.setBorderSpacing(1)
                .setXLabels(AxisController.LabelPosition.OUTSIDE)
                .setYLabels(AxisController.LabelPosition.OUTSIDE)
                .setLabelsColor(getResources().getColor(R.color.line_labels))
                .setXAxis(false)
                .setYAxis(false)
                .setBorderSpacing(Tools.fromDpToPx(5))
                .setGrid(ChartView.GridType.HORIZONTAL, gridPaint);
    }

    public void findRange(ArrayList<Float> mArrayList)
    {
        maxRange = Math.round(Collections.max(mArrayList));
        minRange = Math.round(Collections.min(mArrayList));
        if(maxRange%10 != 0){
            maxRange += (10 - (maxRange%10));
        }
        if(minRange%10 != 0){
            minRange -= (minRange%10);
        }
        int dis = maxRange - minRange;
        Log.d("distance : ", String.valueOf(dis));
        if(dis <= 100){
            step = 10;
        }
        else if(dis <= 1000) {
            step = 100;
        }
        else{
            step = 1000;
        }
        Log.d("distance : ", "step : "+String.valueOf(step));
//        step = maxRange/minRange;
//        if(maxRange<100){
//            step=10;
//        }else if(maxRange<1000){
//            step=100;
//        }else{
//            step=1000;
//        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
