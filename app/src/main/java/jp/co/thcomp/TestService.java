package jp.co.thcomp;

import javax.microedition.khronos.opengles.GL10;

import jp.co.thcomp.gl.R;
import jp.co.thcomp.glsurfaceview.GLContext;
import jp.co.thcomp.glsurfaceview.GLDrawView;
import jp.co.thcomp.glsurfaceview.GLQuad;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.IBinder;
import android.view.WindowManager;

public class TestService extends Service {
	private static final int SERVICE_FOREGROUND_ID = "TestService".hashCode();
	private GLDrawView mDrawView;
	private GLQuad mQuad1;
	private GLQuad mQuad2;
	private Notification mNotification;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mParams;

	@Override
	public void onCreate() {
		super.onCreate();

		WindowManager windowManager = mWindowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);

		mParams = new WindowManager.LayoutParams();
		mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
		mParams.format = PixelFormat.TRANSLUCENT;
		mParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN |
						WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		mParams.setTitle("Test OpenGL");

		mDrawView = new GLDrawView(this);
		mDrawView.setZOrderOnTop(true);
		mDrawView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mDrawView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
		mDrawView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR | GLSurfaceView.DEBUG_LOG_GL_CALLS);
		mDrawView.setClearColor(Color.TRANSPARENT);
		GLContext glContext = new GLContext(mDrawView, this);
		glContext.setSFactor(GL10.GL_ONE_MINUS_DST_ALPHA);
		glContext.setDFactor(GL10.GL_DST_ALPHA);
		mDrawView.startRenderer(glContext, this);

//		{
//			mQuad2 = new GLQuad(mDrawView);
//			mQuad2.setDrawRect(new Rect(0, 0, 500, 500));
//			mQuad2.setColor(0x4BFFFF46);
////			Bitmap drawableBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.diamond_opaque);
////			mQuad2.setTextureBitmap(drawableBitmap, new Rect(0, 0, drawableBitmap.getWidth(), drawableBitmap.getHeight()));
////			drawableBitmap.recycle();
//			mDrawView.addDrawParts(mQuad2);
//		}

		{
			mQuad1 = new GLQuad(mDrawView);
			mQuad1.setDrawRect(new Rect(0, 0, 500, 500));
//			mQuad1.setColor(0x4BFFFF46);
			Bitmap drawableBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.diamond_opaque);
			mQuad1.setTextureBitmap(drawableBitmap, new Rect(0, 0, drawableBitmap.getWidth(), drawableBitmap.getHeight()));
			drawableBitmap.recycle();
			mDrawView.addDrawParts(mQuad1);
		}

		windowManager.addView(mDrawView, mParams);

		mNotification = new Notification();
		mNotification.flags = Notification.FLAG_FOREGROUND_SERVICE;
		//mNotification.icon = notificationIconRes;

		startForeground(SERVICE_FOREGROUND_ID, mNotification);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(true);
		mWindowManager.removeView(mDrawView);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
