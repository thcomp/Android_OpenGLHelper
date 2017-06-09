package jp.co.thcomp.glsurfaceview;

import android.opengl.GLES11;
import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

public class GLCircle extends GLPolygon {
	private GLVertexFactory.CircleInfo mCircleInfo = null;
	private float mCenterX = 0;
	private float mCenterY = 0;
	private float mCenterZ = 0;
	private float mCircleWidth = 0;
	private float mCircleHeight = 0;
	private float mCenterXWR = Float.MAX_VALUE;
	private float mCenterYWR = Float.MAX_VALUE;
	private float mCenterZWR = Float.MAX_VALUE;
	private float mCircleWidthWR = Float.MAX_VALUE;
	private float mCircleHeightWR = Float.MAX_VALUE;

	public GLCircle(GLDrawView view) {
		super(view);

		mCircleInfo = GLVertexFactory.getCircleInfo();
		setVertex(mCircleInfo.outVertices);
		mDrawMode = mCircleInfo.outModeType;
	}

	public void setCircleInfo(float centerX, float centerY, float centerZ, float circleWidth, float circleHeight){
		GLViewSpace viewSpace = mView.getGLContext().getViewSpace();

		mCenterX = centerX;
		mCenterY = centerY;
		mCenterZ = centerZ;
		mCircleWidth = circleWidth;
		mCircleHeight = circleHeight;
		if(viewSpace.mViewPoint){
			mCenterXWR = viewSpace.changeViewPortXtoWorldReferenceX(centerX);
			mCenterYWR = viewSpace.changeViewPortYtoWorldReferenceY(centerY);
			mCenterZWR = viewSpace.changeViewPortZtoWorldReferenceZ(centerZ);
			mCircleWidthWR = viewSpace.changeViewPortSizeXtoWorldReferenceSizeX(circleWidth);
			mCircleHeightWR = viewSpace.changeViewPortSizeYtoWorldReferenceSizeY(circleHeight);
		}
	}

	@Override
	public void draw() {
		GLViewSpace viewSpace = mView.getGLContext().getViewSpace();

		if(mCenterXWR == Float.MAX_VALUE){
			mCenterXWR = viewSpace.changeViewPortXtoWorldReferenceX(mCenterX);
		}
		if(mCenterYWR == Float.MAX_VALUE){
			mCenterYWR = viewSpace.changeViewPortYtoWorldReferenceY(mCenterY);
		}
		if(mCenterZWR == Float.MAX_VALUE){
			mCenterZWR = viewSpace.changeViewPortZtoWorldReferenceZ(mCenterZ);
		}
		if(mCircleWidthWR == Float.MAX_VALUE){
			mCircleWidthWR = viewSpace.changeViewPortSizeXtoWorldReferenceSizeX(mCircleWidth);
		}
		if(mCircleHeightWR == Float.MAX_VALUE){
			mCircleHeightWR = viewSpace.changeViewPortSizeYtoWorldReferenceSizeY(mCircleHeight);
		}

		GLES11.glPushMatrix();
        GLES11.glTranslatef(mCenterXWR, mCenterYWR, mCenterZWR);
        GLES11.glScalef(mCircleWidthWR, mCircleHeightWR, 1f);

		super.draw();
        GLES11.glPopMatrix();
	}
}
