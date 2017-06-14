package jp.co.thcomp.glsurfaceview;

import javax.microedition.khronos.egl.EGLConfig;

public interface GLSurfaceObserver {
	void onSurfaceChanged(int width, int height);
	void onSurfaceCreated(EGLConfig config);
}
