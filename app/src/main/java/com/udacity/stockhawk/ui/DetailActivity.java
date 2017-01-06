package com.udacity.stockhawk.ui;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rashi on 27/12/16.
 */

public class DetailActivity extends AppCompatActivity{

    private static final int DETAIL_LOADER = 0;

    @BindView(R.id.per_change)
    TextView per_change;
    @BindView(R.id.abs_change)
    TextView abs_change;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.symbol)
    TextView symbol;

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

        String history = intent.getStringExtra(getString(R.string.history));
        String symbolText = intent.getStringExtra(getString(R.string.symbol));
        String priceText = intent.getStringExtra(getString(R.string.price));
        String per_changeText = intent.getStringExtra(getString(R.string.perchange));
        String abs_changeText = intent.getStringExtra(getString(R.string.abschange));

        // set values
        symbol.setText(symbolText);
        price.setText(priceText);
        per_change.setText(per_changeText);
        abs_change.setText(abs_changeText);

        String history_ins[] = history.split("\\r?\\n");
        fillLineSet(history_ins);
    }

    private void fillLineSet(String[] history_ins) {
        ArrayList<Float> arrayListPrice = new ArrayList<Float>();
        for (int i = 0; i < history_ins.length; i++){
            String pair[] = history_ins[i].split(", ");
            float price = Float.parseFloat(pair[1]);
//            getDateCurrentTimeZone(Long.parseLong(pair[0]))
            mLineSet.addPoint("" , price);
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
        lineChartView.setAxisBorderValues(minRange,maxRange,step);
        lineChartView.show();
    }

    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
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
        int n = 10;
        if(maxRange >= 100){
            n = 100;
        }
        else if(maxRange >=1000){
            n=1000;
        }
        if(maxRange%n != 0){
            maxRange += (n - (maxRange%n));
        }
        if(minRange%n != 0){
            minRange -= (minRange%n);
        }
        int dis = maxRange - minRange;
        Log.d("distance : ", String.valueOf(dis));
        if(dis < 100){
            step = 10;
        }
        else if(dis < 1000) {
            step = 100;
        }
        else{
            step = 1000;
        }
        Log.d("distance : ", "step : "+String.valueOf(step));
    }

}
