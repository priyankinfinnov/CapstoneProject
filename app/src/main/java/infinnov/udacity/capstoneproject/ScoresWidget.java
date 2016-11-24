package infinnov.udacity.capstoneproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class ScoresWidget extends AppWidgetProvider {

    public static String getHscore(Context context) {
        String URL = "content://infinnov.udacity.capstoneproject/scores/1";

        Uri scoreUri = Uri.parse(URL);
        CursorLoader cursorLoader = new CursorLoader(context, scoreUri, null, null, null, null);
        Cursor c = cursorLoader.loadInBackground();
        String tempScore = "0";
        if (c.moveToFirst()) {
            tempScore = c.getString(c.getColumnIndex(ScoreProvider.SCORE));
        }
        return "Your HighScore: " + tempScore;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Construct the RemoteViews object
        Intent intent = new Intent(context, Game.class);
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.scores_widget);
        String highScore = getHscore(context);
        views.setTextViewText(R.id.tvawHighScore, highScore);
        views.setOnClickPendingIntent(R.id.btawPlay, pending);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

