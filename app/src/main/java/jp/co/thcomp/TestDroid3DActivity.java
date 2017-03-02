package jp.co.thcomp;

import jp.co.thcomp.droidsearch3d.Droid3DCG;
import jp.co.thcomp.gl_sample.R;
import jp.co.thcomp.glsurfaceview.GLDrawElement;
import jp.co.thcomp.glsurfaceview.GLDrawView;
import jp.co.thcomp.glsurfaceview.GLElementUpdater;
import jp.co.thcomp.glsurfaceview.GLViewSpace;
import jp.co.thcomp.util.Constant;
import jp.co.thcomp.util.LogUtil;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class TestDroid3DActivity extends Activity implements OnTouchListener, OnSeekBarChangeListener, OnCheckedChangeListener, GLElementUpdater.OnUpdateListener, OnClickListener{
	private static final int TOTAL_ANIME_DURATION_MS = 500;
	private static final int DURATION_PER_MOVE = 10;
	private static final int MIN_MOVE_BY = 10;
	private static final int MIN_SEEK_VALUE = -50;
	private static final int MAX_SEEK_VALUE = 50;
	private GLDrawView mView;
	private Droid3DCG mDroid3D;
	private int mRotateDegree = 0;
	private CompoundButton mEnableButton;
	private CompoundButton mEnableSwingButton;
	private CompoundButton mEnableRotateButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogUtil.logoutput(Constant.LOG_SWITCH.LOG_SWITCH_DEBUG);

		mView = new GLDrawView(getApplicationContext());
		mView.startRenderer(null, getApplicationContext());
		//mView.getGLContext().setEnableLighting(true);
		//this.setContentView(mView);
		setContentView(R.layout.test_droid_3d);

		FrameLayout frameForGL = (FrameLayout)findViewById(R.id.FrameForGL);
		frameForGL.addView(mView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		mDroid3D = new Droid3DCG(mView);
		mView.addDrawParts(mDroid3D);
		mDroid3D.moveTo((int)(mDroid3D.getDroidWidth(this) / 2), (int)(mDroid3D.getDroidHeight(this) / 2));

		SeekBar cameraXSeekBar = (SeekBar)findViewById(R.id.CameraXSeekBar);
		cameraXSeekBar.setMax(MAX_SEEK_VALUE - MIN_SEEK_VALUE);
		cameraXSeekBar.setOnSeekBarChangeListener(this);
		SeekBar cameraYSeekBar = (SeekBar)findViewById(R.id.CameraYSeekBar);
		cameraYSeekBar.setMax(MAX_SEEK_VALUE - MIN_SEEK_VALUE);
		cameraYSeekBar.setOnSeekBarChangeListener(this);
		SeekBar cameraZSeekBar = (SeekBar)findViewById(R.id.CameraZSeekBar);
		cameraZSeekBar.setMax(MAX_SEEK_VALUE - MIN_SEEK_VALUE);
		cameraZSeekBar.setOnSeekBarChangeListener(this);

		//RadioButton enableRotateSwitch = (RadioButton)findViewById(R.id.EnableRotate);
		//enableRotateSwitch.setOnCheckedChangeListener(this);
		//enableRotateSwitch.setChecked(true);
		RadioButton enableEyeRButton = (RadioButton)findViewById(R.id.EnableEye);
		enableEyeRButton.setOnCheckedChangeListener(this);
		enableEyeRButton.setChecked(true);
		RadioButton enableCenterRButton = (RadioButton)findViewById(R.id.EnableCenter);
		enableCenterRButton.setOnCheckedChangeListener(this);
		RadioButton enableUpRButton = (RadioButton)findViewById(R.id.EnableUp);
		enableUpRButton.setOnCheckedChangeListener(this);

		RadioButton swingHeadRButton = (RadioButton)findViewById(R.id.SwingHead);
		swingHeadRButton.setChecked(true);
		mEnableSwingButton = swingHeadRButton;
		swingHeadRButton.setOnCheckedChangeListener(this);
		RadioButton swingBodyRButton = (RadioButton)findViewById(R.id.SwingBody);
		swingBodyRButton.setOnCheckedChangeListener(this);
		RadioButton swingLArmRButton = (RadioButton)findViewById(R.id.SwingLArm);
		swingLArmRButton.setOnCheckedChangeListener(this);
		RadioButton swingRArmRButton = (RadioButton)findViewById(R.id.SwingRArm);
		swingRArmRButton.setOnCheckedChangeListener(this);
		RadioButton swingLFootRButton = (RadioButton)findViewById(R.id.SwingLFoot);
		swingLFootRButton.setOnCheckedChangeListener(this);
		RadioButton swingRFootRButton = (RadioButton)findViewById(R.id.SwingRFoot);
		swingRFootRButton.setOnCheckedChangeListener(this);

		Button swingButton = (Button)findViewById(R.id.SwingDroid);
		swingButton.setOnClickListener(this);

		RadioButton rotateAxisXRButton = (RadioButton)findViewById(R.id.RotateAxisX);
		RadioButton rotateAxisYRButton = (RadioButton)findViewById(R.id.RotateAxisY);
		RadioButton rotateAxisZRButton = (RadioButton)findViewById(R.id.RotateAxisZ);
		rotateAxisYRButton.setChecked(true);
		rotateAxisXRButton.setOnCheckedChangeListener(this);
		rotateAxisYRButton.setOnCheckedChangeListener(this);
		rotateAxisZRButton.setOnCheckedChangeListener(this);
		mEnableRotateButton = rotateAxisYRButton;

		Button rotateDroidButton = (Button)findViewById(R.id.RotateDroid);
		rotateDroidButton.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mView.onResume();
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
				//case R.id.EnableRotate:
				//	break;
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
			switch(buttonView.getId()){
			case R.id.EnableEye:
			case R.id.EnableCenter:
			case R.id.EnableUp:
				setEnableCameraParam(buttonView);
				break;
			case R.id.SwingHead:
			case R.id.SwingBody:
			case R.id.SwingLArm:
			case R.id.SwingRArm:
			case R.id.SwingLFoot:
			case R.id.SwingRFoot:
				setSwingParts(buttonView);
				break;
			case R.id.RotateAxisX:
			case R.id.RotateAxisY:
			case R.id.RotateAxisZ:
				setRotateDroid(buttonView);
				break;
			}
		}
	}

	@Override
	public int onUpdate(GLDrawElement drawElement) {
//		final float targetX = 50;
//		final float targetY = 50;
//		float currentX = drawElement.getXinVP();
//		float currentY = drawElement.getYinVP();
//		float nextX = currentX;
//		float nextY = currentY;
//
//		if(mAnimeCount == 0){
//			mMoveByX = (targetX - currentX) / (TOTAL_ANIME_DURATION_MS / DURATION_PER_MOVE);
//			mMoveByY = (targetY - currentY) / (TOTAL_ANIME_DURATION_MS / DURATION_PER_MOVE);
//			if((mMoveByX > 0) && (mMoveByX < MIN_MOVE_BY)){
//				mMoveByX = MIN_MOVE_BY;
//			}
//			if((mMoveByY > 0) && (mMoveByY < MIN_MOVE_BY)){
//				mMoveByY = MIN_MOVE_BY;
//			}
//		}
//
//		if(mMoveByX == 0 && mMoveByY == 0){
//			// no animation
//			return GLElementUpdater.NO_MORE_ANIMATION;
//		}else if((nextX == targetX) && (nextY == targetY)){
//			// no animation
//			return GLElementUpdater.NO_MORE_ANIMATION;
//		}else{
//			if(Math.abs((nextX - targetX) / mMoveByX) < 1){
//				nextX = targetX;
//			}else{
//				nextX = currentX + mMoveByX;
//			}
//			
//			if(Math.abs((nextY - targetY) / mMoveByY) < 1){
//				nextY = targetY;
//			}else{
//				nextY = currentY + mMoveByY;
//			}
//
//			mQuad.setDrawRect(new Rect((int)nextX - 50, (int)nextY - 50, (int)nextX + 50, (int)nextY + 50));
//		}
//
//		mAnimeCount++;
//		return DURATION_PER_MOVE;
		return 0;
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
				RadioButton disableRButton = (RadioButton)findViewById(id);
				disableRButton.setChecked(false);
			}
		}
	}

	private void setSwingParts(CompoundButton enableButton){
		int ids[] = {R.id.SwingHead, R.id.SwingBody, R.id.SwingLArm, R.id.SwingRArm, R.id.SwingLFoot, R.id.SwingRFoot};
		int enableId = enableButton.getId();
		mEnableSwingButton = enableButton;

		for(int id : ids){
			if(id != enableId){
				RadioButton disableRButton = (RadioButton)findViewById(id);
				disableRButton.setChecked(false);
			}
		}
	}

	private void setRotateDroid(CompoundButton enableButton){
		int ids[] = {R.id.RotateAxisX, R.id.RotateAxisY, R.id.RotateAxisZ};
		int enableId = enableButton.getId();
		mEnableRotateButton = enableButton;
		mRotateDegree = 0;

		for(int id : ids){
			if(id != enableId){
				RadioButton disableRButton = (RadioButton)findViewById(id);
				disableRButton.setChecked(false);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.SwingDroid:
			onClickForSwing(v);
			break;
		case R.id.RotateDroid:
			onClickForRotate(v);
			break;
		}
	}

	public void onClickForSwing(View v) {
		int ids[] = {R.id.SwingHead, R.id.SwingBody, R.id.SwingLArm, R.id.SwingRArm, R.id.SwingLFoot, R.id.SwingRFoot};
		int enableId = mEnableSwingButton.getId();

		for(int id : ids){
			if(id == enableId){
				EditText swingDegreeEText = (EditText)findViewById(R.id.SwingDegree);
				if(swingDegreeEText != null){
					String degreeStr = swingDegreeEText.getEditableText().toString();
					int degree = 0;
					try{
						degree = Integer.valueOf(degreeStr);
					}catch(NumberFormatException e){
					}
					int partsType = Droid3DCG.PartsHead;
					int axis = Droid3DCG.AxisX;

					switch(id){
					case R.id.SwingBody:
						partsType = Droid3DCG.PartsBody;
						axis = Droid3DCG.AxisY;
						break;
					case R.id.SwingLArm:
						partsType = Droid3DCG.PartsLArm;
						break;
					case R.id.SwingRArm:
						partsType = Droid3DCG.PartsRArm;
						break;
					case R.id.SwingLFoot:
						partsType = Droid3DCG.PartsLFoot;
						break;
					case R.id.SwingRFoot:
						partsType = Droid3DCG.PartsRFoot;
						break;
					case R.id.SwingHead:
						axis = Droid3DCG.AxisY;
					default:
						break;
					}

					mDroid3D.swingParts(partsType, degree, axis);
				}
				break;
			}
		}
	}

	public void onClickForRotate(View v) {
		int ids[] = {R.id.RotateAxisX, R.id.RotateAxisY, R.id.RotateAxisZ};
		int enableId = mEnableRotateButton.getId();

		for(int id : ids){
			if(id == enableId){
				int axis = Droid3DCG.AxisY;

				switch(id){
				case R.id.RotateAxisX:
					axis = Droid3DCG.AxisX;
					break;
				case R.id.RotateAxisZ:
					axis = Droid3DCG.AxisZ;
					break;
				default:
					break;
				}

				mDroid3D.rotateDroid(mRotateDegree, axis);
				mRotateDegree += 15;
				break;
			}
		}
	}
}
