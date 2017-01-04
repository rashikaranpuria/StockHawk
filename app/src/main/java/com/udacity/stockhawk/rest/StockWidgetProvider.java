package com.udacity.stockhawk.rest;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.udacity.stockhawk.sync.QuoteSyncJob;
import com.udacity.stockhawk.sync.StockWidgetService;

import timber.log.Timber;

/**
 * Created by rashi on 28/12/16.
 */

public class StockWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, StockWidgetService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, StockWidgetService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("above on receive in stock widget provider"+intent.getAction());

        super.onReceive(context, intent);
        if(QuoteSyncJob.ACTION_WIDGET_UPDATED.equals(intent.getAction())){
            Timber.d("on receive in stock widget provider");
            context.startService(new Intent(context, StockWidgetService.class));
        }
    }
}
