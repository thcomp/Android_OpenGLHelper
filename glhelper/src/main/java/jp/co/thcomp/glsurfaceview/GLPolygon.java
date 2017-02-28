package jp.co.thcomp.glsurfaceview;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import jp.co.thcomp.util.Constant;

public class GLPolygon extends GLDrawElement{
	protected static final int COLORS_DIMENSION = 4;
	protected static final int VERTEX_DIMENSION = 3;
	protected static final int VERTEX_X_POSITION = 0;
	protected static final int VERTEX_Y_POSITION = 1;
	protected static final int VERTEX_Z_POSITION = 2;
	protected static final int VBO_INDEX_VERTEX = 0;
	protected static final int VBO_INDEX_COLOR = 1;
	protected static final int VBO_INDEX_NORMAL = 2;
	protected static final int VBO_SIZE = 3;
	//protected static final int VERTEX_ALPHA_POSITION = 3;
	protected int mVboId[];
	protected FloatBuffer mVertex = null;
	protected FloatBuffer mNormals = null;
	protected FloatBuffer mColors = null;
	protected Rect3D mRectInVP = null;
	protected int mVertexSize = 0;
	protected int mNormalsSize = 0;
	protected int mColorsSize = 0;
	protected int mDrawMode = GL10.GL_TRIANGLE_FAN;
	protected ArrayList<RotateInfo> mRotateInfoList = new ArrayList<RotateInfo>();
	protected ArrayList<ScaleInfo> mScaleInfoList = new ArrayList<ScaleInfo>();
	protected ArrayList<TranslateInfo> mTranslateInfoList = new ArrayList<TranslateInfo>();

	public GLPolygon(GLDrawView view){
		super(view);
	}

	public RotateInfo addRotation(float rotateDegree, float rotateCenterX, float rotateCenterY, float rotateCenterZ){
		RotateInfo rotateInfo = new RotateInfo();

		rotateInfo.rotateDegree = rotateDegree;
		rotateInfo.centerX = rotateCenterX;
		rotateInfo.centerY = rotateCenterY;
		rotateInfo.centerZ = rotateCenterZ;

		mRotateInfoList.add(rotateInfo);
		return rotateInfo;
	}

	public ScaleInfo addScale(float scaleX, float scaleY, float scaleZ){
		ScaleInfo scaleInfo = new ScaleInfo();

		scaleInfo.scaleX = scaleX;
		scaleInfo.scaleY = scaleY;
		scaleInfo.scaleZ = scaleZ;

		mScaleInfoList.add(scaleInfo);
		return scaleInfo;
	}

	public TranslateInfo addTranslate(float translateByX, float translateByY, float translateByZ){
		TranslateInfo translateInfo = new TranslateInfo();

		translateInfo.translateByX = translateByX;
		translateInfo.translateByY = translateByY;
		translateInfo.translateByZ = translateByZ;

		mTranslateInfoList.add(translateInfo);
		return translateInfo;
	}

	public void addRotation(RotateInfo rotateInfo){
		mRotateInfoList.add(rotateInfo);
	}

	public void addScale(ScaleInfo scaleInfo){
		scaleInfo.scaleXWR = Float.MAX_VALUE;
		scaleInfo.scaleYWR = Float.MAX_VALUE;
		scaleInfo.scaleZWR = Float.MAX_VALUE;
		mScaleInfoList.add(scaleInfo);
	}

	public void addTranslate(TranslateInfo translateInfo){
		translateInfo.translateByXWR = Float.MAX_VALUE;
		translateInfo.translateByYWR = Float.MAX_VALUE;
		translateInfo.translateByZWR = Float.MAX_VALUE;
		mTranslateInfoList.add(translateInfo);
	}

	public void updateRotation(RotateInfo rotateInfo, float rotateDegree, float rotateCenterX, float rotateCenterY, float rotateCenterZ){
		if(rotateInfo != null){
			rotateInfo.rotateDegree = rotateDegree;
			rotateInfo.centerX = rotateCenterX;
			rotateInfo.centerY = rotateCenterY;
			rotateInfo.centerZ = rotateCenterZ;
		}
	}

	public void updateScale(ScaleInfo scaleInfo, float scaleX, float scaleY, float scaleZ){
		if(scaleInfo != null){
			if(scaleInfo.scaleX != scaleX){
				scaleInfo.scaleX = scaleX;
				scaleInfo.scaleXWR = Float.MAX_VALUE;
			}
			if(scaleInfo.scaleY != scaleY){
				scaleInfo.scaleY = scaleY;
				scaleInfo.scaleYWR = Float.MAX_VALUE;
			}
			if(scaleInfo.scaleZ != scaleZ){
				scaleInfo.scaleZ = scaleZ;
				scaleInfo.scaleZWR = Float.MAX_VALUE;
			}
		}
	}

