package com.n8.intouch.signin

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import com.n8.intouch.R
import com.n8.intouch.browsescreen.BrowseActivity

/**
 * Implementation of App Widget functionality.
 */
class QuickAddWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // region Private Functions

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

        val widgetText = context.getString(R.string.widget_text)

        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.quick_add_widget)

        val browseIntent = BrowseActivity.createIntentToAutoAdd(context)
        val clickPendingIntent = PendingIntent.getActivity(context, 0, browseIntent, 0)

        views.setOnClickPendingIntent(R.id.widget_view, clickPendingIntent)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    // endregion Private Functions
}

