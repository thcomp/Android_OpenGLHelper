package jp.co.thcomp.glsurfaceview;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

abstract public class GLDrawElement implements GLSurfaceObserver, OnTouchListener {
    protected GLDrawViewController mView;
    protected GLTexture mTexture = null;
    protected boolean mDirty = false;
    protected OnTouchListener mUserTouchListener;
    protected GLElementUpdater.OnUpdateListener mUpdateListener;

    public GLDrawElement(GLDrawViewController view) {
        mView = view;
    }

    public final void setTextureBitmap(Bitmap bitmap) {
        mTexture = GLTextureCreator.createGLTexture(mView, bitmap);
        mDirty = true;
    }

    public final void setTextureBitmap(Bitmap bitmap, Rect srcRect) {
        mTexture = GLTextureCreator.createGLTexture(mView, bitmap, srcRect);
        mDirty = true;
    }

    public final void setGLTexture(GLTexture texture) {
        mTexture = texture;
        mDirty = true;
    }

    public GLTexture getGLTexture() {
        return mTexture;
    }

    public final void setOnTouchListener(OnTouchListener l) {
        mUserTouchListener = l;
    }

    @Override
    public final boolean onTouch(View v, MotionEvent event) {
        if (mUserTouchListener != null) {
            return mUserTouchListener.onTouch(v, event);
        }
        return false;
    }

    public final void setOnUpdateListener(GLElementUpdater.OnUpdateListener l) {
        mUpdateListener = l;
    }

    public final int update() {
        if (mUpdateListener != null) {
            return mUpdateListener.onUpdate(this);
        }
        return -1;
    }

    //abstract public void draw(GL10 gl);
    //abstract public void release(GL10 gl);
    abstract public void draw();

    abstract public void release();

    abstract public boolean isInRect(float xPosWR, float yPoxWR);

    abstract public float getX();

    abstract public float getY();

    abstract public float getZ();

    abstract public float getXinVP();

    abstract public float getYinVP();

    abstract public float getZinVP();
}
