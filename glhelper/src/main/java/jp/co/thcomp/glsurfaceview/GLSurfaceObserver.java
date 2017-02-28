package jp.co.thcomp.glsurfaceview;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public interface GLSurfaceObserver {
	void onSurfaceChanged(GL10 gl, int width, int height);
	void onSurfaceCreated(GL10 gl, EGLConfig config);
}
