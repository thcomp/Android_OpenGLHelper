package jp.co.thcomp.glsurfaceview;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class GLContext {
    private GLDrawView mView;
    @SuppressWarnings("unused")
    private Context mContext;
    private GLViewSpace mViewSpace;

    private int mRenderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY;
    private boolean mEnableDither = true;
    private boolean mEnableLighting = false;
    private boolean mEnableTexture = true;
    private boolean mEnableDepthTest = true;
    private int mDepthFunc = GL11.GL_LEQUAL;
    private boolean mEnableBlend = true;
    private boolean mEnableVBO = false;
    private int mSFactor = GL11.GL_ONE;
    private int mDFactor = GL11.GL_ONE_MINUS_SRC_ALPHA;
    private int mProjectionType = GLConstant.ProjectionType.ORTHOGRAPHIC;

    public GLContext(GLDrawView view, Context context) {
        mView = view;
        mContext = context;
        mViewSpace = new GLViewSpace(mProjectionType);
    }

    public GLContext(GLDrawView view, Context context, int projectionType) {
        mView = view;
        mContext = context;
        mProjectionType = projectionType;
        mViewSpace = new GLViewSpace(projectionType);
    }

    public void setViewport(int x, int y, int width, int height) {
        mViewSpace.setViewportSize(mProjectionType, x, y, width, height);
    }

    public void setGLConfiguration() {
        mView.setRenderMode(mRenderMode);
        if (mEnableDither) {
            GLES10.glEnable(GL10.GL_DITHER);
        } else {
            GLES10.glDisable(GL10.GL_DITHER);
        }
        if (mEnableLighting) {
            GLES10.glEnable(GL10.GL_LIGHTING);
            GLES10.glEnable(GL10.GL_LIGHT0);
        } else {
            GLES10.glDisable(GL10.GL_LIGHTING);
        }
        if (mEnableDepthTest) {
            GLES10.glEnable(GL10.GL_DEPTH_TEST);
            GLES10.glDepthFunc(mDepthFunc);
        } else {
            GLES10.glDisable(GL10.GL_DEPTH_TEST);
        }
        if (mEnableBlend) {
            GLES10.glEnable(GL10.GL_BLEND);
            GLES10.glBlendFunc(mSFactor, mDFactor);
        } else {
            GLES10.glDisable(GL10.GL_BLEND);
        }
    }

    public GLViewSpace getViewSpace() {
        return mViewSpace;
    }

    public void setViewSpace(GLViewSpace mViewSpace) {
        this.mViewSpace = mViewSpace;
    }

    public int getRenderMode() {
        return mRenderMode;
    }

    public void setRenderMode(int mRenderMode) {
        this.mRenderMode = mRenderMode;
    }

    public boolean isEnableDither() {
        return mEnableDither;
    }

    public void setEnableDither(boolean mEnableDither) {
        this.mEnableDither = mEnableDither;
    }

    public boolean isEnableLighting() {
        return mEnableLighting;
    }

    public void setEnableLighting(boolean mEnableLighting) {
        this.mEnableLighting = mEnableLighting;
    }

    public boolean isEnableTexture() {
        return mEnableTexture;
    }

    public void setEnableTexture(boolean mEnableTexture) {
        this.mEnableTexture = mEnableTexture;
    }

    public boolean isEnableDepthTest() {
        return mEnableDepthTest;
    }

    public void setEnableDepthTest(boolean mEnableDepthTest) {
        this.mEnableDepthTest = mEnableDepthTest;
    }

    public int getDepthFunc() {
        return mDepthFunc;
    }

    public void setDepthFunc(int mDepthFunc) {
        this.mDepthFunc = mDepthFunc;
    }

    public boolean isEnableBlend() {
        return mEnableBlend;
    }

    public void setEnableBlend(boolean mEnableBlend) {
        this.mEnableBlend = mEnableBlend;
    }

    public boolean isEnableVBO() {
        return mEnableVBO;
    }

    public void setEnableVBO(boolean mEnableVBO) {
        this.mEnableVBO = mEnableVBO;
    }

    public int getSFactor() {
        return mSFactor;
    }

    public void setSFactor(int mSFactor) {
        this.mSFactor = mSFactor;
    }

    public int getDFactor() {
        return mDFactor;
    }

    public void setDFactor(int mDFactor) {
        this.mDFactor = mDFactor;
    }

    public int getProjectionType() {
        return mProjectionType;
    }

//	public void setProjectionType(int mProjectionType) {
//		this.mProjectionType = mProjectionType;
//	}

    public float getLeftWR() {
        return mViewSpace.getLeftWR();
    }

    public float getRightWR() {
        return mViewSpace.getRightWR();
    }

    public float getTopWR() {
        return mViewSpace.getTopWR();
    }

    public float getBottomWR() {
        return mViewSpace.getBottomWR();
    }

    public float getZNearWR() {
        return mViewSpace.getZNearWR();
    }

    public float getZFarWR() {
        return mViewSpace.getZFarWR();
    }

    public int getXposVP() {
        return mViewSpace.getXposVP();
    }

    public int getYposVP() {
        return mViewSpace.getYposVP();
    }

    public int getWidthVP() {
        return mViewSpace.getWidthVP();
    }

    public int getHeightVP() {
        return mViewSpace.getHeightVP();
    }
}
