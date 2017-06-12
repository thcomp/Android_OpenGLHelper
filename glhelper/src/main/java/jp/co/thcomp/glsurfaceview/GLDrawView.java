package jp.co.thcomp.glsurfaceview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jp.co.thcomp.util.Constant;
import jp.co.thcomp.util.HashMultimap;
import jp.co.thcomp.util.LogUtil;

public class GLDrawView extends GLSurfaceView implements GLSurfaceView.Renderer, OnTouchListener {
    protected static final int SORT_BY_ASC = 0;
    protected static final int SORT_BY_DESC = 1;

    protected ArrayList<GLDrawElement> mRemovedElementList = new ArrayList<GLDrawElement>();
    protected HashMultimap<Float, GLDrawElement> mDrawElementList = new HashMultimap<Float, GLDrawElement>();
    protected ArrayList<GLDrawElement> mDirtyDrawElementList = new ArrayList<GLDrawElement>();
    protected HashMap<Integer, GLDrawElement> mDraggingDrawElementList = new HashMap<Integer, GLDrawElement>();
    protected GLElementUpdater mElementUpdater;
    protected GLContext mGLContext;
    protected GLSurfaceObserver mObserver;
    protected float mClearColorR = 1f;
    protected float mClearColorG = 1f;
    protected float mClearColorB = 1f;
    protected float mClearColorA = 1f;

    @SuppressWarnings("rawtypes")
    protected Comparator mComparators[] = new Comparator[]{
            new Comparator() {
                public int compare(Object object1, Object object2) {
                    int ret = 0;

                    if ((object1 instanceof Float) && (object2 instanceof Float)) {
                        Float f1 = (Float) object1;
                        Float f2 = (Float) object2;

                        ret = (f1 == f2) ? 0 : f1 < f2 ? -1 : 1;
                    }

                    return ret;
                }
            },
            new Comparator() {
                public int compare(Object object1, Object object2) {
                    int ret = 0;

                    if ((object1 instanceof Float) && (object2 instanceof Float)) {
                        Float f1 = (Float) object1;
                        Float f2 = (Float) object2;

                        ret = (f1 == f2) ? 0 : f1 > f2 ? -1 : 1;
                    }

                    return ret;
                }
            },
    };

    public GLDrawView(Context context) {
        this(context, null);
        if (LogUtil.isOutput(Constant.LOG_TYPE.LOG_TYPE_DEBUG)) {
            setDebugFlags(DEBUG_LOG_GL_CALLS | DEBUG_CHECK_GL_ERROR);
        }
        mElementUpdater = new GLElementUpdater(this);
        setClickable(true);
        setOnTouchListener(this);
    }