	public void updateTranslate(TranslateInfo translateInfo, float translateByX, float translateByY, float translateByZ){
		if(translateInfo != null){
			if(translateInfo.translateByX != translateByX){
				translateInfo.translateByX = translateByX;
				translateInfo.translateByXWR = Float.MAX_VALUE;
			}
			if(translateInfo.translateByY != translateByY){
				translateInfo.translateByY = translateByY;
				translateInfo.translateByYWR = Float.MAX_VALUE;
			}
			if(translateInfo.translateByZ != translateByZ){
				translateInfo.translateByZ = translateByZ;
				translateInfo.translateByZWR = Float.MAX_VALUE;
			}
		}
	}

	public void removeRotation(RotateInfo rotateInfo){
		mRotateInfoList.remove(rotateInfo);
	}

	public void removeScale(ScaleInfo scaleInfo){
		mScaleInfoList.remove(scaleInfo);
	}

	public void removeTranslate(TranslateInfo translateInfo){
		mTranslateInfoList.remove(translateInfo);
	}

	public void removeAllRotation(){
		mRotateInfoList.clear();
	}

	public void removeAllScale(){
		mScaleInfoList.clear();
	}

	public void removeAllTranslate(){
		mTranslateInfoList.clear();
	}

	public void setVertex(float[] pos3dArray){
		mVertexSize = pos3dArray.length / VERTEX_DIMENSION;
		mVertex = ByteBuffer.allocateDirect(mVertexSize * Constant.FLOAT_SIZE * VERTEX_DIMENSION).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mVertex.put(pos3dArray);
		mVertex.position(0);
	}

	public void setVertex(float[] xPosArray, float[] yPosArray){
		setVertex(xPosArray, yPosArray, null);
	}

	public void setVertex(float[] xPosArray, float[] yPosArray, float[] zPosArray){
		if(xPosArray != null){
			mVertexSize = xPosArray.length;
		}else if(yPosArray != null){
			mVertexSize = yPosArray.length;
		}else if(zPosArray != null){
			mVertexSize = zPosArray.length;
		}

		mVertex = ByteBuffer.allocateDirect(mVertexSize * Constant.FLOAT_SIZE * VERTEX_DIMENSION).order(ByteOrder.nativeOrder()).asFloatBuffer();
		int position = 0;
		for(int i=0; i<mVertexSize; i++){
			position = i * VERTEX_DIMENSION + 0;
			if(xPosArray != null){
				try{
					mVertex.put(position, xPosArray[i]);
				}catch(IndexOutOfBoundsException e){
					mVertex.put(position, 0);
				}
			}else{
				mVertex.put(position, 0);
			}

			position = i * VERTEX_DIMENSION + 1;
			if(yPosArray != null){
				try{
					mVertex.put(position, yPosArray[i]);
				}catch(IndexOutOfBoundsException e){
					mVertex.put(position, 0);
				}
			}else{
				mVertex.put(position, 0);
			}

			position = i * VERTEX_DIMENSION + 2;
			if(zPosArray != null){
				try{
					mVertex.put(position, zPosArray[i]);
				}catch(IndexOutOfBoundsException e){
					mVertex.put(position, 0);
				}
			}else{
				mVertex.put(position, 0);
			}
		}

		mVertex.position(0);
	}

	public void setNormals(float[] pos3dArray){
		mNormalsSize = pos3dArray.length / VERTEX_DIMENSION;
		mNormals = ByteBuffer.allocateDirect(mVertexSize * Constant.FLOAT_SIZE * VERTEX_DIMENSION).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mNormals.put(pos3dArray);
		mNormals.position(0);
	}

	public void setNormals(float[] xPosArray, float[] yPosArray){
		setVertex(xPosArray, yPosArray, null);
	}

	public void setNormals(float[] xPosArray, float[] yPosArray, float[] zPosArray){
		if(xPosArray != null){
			mNormalsSize = xPosArray.length;
		}else if(yPosArray != null){
			mNormalsSize = yPosArray.length;
		}else if(zPosArray != null){
			mNormalsSize = zPosArray.length;
		}

		mNormals = ByteBuffer.allocateDirect(mNormalsSize * Constant.FLOAT_SIZE * VERTEX_DIMENSION).order(ByteOrder.nativeOrder()).asFloatBuffer();
		int position = 0;
		for(int i=0; i<mNormalsSize; i++){
			position = i * VERTEX_DIMENSION + 0;
			if(xPosArray != null){
				try{
					mNormals.put(position, xPosArray[i]);
				}catch(IndexOutOfBoundsException e){
					mNormals.put(position, 0);
				}
			}else{
				mNormals.put(position, 0);
			}

			position = i * VERTEX_DIMENSION + 1;
			if(yPosArray != null){
				try{
					mNormals.put(position, yPosArray[i]);
				}catch(IndexOutOfBoundsException e){
					mNormals.put(position, 0);
				}
			}else{
				mNormals.put(position, 0);
			}

			position = i * VERTEX_DIMENSION + 2;
			if(zPosArray != null){
				try{
					mNormals.put(position, zPosArray[i]);
				}catch(IndexOutOfBoundsException e){
					mNormals.put(position, 0);
				}
			}else{
				mNormals.put(position, 0);
			}
		}

		mNormals.position(0);
	}

