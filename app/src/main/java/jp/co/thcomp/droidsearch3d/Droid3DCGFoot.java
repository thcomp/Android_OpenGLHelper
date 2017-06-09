package jp.co.thcomp.droidsearch3d;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES11;

import jp.co.thcomp.glsurfaceview.GLCylinder;
import jp.co.thcomp.glsurfaceview.GLDrawView;
import jp.co.thcomp.glsurfaceview.GLHemiSphere;
import jp.co.thcomp.glsurfaceview.RotateInfo;
import jp.co.thcomp.util.DisplayUtil;

public class Droid3DCGFoot extends Droid3DCGParts {
	public static final int LeftFoot = 0;
	public static final int RightFoot = 1;
	public static final float RobotFootOffsetX[] = {RobotLFootOffsetX, RobotRFootOffsetX};

	private GLCylinder mFootCylinder;
	private GLHemiSphere mFootBtmHemiSphere;
	private int mFootIndex;
	private RotateInfo mSwingInfo;
	private RotateInfo mPartsRotateInfo;
	private boolean mFirstSurfaceCreate = true;

	public Droid3DCGFoot(final GLDrawView view, int footIndex) {
		super(view);
		mContext = view.getContext();
		mFootIndex = footIndex;
		if(footIndex != LeftFoot && footIndex != RightFoot){
			throw new UnsupportedOperationException();
		}

		new Thread(new Runnable(){
			@Override
			public void run() {
				GLDrawView view = mView;

				mFootCylinder = new GLCylinder(view);
				mFootBtmHemiSphere = new GLHemiSphere(view);

				mFootCylinder.setColors(RobotColorR, RobotColorG, RobotColorB, RobotColorA);
				mFootBtmHemiSphere.setColors(RobotColorR, RobotColorG, RobotColorB, RobotColorA);
			}
		}).start();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		mSurfaceWidth = width;
		mSurfaceHeight = height;
		float density = DisplayUtil.getDefaultDisplayDensity(mContext);

		if(mFirstSurfaceCreate){
			int footIndex = mFootIndex;
			Context context = mContext;
			float halfWidth = (((float)width) - Droid3DCGParts.getRobotWidth(context)) / 2;
			float halfHeight = (((float)height) - Droid3DCGParts.getRobotHeight(context)) / 2;
			mFootCylinder.setCylinderInfo(density * (RobotFootOffsetX[footIndex] + RobotFootRadius) + halfWidth, density * RobotFootOffsetY + halfHeight, DefaultZOrder, density * RobotFootRadius, density * RobotFootCylinderHeight);
			mFootBtmHemiSphere.setSphereInfo(density * (RobotFootOffsetX[footIndex] + RobotFootRadius) + halfWidth, density * (RobotFootOffsetY + RobotFootCylinderHeight) + halfHeight, DefaultZOrder, density * RobotFootRadius);
			mFootBtmHemiSphere.addRotation(180, 1f, 0f, 0f);
			mFirstSurfaceCreate = false;
		}
	}

	public void swing(float degree, int axis) {
		RotateInfo swingInfo = mSwingInfo;

		if(swingInfo == null){
			swingInfo = mSwingInfo = new RotateInfo();
		}

		swingInfo.rotateDegree = degree;
		switch(axis){
		case AxisX:
			swingInfo.centerX = 1f;
			swingInfo.centerY = 0f;
			swingInfo.centerZ = 0f;
			break;
		case AxisY:
			swingInfo.centerX = 0f;
			swingInfo.centerY = 1f;
			swingInfo.centerZ = 0f;
			break;
		case AxisZ:
			swingInfo.centerX = 0f;
			swingInfo.centerY = 0f;
			swingInfo.centerZ = 1f;
			break;
		}
	}

	public void rotate(float degree, int axis) {
		RotateInfo partsRotateInfo = mPartsRotateInfo;

		if(partsRotateInfo == null){
			partsRotateInfo = mPartsRotateInfo = new RotateInfo();
			mFootCylinder.addRotation(partsRotateInfo);
			mFootBtmHemiSphere.addRotation(partsRotateInfo);
		}

		partsRotateInfo.rotateDegree = degree;
		switch(axis){
		case AxisX:
			partsRotateInfo.centerX = 1f;
			partsRotateInfo.centerY = 0f;
			partsRotateInfo.centerZ = 0f;
			break;
		case AxisY:
			partsRotateInfo.centerX = 0f;
			partsRotateInfo.centerY = 1f;
			partsRotateInfo.centerZ = 0f;
			break;
		case AxisZ:
			partsRotateInfo.centerX = 0f;
			partsRotateInfo.centerY = 0f;
			partsRotateInfo.centerZ = 1f;
			break;
		}
	}

	@Override
	public void drawBase() {
		boolean pushMatrix = false;
		RotateInfo swingInfo = mSwingInfo;

		try{
			if(swingInfo != null && swingInfo.rotateDegree != 0){
				GLES11.glPushMatrix();
				pushMatrix = true;
				GLES11.glRotatef(swingInfo.rotateDegree, swingInfo.centerX, swingInfo.centerY, swingInfo.centerZ);
			}

			mFootCylinder.draw();
			mFootBtmHemiSphere.draw();
		}finally{
			if(pushMatrix){
				GLES11.glPopMatrix();
			}
		}
	}

	@Override
	public void release() {
	}
}
