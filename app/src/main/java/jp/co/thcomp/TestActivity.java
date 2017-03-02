package jp.co.thcomp;

import jp.co.thcomp.gl_sample.R;
import jp.co.thcomp.glsurfaceview.GLCircle;
import jp.co.thcomp.glsurfaceview.GLConstant;
import jp.co.thcomp.glsurfaceview.GLContext;
import jp.co.thcomp.glsurfaceview.GLCube;
import jp.co.thcomp.glsurfaceview.GLCylinder;
import jp.co.thcomp.glsurfaceview.GLDrawElement;
import jp.co.thcomp.glsurfaceview.GLDrawView;
import jp.co.thcomp.glsurfaceview.GLQuad;
import jp.co.thcomp.glsurfaceview.GLElementUpdater;
import jp.co.thcomp.glsurfaceview.GLSphere;
import jp.co.thcomp.glsurfaceview.GLViewSpace;
import jp.co.thcomp.glsurfaceview.Rect3D;
import jp.co.thcomp.util.Constant;
import jp.co.thcomp.util.LogUtil;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;

public class TestActivity extends Activity implements OnTouchListener, OnSeekBarChangeListener, OnCheckedChangeListener, GLElementUpdater.OnUpdateListener{
	private static final int TOTAL_ANIME_DURATION_MS = 500;
	private static final int DURATION_PER_MOVE = 10;
	private static final int MIN_MOVE_BY = 10;
	private static final int MIN_SEEK_VALUE = -50;
	private static final int MAX_SEEK_VALUE = 50;
	private GLDrawView mView;
	private GLQuad mQuad;
	private GLCircle mCircle1;
	private GLCircle mCircle2;
	private GLCircle mPerfectCircle;
	private GLCube mCube1;
	private GLSphere mSphere;
	private GLCylinder mCylinder;
	private int mAnimeCount = 0;
	private float mMoveByX = 0;
	private float mMoveByY = 0;
	private CompoundButton mEnableButton;
	private GLDrawElement mTargetElement;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogUtil.logoutput(Constant.LOG_SWITCH.LOG_SWITCH_DEBUG);

		mView = new GLDrawView(getApplicationContext());
		GLContext glContext = new GLContext(mView, this, GLConstant.ProjectionType.PERSPECTIVE);
		mView.startRenderer(glContext, getApplicationContext());
		//mView.startRenderer(null, getApplicationContext());
		//mView.getGLContext().setEnableLighting(true);
		//this.setContentView(mView);
		setContentView(R.layout.test_main);