	public void setColor(int color){
		float colorRGBAArray[] = new float[]{
				((float)((color & 0xFF000000) >> 24)) / 0xFF,
				((float)((color & 0x00FF0000) >> 16)) / 0xFF,
				((float)((color & 0x0000FF00) >> 8)) / 0xFF,
				((float)((color & 0x000000FF) >> 0)) / 0xFF};
		mColorsSize = 1;
		mColors = ByteBuffer.allocateDirect(mColorsSize * Constant.FLOAT_SIZE * COLORS_DIMENSION).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mColors.put(colorRGBAArray);
		mColors.position(0);
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
	public void draw(GL10 gl10) {
		if(mVertex != null){
			GL11 gl = (GL11)gl10;
			boolean enableVBO = mView.getGLContext().isEnableVBO();
			if(enableVBO){
				if(mVboId == null){
					mVboId = new int[VBO_SIZE];
					gl.glGenBuffers(VBO_SIZE, mVboId, 0);
					gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVboId[VBO_INDEX_VERTEX]);
					gl.glBufferData(GL11.GL_ARRAY_BUFFER, mVertexSize * Constant.FLOAT_SIZE * VERTEX_DIMENSION, mVertex, GL11.GL_STATIC_DRAW);
					if(mNormals != null){
						gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVboId[VBO_INDEX_NORMAL]);
						gl.glBufferData(GL11.GL_ARRAY_BUFFER, mVertexSize * Constant.FLOAT_SIZE * VERTEX_DIMENSION, mVertex, GL11.GL_STATIC_DRAW);
					}
					if(mColors != null){
						gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVboId[VBO_INDEX_COLOR]);
						gl.glBufferData(GL11.GL_ARRAY_BUFFER, mColorsSize * Constant.FLOAT_SIZE * COLORS_DIMENSION, mColors, GL11.GL_STATIC_DRAW);
					}
				}
			}

			if(mTexture != null){
				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				mTexture.bind(gl);
				if(mColors != null){
					GLContext glContext = mView.mGLContext;
					float[] colorArray = new float[]{mColors.get(0), mColors.get(1), mColors.get(2), mColors.get(3)};

//					gl.glColor4f(mColors.get(0), mColors.get(1), mColors.get(2), mColors.get(3));
					gl.glEnable(GL10.GL_BLEND);
					gl.glBlendFunc(glContext.getSFactor(), glContext.getDFactor());
					gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_BLEND);
					gl.glTexEnvfv(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_COLOR, colorArray, 0);
					gl.glDisable(GL10.GL_BLEND);
				}else{
					gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
				}
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

