package jp.co.thcomp.glsurfaceview;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.co.thcomp.util.Constant;
import jp.co.thcomp.util.MediaUtil;
import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.opengl.GLES11;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.view.View;

public class GLTexture {
	protected static final int TEXTURE_COORD_SIZE = 2;
	protected static final IntBuffer TEXTURE_BUFFER = IntBuffer.allocate(1);
	protected static final float DEFAULL_TEXTURE_COORD_ARRAY[] = {		// assume to draw by GL10.GL_TRIANGLE_FAN
		0.0f,  0.0f,
		1.0f,  0.0f,
		1.0f, 1.0f,
		0.0f, 1.0f,
	};
	protected GLDrawViewController mView;
	protected Integer mTextureId = null;
	protected FloatBuffer mTextureRegion = null;
	protected Bitmap mTextureBitmap = null;
	protected boolean mAutoRecycle = true;

	public GLTexture(GLDrawViewController view){
		mView = view;
	}

	public GLTexture(GLDrawViewController view, boolean autoRecycle){
		this(view);
		mAutoRecycle = autoRecycle;
	}

	public void bind(){
		if(mTextureRegion != null){
			GLES20.glActiveTexture(GL10.GL_TEXTURE0);
			GLES20.glEnable(GL10.GL_TEXTURE_2D);

			if(mTextureId == null){
				int tempTextureId[] = new int[1];

				GLES20.glGenTextures(1, tempTextureId, 0);
				mTextureId = tempTextureId[0];
				GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

				GLES20.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, mTextureBitmap, 0);
				unregistTextureBitmap();
			}else{
				GLES20.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
			}

			// need to allocate id every call
			GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

			GLES11.glTexCoordPointer(TEXTURE_COORD_SIZE, GL10.GL_FLOAT, 0, mTextureRegion);
		}
	}

	public void unbind(){
		if(mTextureId != null){
			synchronized(TEXTURE_BUFFER){
				TEXTURE_BUFFER.put(mTextureId);
				TEXTURE_BUFFER.position(0);
				GLES20.glDeleteTextures(1, TEXTURE_BUFFER);
				mTextureId = null;
			}
		}
	}

	public void registTextureBitmap(Bitmap bitmap){
		registTextureBitmap(bitmap, DEFAULL_TEXTURE_COORD_ARRAY);
	}

	public void registTextureBitmap(Bitmap bitmap, float rectVertexesX[], float rectVertexesY[], float rectVertexesZ[]){
		int size = rectVertexesX.length;
		float vertexes[] = new float[size * 3];
		for(int i=0; i<size; i++){
			if(rectVertexesX == null){
				vertexes[i + 0] = 0.0f;
			}else{
				vertexes[i + 0] = rectVertexesX[i];
			}

			if(rectVertexesY == null){
				vertexes[i + 1] = 0.0f;
			}else{
				vertexes[i + 1] = rectVertexesY[i];
			}

			if(rectVertexesZ == null){
				vertexes[i + 2] = 0.0f;
			}else{
				vertexes[i + 2] = rectVertexesZ[i];
			}
		}

		registTextureBitmap(bitmap, vertexes);
	}

	public void registTextureBitmap(Bitmap bitmap, float rectVertexes[]){
		unregistTextureBitmap();

		mTextureBitmap = bitmap;
		mTextureRegion = ByteBuffer.allocateDirect(Constant.FLOAT_SIZE * rectVertexes.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTextureRegion.put(rectVertexes);
		mTextureRegion.position(0);
	}

	public void unregistTextureBitmap(){
		// call enable from all thread
		if(mTextureBitmap != null){
			if(mAutoRecycle){
				MediaUtil.BitmapPool.recycle(mTextureBitmap);
			}
			mTextureBitmap = null;
		}
	}

	public int getBitmapConfig(){
		int ret = GL10.GL_INVALID_VALUE;

		if(mTextureBitmap != null && !mTextureBitmap.isRecycled()){
			if((mTextureBitmap.getConfig() == Bitmap.Config.ARGB_8888) || (mTextureBitmap.getConfig() == Bitmap.Config.ARGB_4444)){
				ret = GL10.GL_RGBA;
			}else if(mTextureBitmap.getConfig() == Bitmap.Config.RGB_565){
				ret = GL10.GL_RGB;
			}
		}
		return ret;
	}

	@Override
	protected void finalize() throws Throwable {
		unregistTextureBitmap();
		super.finalize();
	}
}