		FrameLayout frameForGL = (FrameLayout)findViewById(R.id.FrameForGL);
		frameForGL.addView(mView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

		SeekBar cameraXSeekBar = (SeekBar)findViewById(R.id.CameraXSeekBar);
		cameraXSeekBar.setMax(MAX_SEEK_VALUE - MIN_SEEK_VALUE);
		cameraXSeekBar.setOnSeekBarChangeListener(this);
		SeekBar cameraYSeekBar = (SeekBar)findViewById(R.id.CameraYSeekBar);
		cameraYSeekBar.setMax(MAX_SEEK_VALUE - MIN_SEEK_VALUE);
		cameraYSeekBar.setOnSeekBarChangeListener(this);
		SeekBar cameraZSeekBar = (SeekBar)findViewById(R.id.CameraZSeekBar);
		cameraZSeekBar.setMax(MAX_SEEK_VALUE - MIN_SEEK_VALUE);
		cameraZSeekBar.setOnSeekBarChangeListener(this);

		Switch enableRotateSwitch = (Switch)findViewById(R.id.EnableZorder);
		enableRotateSwitch.setOnCheckedChangeListener(this);
		enableRotateSwitch.setChecked(true);
		Switch enableEyeSwitch = (Switch)findViewById(R.id.EnableEye);
		enableEyeSwitch.setOnCheckedChangeListener(this);
		Switch enableCenterSwitch = (Switch)findViewById(R.id.EnableCenter);
		enableCenterSwitch.setOnCheckedChangeListener(this);
		Switch enableUpSwitch = (Switch)findViewById(R.id.EnableUp);
		enableUpSwitch.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mView.onResume();
		mQuad = new GLQuad(mView);
		mQuad.setOnTouchListener(this);
		Rect3D drawRect = new Rect3D(0, 0, 200, 200, 0, 0, 0, 0);
		//drawRect.ZofLeftBottom = drawRect.ZofLeftTop = drawRect.ZofRightBottom = drawRect.ZofRightTop = 1100;
		mQuad.setDrawRect(drawRect);
		//mQuad.setDrawRect(new Rect(0, 0, 100, 100));
		//mQuad.setColors(new float[]{0.0f}, new float[]{1.0f}, new float[]{0.0f}, new float[]{0.5f});
		Bitmap drawableBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
		//Bitmap texBitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
		//Canvas canvas = new Canvas(texBitmap);
		//canvas.drawBitmap(drawableBitmap, new Rect(0, 0, drawableBitmap.getWidth(), drawableBitmap.getHeight()), new Rect(0, 0, 128, 128), null);
		//mQuad.setTextureBitmap(texBitmap);
		//texBitmap.recycle();
		//mQuad.setTextureBitmap(drawableBitmap, new Rect(0, 0, drawableBitmap.getWidth()/2, drawableBitmap.getHeight()));
		mQuad.setTextureBitmap(drawableBitmap, new Rect(0, 0, drawableBitmap.getWidth() / 2, drawableBitmap.getHeight() / 2));
		drawableBitmap.recycle();
		mView.addDrawParts(mQuad);
		mTargetElement = mQuad;

		mCircle1 = new GLCircle(mView);
		mCircle1.setOnTouchListener(this);
		mCircle1.setCircleInfo(200, 200, 0, 50, 100);
		mCircle1.setColors(new float[]{0.0f}, new float[]{1.0f}, new float[]{0.0f}, new float[]{1.0f});
//		mView.addDrawParts(mCircle1);

		mCircle2 = new GLCircle(mView);
		mCircle2.setOnTouchListener(this);
		mCircle2.setCircleInfo(100, 400, 2, 100, 50);
		mCircle2.setColors(new float[]{0.0f}, new float[]{1.0f}, new float[]{0.0f}, new float[]{1.0f});
//		mView.addDrawParts(mCircle2);

		mPerfectCircle = new GLCircle(mView);
		mPerfectCircle.setOnTouchListener(this);
		mPerfectCircle.setCircleInfo(200, 400, 4, 100, 100);
		mPerfectCircle.setColors(new float[]{0.0f}, new float[]{1.0f}, new float[]{0.0f}, new float[]{1.0f});
//		mView.addDrawParts(mPerfectCircle);

		mCube1 = new GLCube(mView);
		mCube1.setOnTouchListener(this);
		mCube1.setCubeInfo(200, 400, 0, 50, 50, 50);
		mCube1.setColors(new float[]{0.0f}, new float[]{0.0f}, new float[]{1.0f}, new float[]{1.0f});
//		mView.addDrawParts(mCube1);

		mSphere = new GLSphere(mView);
		mSphere.setOnTouchListener(this);
		mSphere.setSphereInfo(200, 200, -100, 200);
		mSphere.setColors(new float[]{0.0f}, new float[]{0.0f}, new float[]{1.0f}, new float[]{1.0f});
//		mView.addDrawParts(mSphere);
//		mTargetElement = mSphere;

		mCylinder = new GLCylinder(mView);
		mCylinder.setOnTouchListener(this);
		//mCylinder.setCylinderInfo(200, 200, 0, 100, 16, 5, 50);
		mCylinder.setCylinderInfo(200, 200, 0, 100, 200);
		mCylinder.setColors(new float[]{0.0f}, new float[]{0.0f}, new float[]{1.0f}, new float[]{1.0f});
		mView.addDrawParts(mCylinder);
	}