			gl.glPushMatrix();
			GLViewSpace viewSpace = mView.getViewSpace();
			{
				ArrayList<RotateInfo> rotateInfoList = mRotateInfoList;
				ArrayList<ScaleInfo> scaleInfoList = mScaleInfoList;
				ArrayList<TranslateInfo> translateInfoList = mTranslateInfoList;
				int size;

				if((size = translateInfoList.size()) > 0){
					TranslateInfo translateInfo = null;
					for(int i = size-1; i>= 0; i--){
						translateInfo = translateInfoList.get(i);
						if(translateInfo != null){
							if(translateInfo.translateByXWR == Float.MAX_VALUE){
								translateInfo.translateByXWR = viewSpace.changeViewPortXtoWorldReferenceX(translateInfo.translateByX);
							}
							if(translateInfo.translateByYWR == Float.MAX_VALUE){
								translateInfo.translateByYWR = viewSpace.changeViewPortYtoWorldReferenceY(translateInfo.translateByY);
							}
							if(translateInfo.translateByZWR == Float.MAX_VALUE){
								translateInfo.translateByZWR = viewSpace.changeViewPortZtoWorldReferenceZ(translateInfo.translateByZ);
							}
							gl.glTranslatef(translateInfo.translateByXWR, translateInfo.translateByYWR, translateInfo.translateByZWR);
						}
					}
				}

				if((size = rotateInfoList.size()) > 0){
					RotateInfo rotateInfo = null;
					for(int i = size-1; i>= 0; i--){
						rotateInfo = rotateInfoList.get(i);
						if(rotateInfo.rotateDegree % 360 != 0){
							gl.glRotatef(rotateInfo.rotateDegree, rotateInfo.centerX, rotateInfo.centerY, rotateInfo.centerZ);
						}
					}
				}

				if((size = scaleInfoList.size()) > 0){
					ScaleInfo scaleInfo = null;
					for(int i = size-1; i>= 0; i--){
						scaleInfo = scaleInfoList.get(i);
						if(scaleInfo.scaleXWR == Float.MAX_VALUE){
							scaleInfo.scaleXWR = viewSpace.changeViewPortSizeXtoWorldReferenceSizeX(scaleInfo.scaleX);
						}
						if(scaleInfo.scaleYWR == Float.MAX_VALUE){
							scaleInfo.scaleYWR = viewSpace.changeViewPortSizeYtoWorldReferenceSizeY(scaleInfo.scaleY);
						}
						if(scaleInfo.scaleZWR == Float.MAX_VALUE){
							scaleInfo.scaleZWR = viewSpace.changeViewPortSizeZtoWorldReferenceSizeZ(scaleInfo.scaleZ);
						}
						gl.glScalef(scaleInfo.scaleXWR, scaleInfo.scaleYWR, scaleInfo.scaleZWR);
					}
				}
			}
			if(enableVBO){
				if(mNormals != null){
					gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVboId[VBO_INDEX_NORMAL]);
					gl.glNormalPointer(GL10.GL_FLOAT, 0, 0);
				}
				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVboId[VBO_INDEX_VERTEX]);
				gl.glVertexPointer(VERTEX_DIMENSION, GL10.GL_FLOAT, 0, 0);
			}else{
				gl.glVertexPointer(VERTEX_DIMENSION, GL10.GL_FLOAT, 0, mVertex);
			}
			gl.glDrawArrays(mDrawMode, 0, mVertexSize);
			gl.glPopMatrix();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

	@Override
	public void release(GL10 gl10) {
		GL11 gl = (GL11)gl10;
		if(mTexture != null){
			mTexture.unbind(gl);
			mTexture = null;
		}
		if(mVboId != null){
			gl.glDeleteBuffers(VBO_SIZE, mVboId, 0);
		}
	}

	@Override
	public boolean isInRect(float xPosWR, float yPosWR) {
		boolean ret = false;

		if(mRectInVP != null){
			ret = mRectInVP.contains((int)xPosWR, (int)yPosWR);
		}else{
			if(mVertex != null){
				mRectInVP = mView.getViewSpace().getVirtualVPRect3DFromWRRect(mVertex.array());
				ret = mRectInVP.contains((int)xPosWR, (int)yPosWR);
			}
		}

		return ret;
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

		float ret = 0f;
		TranslateInfo baseTranslateInfo = mTranslateInfoList.size() > 0 ? mTranslateInfoList.get(0) : null;

		if(baseTranslateInfo != null){
			GLViewSpace viewSpace = mView.getViewSpace();
			if(viewSpace.mViewPoint){
				if(baseTranslateInfo.translateByXWR == Float.MAX_VALUE){
					baseTranslateInfo.translateByXWR = viewSpace.changeViewPortXtoWorldReferenceX(baseTranslateInfo.translateByX);
				}
				ret = baseTranslateInfo.translateByXWR;
			}
		}

		return ret;
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

		float ret = 0f;
		TranslateInfo baseTranslateInfo = mTranslateInfoList.size() > 0 ? mTranslateInfoList.get(0) : null;

		if(baseTranslateInfo != null){
			GLViewSpace viewSpace = mView.getViewSpace();
			if(viewSpace.mViewPoint){
				if(baseTranslateInfo.translateByYWR == Float.MAX_VALUE){
					baseTranslateInfo.translateByYWR = viewSpace.changeViewPortYtoWorldReferenceY(baseTranslateInfo.translateByY);
				}
				ret = baseTranslateInfo.translateByYWR;
			}
		}

		return ret;
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

		float ret = 0f;
		TranslateInfo baseTranslateInfo = mTranslateInfoList.size() > 0 ? mTranslateInfoList.get(0) : null;

		if(baseTranslateInfo != null){
			GLViewSpace viewSpace = mView.getViewSpace();
			if(viewSpace.mViewPoint){
				if(baseTranslateInfo.translateByZWR == Float.MAX_VALUE){
					baseTranslateInfo.translateByZWR = viewSpace.changeViewPortZtoWorldReferenceZ(baseTranslateInfo.translateByZ);
				}
				ret = baseTranslateInfo.translateByZWR;
			}
		}

		return ret;
	}

	@Override
	public float getXinVP() {
		return mView.getGLContext().getViewSpace().changeWorldReferenceXtoViewPortX(getX());
	}

	@Override
	public float getYinVP() {
		return mView.getGLContext().getViewSpace().changeWorldReferenceYtoViewPortY(getY());
	}

	@Override
	public float getZinVP() {
		return getZ();
	}
}
