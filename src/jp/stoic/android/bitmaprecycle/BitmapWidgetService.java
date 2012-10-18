package jp.stoic.android.bitmaprecycle;

import java.util.Random;

import android.app.ActivityManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

public class BitmapWidgetService extends Service {

	private final static long UPDATE_PERIOD = 3*1000;
	
	private Random mRand;
	
	private int[] mAppWidgetIds;
	private AppWidgetManager mAppWidgetManager;
	
	private ActivityManager mActivityManager;
	private ActivityManager.MemoryInfo mMemoryInfo;
	
	private Handler mHandler;
	private boolean mStopped = false;
	
	private Runnable mTicker = new Runnable() {
		
		@Override
		public void run() {
			if (mStopped) return;
			updateAppWidget();
			mHandler.removeCallbacks(mTicker);
			mHandler.postDelayed(mTicker, UPDATE_PERIOD);
		}
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		mRand = new Random();
		mAppWidgetManager = AppWidgetManager.getInstance(this);
		
		mHandler = new Handler();
		
		mActivityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		mMemoryInfo = new ActivityManager.MemoryInfo();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();
		mAppWidgetIds = extras.getIntArray("appwidgetids");

		mHandler.removeCallbacks(mTicker);
		mHandler.post(mTicker);
		return START_STICKY;
	}
	
	private Bitmap createBitmap() {
		Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
		Canvas cv = new Canvas(bitmap);
		int r = mRand.nextInt(256);
		int g = mRand.nextInt(256);
		int b = mRand.nextInt(256);
		Paint paint = new Paint();
		paint.setARGB(0xFF, r, g, b);
		cv.drawRect(0, 0, 256, 256, paint);
		return bitmap;
	}
	
	private void updateAppWidget() {
		RemoteViews rv = new RemoteViews(getPackageName(), R.layout.appwidget);
		rv.setImageViewBitmap(R.id.widget_image, createBitmap());
		mActivityManager.getMemoryInfo(mMemoryInfo);
		rv.setTextViewText(R.id.widget_memory_text,
				String.format("%dk", mMemoryInfo.availMem/1024));
		mAppWidgetManager.updateAppWidget(mAppWidgetIds, rv);
	}
}
