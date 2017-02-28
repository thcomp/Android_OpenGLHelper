package jp.co.thcomp.glsurfaceview;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Rect;

import jp.co.thcomp.util.Constant;

public class GLPolygon3D extends GLDrawElement{
	protected static final String TAG = "GLPolygon";
	protected static final int COLORS_DIMENSION = 4;
	protected static final int VERTEX_DIMENSION = 3;
	protected static final int VERTEX_X_POSITION = 0;
	protected static final int VERTEX_Y_POSITION = 1;
	protected static final int VERTEX_Z_POSITION = 2;
	//protected static final int VERTEX_ALPHA_POSITION = 3;
	protected FloatBuffer[] mVertex = null;
	protected FloatBuffer[] mNormalVector = null;
	protected FloatBuffer mColors = null;
	protected Rect mRectInVP = null;
	protected int mVertexSize = 0;
	protected int mColorsSize = 0;
	protected int mDrawMode = GL10.GL_TRIANGLE_FAN;

	public GLPolygon3D(GLDrawView view){
		super(view);
	}

	public void setVertex(float[][] pos3dArray){
		mVertex = new FloatBuffer[pos3dArray.length];
		for(int i=0, sizeI=pos3dArray.length; i<sizeI; i++){
			mVertex[i] = ByteBuffer.allocateDirect(pos3dArray[i].length * Constant.FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mVertex[i].put(pos3dArray[i]);
			mVertex[i].position(0);
		}
	}

	public void setNormalVector(float[][] pos3dArray){
		mNormalVector = new FloatBuffer[pos3dArray.length];
		for(int i=0, sizeI=pos3dArray.length; i<sizeI; i++){
			mNormalVector[i] = ByteBuffer.allocateDirect(pos3dArray[i].length * Constant.FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mNormalVector[i].put(pos3dArray[i]);
			mNormalVector[i].position(0);
		}
	}

	public void setColors(float[] colorRGBAArray){
		mColorsSize = colorRGBAArray.length / COLORS_DIMENSION;
		mColors = ByteBuffer.allocateDirect(mColorsSize * Constant.FLOAT_SIZE * COLORS_DIMENSION).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mColors.put(colorRGBAArray);
		mColors.position(0);
	}

	public void setColors(float[] redArray, float[] greenArray, float[] blueArray, float[] alphaArray){
		if(redArray != null){
			mColorsSize = redArray.length;
		}else if(greenArray != null){
			mColorsSize = greenArray.length;
		}else if(blueArray != null){
			mColorsSize = blueArray.length;
		}else if(alphaArray != null){
			mColorsSize = alphaArray.length;
		}

		mColors = ByteBuffer.allocateDirect(mColorsSize * Constant.FLOAT_SIZE * COLORS_DIMENSION).order(ByteOrder.nativeOrder()).asFloatBuffer();
		int position = 0;
		for(int i=0; i<mColorsSize; i++){
			position = i * 3 + 0;
			if(redArray != null){
				try{
					mColors.put(position, redArray[i]);
				}catch(IndexOutOfBoundsException e){
					mColors.put(position, 0);
				}
			}else{
				mColors.put(position, 0);
			}

			position = i * COLORS_DIMENSION + 1;
			if(greenArray != null){
				try{
					mColors.put(position, greenArray[i]);
				}catch(IndexOutOfBoundsException e){
					mColors.put(position, 0);
				}
			}else{
				mColors.put(position, 0);
			}

			position = i * COLORS_DIMENSION + 2;
			if(blueArray != null){
				try{
					mColors.put(position, blueArray[i]);
				}catch(IndexOutOfBoundsException e){
					mColors.put(position, 0);
				}
			}else{
				mColors.put(position, 0);
			}

			position = i * COLORS_DIMENSION + 3;
			if(alphaArray != null){
				try{
					mColors.put(position, alphaArray[i]);
				}catch(IndexOutOfBoundsException e){
					mColors.put(position, 0);
				}
			}else{
				mColors.put(position, 0);
			}
		}

		mColors.position(0);
	}

	@Override
	public void draw(GL10 gl) {
		if(mVertex != null){
			if(mTexture != null){
				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				mTexture.bind(gl);
			}else{
				gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				if(mColors != null){
					if(mColorsSize == 1){
						gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
						gl.glColor4f(mColors.get(0), mColors.get(1), mColors.get(2), mColors.get(3));
						//gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
					}else{
						gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
						gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColors);
					}
				}
			}

			gl.glEnable(GL10.GL_NORMALIZE);
			for(int i=0, size=mVertex.length; i<size; i++){
				gl.glNormal3f(mNormalVector[i].get(0), mNormalVector[i].get(1), mNormalVector[i].get(2));
				gl.glVertexPointer(VERTEX_DIMENSION, GL10.GL_FLOAT, 0, mVertex[i]);
				gl.glDrawArrays(mDrawMode, 0, mVertex[i].capacity() / VERTEX_DIMENSION);
			}
			gl.glDisable(GL10.GL_NORMALIZE);
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

	@Override
	public void release(GL10 gl) {
		if(mTexture != null){
			mTexture.unbind(gl);
			mTexture = null;
		}
	}

	@Override
	public boolean isInRect(float xPosWR, float yPosWR) {
//		boolean ret = false;
//
//		if(mRectInVP != null){
//			ret = mRectInVP.contains((int)xPosWR, (int)yPosWR);
//		}else{
//			if(mVertex != null){
//				mRectInVP = mView.getViewSpace().getVirtualVPRectFromWRRect(mVertex.array());
//				ret = mRectInVP.contains((int)xPosWR, (int)yPosWR);
//			}
//		}
//
//		return ret;
		return false;
	}

	@Override
	public float getX() {
//		float ret = 0;
//
//		if(mVertex != null){
//			for(int i=0; i<mVertexSize; i++){
//				LogUtil.d(TAG, "getX: mVertex.get(" + (i*VERTEX_DIMENSION + VERTEX_X_POSITION) + ")=" + mVertex.get(i*VERTEX_DIMENSION + VERTEX_X_POSITION));
//				ret += mVertex.get(i*VERTEX_DIMENSION + VERTEX_X_POSITION);
//			}
//			ret /= mVertexSize;
//			LogUtil.d(TAG, "getX: " + ret);
//		}else{
//			ret = Float.NaN;
//		}
//
//		return ret;
		return Float.NaN;
	}

	@Override
	public float getY() {
//		float ret = 0;
//
//		if(mVertex != null){
//			for(int i=0; i<mVertexSize; i++){
//				LogUtil.d(TAG, "getY: mVertex.get(" + (i*VERTEX_DIMENSION + VERTEX_Y_POSITION) + ")=" + mVertex.get(i*VERTEX_DIMENSION + VERTEX_Y_POSITION));
//				ret += mVertex.get(i*VERTEX_DIMENSION + VERTEX_Y_POSITION);
//			}
//			ret /= mVertexSize;
//			LogUtil.d(TAG, "getY: " + ret);
//		}else{
//			ret = Float.NaN;
//		}
//
//		return ret;
		return Float.NaN;
	}

	@Override
	public float getZ() {
//		float ret = 0;
//
//		if(mVertex != null){
//			for(int i=0; i<mVertexSize; i++){
//				ret += mVertex.get(i*VERTEX_DIMENSION + VERTEX_Z_POSITION);
//			}
//			ret /= mVertexSize;
//		}else{
//			ret = Float.NaN;
//		}
//
//		return ret;
		return Float.NaN;
	}

	@Override
	public float getXinVP() {
//		return mView.getGLContext().getViewSpace().changeWorldReferenceXtoViewPortX(getX());
		return Float.NaN;
	}

	@Override
	public float getYinVP() {
//		return mView.getGLContext().getViewSpace().changeWorldReferenceYtoViewPortY(getY());
		return Float.NaN;
	}

	@Override
	public float getZinVP() {
//		return getZ();
		return Float.NaN;
	}
}
