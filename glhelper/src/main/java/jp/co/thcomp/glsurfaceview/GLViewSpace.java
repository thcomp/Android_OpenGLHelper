package jp.co.thcomp.glsurfaceview;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLU;

public class GLViewSpace {
	protected float mLeftWR = -1.0f;
	protected float mRightWR = 1.0f;
	protected float mTopWR = 1.0f;
	protected float mBottomWR = -1.0f;
	//protected float mZNearWR = -20.0f;
	//protected float mZFarWR = 20.0f;
	protected float mZNearWR = -5.0f;
	protected float mZFarWR = 10.0f;

	protected int mXposVP = 0;
	protected int mYposVP = 0;
	protected int mWidthVP = 0;
	protected int mHeightVP = 0;

	protected float mEyeX = 0.0f;
	protected float mEyeY = 0.0f;
	protected float mEyeZ = 5.0f;
	protected float mCenterX = 0.0f;
	protected float mCenterY = 0.0f;
	protected float mCenterZ = 0.0f;
	protected float mUpX = 0.0f;
	protected float mUpY = 1.0f;
	protected float mUpZ = 0.0f;
	protected boolean mChangedLookAt = false;
	protected boolean mViewPoint = false;
	protected int mProjectionType = GLConstant.ProjectionType.ORTHOGRAPHIC;

	public GLViewSpace(int projectionType){
		mProjectionType = projectionType;

		switch(projectionType){
		case GLConstant.ProjectionType.PERSPECTIVE:
			mZNearWR = 1f;
			mZFarWR = 3f;
			mEyeZ = 1f;
			break;
		default:
			mProjectionType = GLConstant.ProjectionType.ORTHOGRAPHIC;
			break;
		}
	}

	public void setViewportSize(GL10 gl, int projectionType, int x, int y, int width, int height, float zNear, float zFar){
		mZNearWR = zNear;
		mZFarWR = zFar;

		setViewportSize(gl, projectionType, x, y, width, height);
	}

	public void setViewportSize(GL10 gl, int projectionType, int x, int y, int width, int height){
		mXposVP = x;
		mYposVP = y;
		mWidthVP = width;
		mHeightVP = height;

		float ratio = (float) width / height;
		mLeftWR = -ratio;
		mRightWR = ratio;

		gl.glViewport(x, y, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		switch(projectionType){
		case GLConstant.ProjectionType.ORTHOGRAPHIC:
			gl.glOrthof(mLeftWR, mRightWR, mBottomWR, mTopWR, mZNearWR, mZFarWR);
			break;
		case GLConstant.ProjectionType.PERSPECTIVE:
			gl.glFrustumf(mLeftWR, mRightWR, mBottomWR, mTopWR, mZNearWR, mZFarWR);
			break;
		}

		if(!mViewPoint){
			setViewPoint(gl);
		}
	}

	public void setViewPoint(GL10 gl){
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, mEyeX, mEyeY, mEyeZ, mCenterX, mCenterY, mCenterZ, mUpX, mUpY, mUpZ);
		mViewPoint = true;
	}