    public GLDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (LogUtil.isOutput(Constant.LOG_TYPE.LOG_TYPE_DEBUG)) {
            setDebugFlags(DEBUG_LOG_GL_CALLS | DEBUG_CHECK_GL_ERROR);
        }
        mElementUpdater = new GLElementUpdater(this);
        setClickable(true);
        setOnTouchListener(this);
    }

    public void setSurfaceObserver(GLSurfaceObserver observer) {
        mObserver = observer;
    }

    public void startRenderer(GLContext glContext, Context context) {
        if (glContext == null) {
            glContext = new GLContext(this, context);
        }
        mGLContext = glContext;

        setRenderer(this);
    }

    public GLContext getGLContext() {
        return mGLContext;
    }

    public void setClearColor(int color) {
        mClearColorA = ((float) ((color & 0xFF000000) >> 24)) / 0xFF;
        mClearColorR = ((float) ((color & 0x00FF0000) >> 16)) / 0xFF;
        mClearColorG = ((float) ((color & 0x0000FF00) >> 8)) / 0xFF;
        mClearColorB = ((float) ((color & 0x000000FF) >> 0)) / 0xFF;
    }

    public GLViewSpace getViewSpace() {
        return mGLContext.getViewSpace();
    }

    public void initialize(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        mGLContext.setViewport(0, 0, width, height);
        mGLContext.setGLConfiguration();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glColorMask(true, true, true, true);

        gl.glEnable(GL10.GL_LINE_SMOOTH);
    }

    @Override
    public void onResume() {
        super.onResume();
        mElementUpdater.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mElementUpdater.onPause();
    }

    @SuppressWarnings("unchecked")
    protected GLDrawElement isInRect(int sortBy, int xWolrdPos, int yWolrdPos) {
        synchronized (mDrawElementList) {
            mDrawElementList.sort(mComparators[sortBy]);
            GLDrawElement tempDrawElement = null;
            for (int i = 0, size = mDrawElementList.keySize(); i < size; i++) {
                List<GLDrawElement> tempDrawElementList = mDrawElementList.getByIndex(i);
                if (tempDrawElementList != null) {
                    for (int j = 0, sizeJ = tempDrawElementList.size(); j < sizeJ; j++) {
                        tempDrawElement = tempDrawElementList.get(j);
                        if (tempDrawElement != null) {
                            if (tempDrawElement.isInRect(xWolrdPos, yWolrdPos)) {
                                return tempDrawElement;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public void addDrawParts(GLDrawElement drawElement) {
        mDrawElementList.add(0f, drawElement);
    }

    public void addDrawParts(float zPos, GLDrawElement drawElement) {
        mDrawElementList.add(zPos, drawElement);
    }

    public int getDrawPartsNum() {
        return mDrawElementList.size();
    }

    public void removeDrawPartsAll() {
        List<Float> keyList = mDrawElementList.keyList();
        for (int i = 0, size = keyList.size(); i < size; i++) {
            mRemovedElementList.addAll(mDrawElementList.remove(keyList.get(i)));
        }
        mRemovedElementList.addAll(mDirtyDrawElementList);
        mDrawElementList.clearAll();
        mDirtyDrawElementList.clear();
    }

    public void setAnimation(GLDrawElement drawElement) {
        mElementUpdater.add(drawElement);
    }

    public void removeDrawParts(GLDrawElement drawElement) {
        mRemovedElementList.add(drawElement);
        if (!mDrawElementList.removeValue(drawElement)) {
            mDirtyDrawElementList.remove(drawElement);
        }
    }

    public void removeAnimation(GLDrawElement drawElement) {
        mElementUpdater.remove(drawElement);
    }

    public void removeAnimationAll() {
        mElementUpdater.removeAll();
    }

    public void setDirty(GLDrawElement dirtyPart) {
        synchronized (mDrawElementList) {
            mDrawElementList.removeValue(dirtyPart);
        }
        synchronized (mDirtyDrawElementList) {
            mDirtyDrawElementList.add(dirtyPart);
        }
    }

    public void onDrawFrame(GL10 gl) {
        LogUtil.i(GLConstant.TAG, ".onDrawFrame<S>");

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(mClearColorR, mClearColorG, mClearColorB, mClearColorA);

        for (int iCount = 0; iCount < mRemovedElementList.size(); iCount++) {
            mRemovedElementList.remove(iCount).mTexture.unbind();
        }

        mGLContext.getViewSpace().updateLookAt();
        updateDrawPart(gl);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        drawElements(gl);

        LogUtil.i(GLConstant.TAG, ".onDrawFrame<E>");
    }

    protected void updateDrawPart(GL10 gl) {
        LogUtil.i(GLConstant.TAG, ".updateDrawPart<S>");

        ArrayList<GLDrawElement> tempDrawPartList = new ArrayList<GLDrawElement>();

//		synchronized(mDirtyDrawElementList){
//			GLDrawElement tempDrawElement = null;
//			for(int i=0, size=mDirtyDrawElementList.size(); i<size; i++){
//				tempDrawElement = mDirtyDrawElementList.remove(0);
//				tempDrawElement.update(gl);
//				tempDrawPartList.add(tempDrawElement);
//			}
//		}
        synchronized (mDrawElementList) {
            GLDrawElement tempDrawElement = null;
            for (int i = 0, size = tempDrawPartList.size(); i < size; i++) {
                tempDrawElement = tempDrawPartList.get(i);
                mDrawElementList.add(tempDrawElement.getZ(), tempDrawElement);
            }
        }

        LogUtil.i(GLConstant.TAG, ".updateDrawPart<E>");
    }

    @SuppressWarnings("unchecked")
    protected void drawElements(GL10 gl) {
        LogUtil.i(GLConstant.TAG, ".drawElements<S>");

        synchronized (mDrawElementList) {
            mDrawElementList.sort(mComparators[SORT_BY_ASC]);

            List<GLDrawElement> drawElementList = null;
            for (int i = 0, size = mDrawElementList.keySize(); i < size; i++) {
                drawElementList = mDrawElementList.getBySortedIndex(i);
                if (drawElementList != null) {
                    for (int j = 0, sizeJ = drawElementList.size(); j < sizeJ; j++) {
                        drawElementList.get(j).draw();
                    }
                }
            }
        }

        LogUtil.i(GLConstant.TAG, ".drawElements<E>");
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogUtil.i(GLConstant.TAG, ".onSurfaceChanged<S>");

        initialize(gl, width, height);

        if (mObserver != null) {
            mObserver.onSurfaceChanged(gl, width, height);
        }

        synchronized (mDrawElementList) {
            List<GLDrawElement> drawElementList = null;
            for (int i = 0, size = mDrawElementList.keySize(); i < size; i++) {
                drawElementList = mDrawElementList.getBySortedIndex(i);
                if (drawElementList != null) {
                    for (int j = 0, sizeJ = drawElementList.size(); j < sizeJ; j++) {
                        drawElementList.get(j).onSurfaceChanged(gl, width, height);
                    }
                }
            }
        }

        synchronized (mDirtyDrawElementList) {
            for (int i = 0; i < mDirtyDrawElementList.size(); i++) {
                mDirtyDrawElementList.get(i).onSurfaceChanged(gl, width, height);
            }
        }

        requestRender();
        LogUtil.i(GLConstant.TAG, ".onSurfaceChanged<E>");
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtil.i(GLConstant.TAG, ".onSurfaceCreated<S>");

        if (mObserver != null) {
            mObserver.onSurfaceCreated(gl, config);
        }

        synchronized (mDrawElementList) {
            List<GLDrawElement> drawElementList = null;
            for (int i = 0, size = mDrawElementList.keySize(); i < size; i++) {
                drawElementList = mDrawElementList.getBySortedIndex(i);
                if (drawElementList != null) {
                    for (int j = 0, sizeJ = drawElementList.size(); j < sizeJ; j++) {
                        drawElementList.get(j).onSurfaceCreated(gl, config);
                    }
                }
            }
        }

        synchronized (mDirtyDrawElementList) {
            for (int i = 0; i < mDirtyDrawElementList.size(); i++) {
                mDirtyDrawElementList.get(i).onSurfaceCreated(gl, config);
            }
        }

        LogUtil.i(GLConstant.TAG, ".onSurfaceCreated<E>");
    }

    public boolean onTouch(View v, MotionEvent event) {
        boolean ret = false;

//		GLDrawElement actionDrawElement = null;
//		switch(event.getActionMasked()){
//		case MotionEvent.ACTION_DOWN:
//		case MotionEvent.ACTION_POINTER_DOWN:
//			actionDrawElement = isInRect(SORT_BY_DESC, (int)event.getX(), (int)event.getY());
//			if(actionDrawElement != null){
//				ret = actionDrawElement.onTouch(v, event);
//			}
//			mDraggingDrawElementList.put(event.getActionIndex(), actionDrawElement);
//			break;
//		case MotionEvent.ACTION_MOVE:
//			actionDrawElement = mDraggingDrawElementList.get(event.getActionIndex());
//			if(actionDrawElement != null){
//				ret = actionDrawElement.onTouch(v, event);
//			}
//			break;
//		case MotionEvent.ACTION_CANCEL:
//		case MotionEvent.ACTION_OUTSIDE:
//		case MotionEvent.ACTION_UP:
//		case MotionEvent.ACTION_POINTER_UP:
//			actionDrawElement = mDraggingDrawElementList.remove(event.getActionIndex());
//			if(actionDrawElement != null){
//				ret = actionDrawElement.onTouch(v, event);
//			}
//			break;
//		}

        return ret;
    }

    protected void removeNodragElement(MotionEvent e) {
        HashMap<Integer, Object> tempIdMap = new HashMap<Integer, Object>();
        for (int i = 0, size = e.getPointerCount(); i < size; i++) {
            tempIdMap.put(e.getPointerId(i), new Object());
        }

        Set<Integer> idSet = mDraggingDrawElementList.keySet();
        Integer idArray[] = idSet.toArray(new Integer[0]);
        for (int i = 0, size = idArray.length; i < size; i++) {
            if (tempIdMap.remove(idArray[i]) == null) {
                mDraggingDrawElementList.remove(idArray[i]);
            }
        }
    }
}
