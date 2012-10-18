package jp.stoic.android.bitmaprecycle;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class BitmapWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		final Intent i = new Intent(context, BitmapWidgetService.class);
		i.putExtra("appwidgetids", appWidgetIds);
		context.startService(i);
	}
	
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);

		final Intent i = new Intent(context, BitmapWidgetService.class);
		context.stopService(i);
	}
}
