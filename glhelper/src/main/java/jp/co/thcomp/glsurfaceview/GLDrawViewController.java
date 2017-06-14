package jp.co.thcomp.glsurfaceview;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

public interface GLDrawViewController {
    // 独自定義のインターフェースは名称は自由。ただし、引数・戻り値は極力"GLxxxxx"(例 GLContext)型を使用すること
    public void initialize(int width, int height);
    public Context getContext();
    public GLContext getGLContext();
    public GLViewSpace getViewSpace();
    public void setSurfaceObserver(GLSurfaceObserver observer);
    public void startRenderer(GLContext glContext, Context context);

    // GLSurfaceViewに存在するメソッドを定義する場合は名称に"Impl"を付与
    public void requestRenderImpl();
    public void setRenderModeImpl(int mode);
}