	@Override
	protected void onPause() {
		super.onPause();

		mView.onPause();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
//		if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
//			int xPos = (int)event.getX();
//			int yPos = (int)event.getY();
//			mQuad.setDrawRect(new Rect(xPos - 50, yPos - 50, xPos + 50, yPos + 50));
//			mView.requestRender();
//		}else if(event.getAction() == MotionEvent.ACTION_UP){
//			mAnimeCount = 0;
//			mQuad.setOnUpdateListener(this);
//			mView.setAnimation(mQuad);
//		}
		return false;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		GLViewSpace viewSpace = mView.getGLContext().getViewSpace();

		if(mEnableButton != null){
			int id = mEnableButton.getId();

			switch(seekBar.getId()){
			case R.id.CameraXSeekBar:
				switch(id){
				case R.id.EnableZorder:
					break;
				case R.id.EnableEye:
					viewSpace.changeEyePointWR(((float)(progress + MIN_SEEK_VALUE)) / 10, viewSpace.getEyePointY(), viewSpace.getEyePointZ());
					break;
				case R.id.EnableCenter:
					viewSpace.changeCenterPointWR(((float)(progress + MIN_SEEK_VALUE)) / 10, viewSpace.getCenterPointY(), viewSpace.getCenterPointZ());
					break;
				case R.id.EnableUp:
					viewSpace.changeUpPointWR(((float)(progress + MIN_SEEK_VALUE)) / 10, viewSpace.getUpY(), viewSpace.getUpZ());
					break;
				}
				break;
			case R.id.CameraYSeekBar:
				switch(id){
				case R.id.EnableZorder:
					break;
				case R.id.EnableEye:
					viewSpace.changeEyePointWR(viewSpace.getEyePointX(), ((float)(progress + MIN_SEEK_VALUE)) / 10, viewSpace.getEyePointZ());
					break;
				case R.id.EnableCenter:
					viewSpace.changeCenterPointWR(viewSpace.getCenterPointX(), ((float)(progress + MIN_SEEK_VALUE)) / 10, viewSpace.getCenterPointZ());
					break;
				case R.id.EnableUp:
					viewSpace.changeUpPointWR(viewSpace.getUpX(), ((float)(progress + MIN_SEEK_VALUE)) / 10, viewSpace.getUpZ());
					break;
				}
				break;
			case R.id.CameraZSeekBar:
				switch(id){
				case R.id.EnableZorder:
					{
						Rect3D drawRect = new Rect3D(0, 0, 200, 200, 0, 0, 0, 0);
						drawRect.ZofLeftBottom = drawRect.ZofLeftTop = drawRect.ZofRightBottom = drawRect.ZofRightTop = -progress;
						mQuad.removeAllRotation();
						mQuad.removeAllScale();
						mQuad.removeAllTranslate();
						mQuad.setDrawRect(drawRect);
						mView.requestRender();
					}
					break;
				case R.id.EnableEye:
					viewSpace.changeEyePointWR(viewSpace.getEyePointX(), viewSpace.getEyePointY(), ((float)(progress + MIN_SEEK_VALUE)) / 10);
					break;
				case R.id.EnableCenter:
					viewSpace.changeCenterPointWR(viewSpace.getCenterPointX(), viewSpace.getCenterPointY(), ((float)(progress + MIN_SEEK_VALUE)) / 10);
					break;
				case R.id.EnableUp:
					viewSpace.changeUpPointWR(viewSpace.getUpX(), viewSpace.getUpY(), ((float)(progress + MIN_SEEK_VALUE)) / 10);
					break;
				}
				break;
			}
			mView.requestRender();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			setEnableCameraParam(buttonView);
		}
	}