	public void setViewPoint(GL10 gl, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ){
		mEyeX = eyeX;
		mEyeY = eyeY;
		mEyeZ = eyeZ;
		mCenterX = centerX;
		mCenterY = centerY;
		mCenterZ = centerZ;
		mUpX = upX;
		mUpY = upY;
		mUpZ = upZ;
		GLU.gluLookAt(gl, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		mViewPoint = true;
	}

	public void changeEyePointWR(float eyeX, float eyeY, float eyeZ){
		synchronized(this){
			mEyeX = eyeX;
			mEyeY = eyeY;
			mEyeZ = eyeZ;
			mChangedLookAt = true;
		}
	}

	public void changeCenterPointWR(float centerX, float centerY, float centerZ){
		synchronized(this){
			mCenterX = centerX;
			mCenterY = centerY;
			mCenterZ = centerZ;
			mChangedLookAt = true;
		}
	}

	public void changeUpPointWR(float upX, float upY, float upZ){
		synchronized(this){
			mUpX = upX;
			mUpY = upY;
			mUpZ = upZ;
			mChangedLookAt = true;
		}
	}

	public void updateLookAt(GL10 gl){
		synchronized(this){
			if(mChangedLookAt){
				mChangedLookAt = false;
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				GLU.gluLookAt(gl, mEyeX, mEyeY, mEyeZ, mCenterX, mCenterY, mCenterZ, mUpX, mUpY, mUpZ);
			}
		}
	}

	public float getEyePointX(){
		return mEyeX;
	}

	public float getEyePointY(){
		return mEyeY;
	}

	public float getEyePointZ(){
		return mEyeZ;
	}

	public float getCenterPointX(){
		return mCenterX;
	}

	public float getCenterPointY(){
		return mCenterY;
	}

	public float getCenterPointZ(){
		return mCenterZ;
	}

	public float getUpX(){
		return mUpX;
	}

	public float getUpY(){
		return mUpY;
	}

	public float getUpZ(){
		return mUpZ;
	}

	/*
	 * get the rectangle of world reference from the rectangle of view port
	 * it is assumed of drawn by glDrawArrays with GL10.GL_TRIANGLE_FAN and 
	 * texture's coordinates comes left-top, right-top, right-bottom and left-bottom in that order.
	 * so this function returns left-bottom, right-bottom, right-top and left-top in that order.
	 */
	public float[] getWRRectFromVPRect(Rect rectInVP){
		return getWRRectFromVPRect(new Rect3D(rectInVP));
	}

	public float[] getWRRectFromVPRect(Rect3D rectInVP){
		float ret[] = null;

		if(mViewPoint){
			ret = new float[4*3];
			float leftWR = changeViewPortXtoWorldReferenceX(rectInVP.left);
			float rightWR = changeViewPortXtoWorldReferenceX(rectInVP.right);
			float topWR = changeViewPortYtoWorldReferenceY(rectInVP.top);
			float bottomWR = changeViewPortYtoWorldReferenceY(rectInVP.bottom);

			// left-top
			ret[0] = leftWR;
			ret[1] = topWR;
			ret[2] = changeViewPortZtoWorldReferenceZ(rectInVP.ZofLeftTop);

			// right-top
			ret[3] = rightWR;
			ret[4] = topWR;
			ret[5] = changeViewPortZtoWorldReferenceZ(rectInVP.ZofRightTop);

			// right-bottom
			ret[6] = rightWR;
			ret[7] = bottomWR;
			ret[8] = changeViewPortZtoWorldReferenceZ(rectInVP.ZofRightBottom);

			// left-bottom
			ret[9] = leftWR;
			ret[10] = bottomWR;
			ret[11] = changeViewPortZtoWorldReferenceZ(rectInVP.ZofLeftBottom);
		}

		return ret;
	}

	public Rect3D getVirtualVPRect3DFromWRRect(float arrayInWR[]){
		Rect tempRet = getVirtualVPRectFromWRRect(arrayInWR);
		Rect3D ret = new Rect3D(tempRet);

		try{
			int offset = 0;
			ret.ZofLeftTop = changeWorldReferenceZtoViewPortZ(arrayInWR[offset + 2]);
			offset += 3;
			ret.ZofRightTop = changeWorldReferenceZtoViewPortZ(arrayInWR[offset + 2]);
			offset += 3;
			ret.ZofLeftBottom = changeWorldReferenceZtoViewPortZ(arrayInWR[offset + 2]);
			offset += 3;
			ret.ZofRightBottom = changeWorldReferenceZtoViewPortZ(arrayInWR[offset + 2]);
		}catch(IndexOutOfBoundsException e){
		}

		return ret;
	}

	public Rect getVirtualVPRectFromWRRect(float arrayInWR[]){
		Rect ret = new Rect();
		RectF rectInWR = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

		for(int i=0, size=arrayInWR.length/3; i<size; i++){
			try{
				if(rectInWR.left > arrayInWR[i]){
					rectInWR.left = arrayInWR[i];
				}
				if(rectInWR.right < arrayInWR[i]){
					rectInWR.right = arrayInWR[i];
				}
			}catch(IndexOutOfBoundsException e){
			}

			try{
				if(rectInWR.top > arrayInWR[i + 1]){
					rectInWR.top = arrayInWR[i + 1];
				}
				if(rectInWR.bottom < arrayInWR[i + 1]){
					rectInWR.bottom = arrayInWR[i + 1];
				}
			}catch(IndexOutOfBoundsException e){
			}
		}

		ret.left = changeWorldReferenceXtoViewPortX(rectInWR.left);
		ret.right = changeWorldReferenceXtoViewPortX(rectInWR.right);
		ret.top = changeWorldReferenceYtoViewPortY(rectInWR.top);
		ret.bottom = changeWorldReferenceYtoViewPortY(rectInWR.bottom);

		return ret;
	}

	public float changeViewPortSizeXtoWorldReferenceSizeX(float sizeX){
		return (sizeX / mWidthVP) * (mRightWR - mLeftWR);
	}

	public float changeViewPortSizeYtoWorldReferenceSizeY(float sizeY){
		return (sizeY / mHeightVP) * (mTopWR - mBottomWR);
	}

	public float changeViewPortSizeZtoWorldReferenceSizeZ(float sizeZ){
		//return (sizeZ / mWidthVP) * 0.2f * (mZFarWR - mZNearWR);
		return (sizeZ / (mWidthVP / 2));
	}

	public float changeViewPortXtoWorldReferenceX(float xPosInVP){
		return ((float)(xPosInVP - mXposVP) / mWidthVP) * (mRightWR - mLeftWR) + mLeftWR;
	}

	public float changeViewPortYtoWorldReferenceY(float yPosInVP){
		return mTopWR - ((float)(yPosInVP - mYposVP) / mHeightVP) * (mTopWR - mBottomWR);
	}

	public float changeViewPortZtoWorldReferenceZ(float zPosInVP){
		//return (float)((zPosInVP * (mZFarWR - mZNearWR) / mWidthVP) + (mZFarWR + mZNearWR) / 2);
		return (float)(zPosInVP / (mWidthVP / 2));
	}

	public int changeWorldReferenceXtoViewPortX(float xPosInWR){
		return (int)(((float)(xPosInWR - mLeftWR) / (mRightWR - mLeftWR)) * mWidthVP) + mXposVP;
	}

	public int changeWorldReferenceYtoViewPortY(float yPosInWR){
		//return (int)(((float)(yPosInWR - mBottomWR) / (mRightWR - mLeftWR)) * mHeightVP) + mYposVP;
		return (int)(((float)(yPosInWR - mTopWR) / (mBottomWR - mTopWR)) * mHeightVP) + mYposVP;
	}

	public int changeWorldReferenceZtoViewPortZ(float zPosInWR){
		//return (int)(((float)(zPosInWR) / (mZFarWR - mZNearWR)) * mWidthVP);
		return (int)(((float)(zPosInWR) / 2.0f) * mWidthVP);
	}

	public float getLeftWR() {
		return mLeftWR;
	}

	public float getRightWR() {
		return mRightWR;
	}

	public float getTopWR() {
		return mTopWR;
	}

	public float getBottomWR() {
		return mBottomWR;
	}

	public float getZNearWR() {
		return mZNearWR;
	}

	public float getZFarWR() {
		return mZFarWR;
	}

	public int getXposVP() {
		return mXposVP;
	}

	public int getYposVP() {
		return mYposVP;
	}

	public int getWidthVP() {
		return mWidthVP;
	}

	public int getHeightVP() {
		return mHeightVP;
	}
}