	@Override
	public int onUpdate(GLDrawElement drawElement) {
		final float targetX = 50;
		final float targetY = 50;
		float currentX = drawElement.getXinVP();
		float currentY = drawElement.getYinVP();
		float nextX = currentX;
		float nextY = currentY;

		if(mAnimeCount == 0){
			mMoveByX = (targetX - currentX) / (TOTAL_ANIME_DURATION_MS / DURATION_PER_MOVE);
			mMoveByY = (targetY - currentY) / (TOTAL_ANIME_DURATION_MS / DURATION_PER_MOVE);
			if((mMoveByX > 0) && (mMoveByX < MIN_MOVE_BY)){
				mMoveByX = MIN_MOVE_BY;
			}
			if((mMoveByY > 0) && (mMoveByY < MIN_MOVE_BY)){
				mMoveByY = MIN_MOVE_BY;
			}
		}

		if(mMoveByX == 0 && mMoveByY == 0){
			// no animation
			return GLElementUpdater.NO_MORE_ANIMATION;
		}else if((nextX == targetX) && (nextY == targetY)){
			// no animation
			return GLElementUpdater.NO_MORE_ANIMATION;
		}else{
			if(Math.abs((nextX - targetX) / mMoveByX) < 1){
				nextX = targetX;
			}else{
				nextX = currentX + mMoveByX;
			}
			
			if(Math.abs((nextY - targetY) / mMoveByY) < 1){
				nextY = targetY;
			}else{
				nextY = currentY + mMoveByY;
			}

			mQuad.setDrawRect(new Rect((int)nextX - 50, (int)nextY - 50, (int)nextX + 50, (int)nextY + 50));
		}

		mAnimeCount++;
		return DURATION_PER_MOVE;
	}

	private void setEnableCameraParam(CompoundButton enableButton){
		int ids[] = {R.id.EnableEye, R.id.EnableCenter, R.id.EnableUp};
		int enableId = enableButton.getId();
		mEnableButton = enableButton;

		for(int id : ids){
			if(id == enableId){
				SeekBar cameraXSeekBar = (SeekBar)findViewById(R.id.CameraXSeekBar);
				SeekBar cameraYSeekBar = (SeekBar)findViewById(R.id.CameraYSeekBar);
				SeekBar cameraZSeekBar = (SeekBar)findViewById(R.id.CameraZSeekBar);
				GLViewSpace viewSpace = mView.getViewSpace();

				switch(id){
				case R.id.EnableEye:
					cameraXSeekBar.setProgress((int)(viewSpace.getEyePointX() * 10) - MIN_SEEK_VALUE);
					cameraYSeekBar.setProgress((int)(viewSpace.getEyePointY() * 10) - MIN_SEEK_VALUE);
					cameraZSeekBar.setProgress((int)(viewSpace.getEyePointZ() * 10) - MIN_SEEK_VALUE);
					break;
				case R.id.EnableCenter:
					cameraXSeekBar.setProgress((int)(viewSpace.getCenterPointX() * 10) - MIN_SEEK_VALUE);
					cameraYSeekBar.setProgress((int)(viewSpace.getCenterPointY() * 10) - MIN_SEEK_VALUE);
					cameraZSeekBar.setProgress((int)(viewSpace.getCenterPointZ() * 10) - MIN_SEEK_VALUE);
					break;
				case R.id.EnableUp:
					cameraXSeekBar.setProgress((int)(viewSpace.getUpX() * 10) - MIN_SEEK_VALUE);
					cameraYSeekBar.setProgress((int)(viewSpace.getUpY() * 10) - MIN_SEEK_VALUE);
					cameraZSeekBar.setProgress((int)(viewSpace.getUpZ() * 10) - MIN_SEEK_VALUE);
					break;
				}
			}else{
				Switch disableSwitch = (Switch)findViewById(id);
				disableSwitch.setChecked(false);
			}
		}
	}
}
