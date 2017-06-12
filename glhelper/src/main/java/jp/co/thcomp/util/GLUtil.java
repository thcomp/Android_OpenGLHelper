package jp.co.thcomp.util;

import android.opengl.GLES10;
import android.opengl.GLU;
import android.opengl.Matrix;

import java.lang.reflect.Field;

public class GLUtil {
    private static final String TAG = GLUtil.class.getSimpleName();

    public static void gluLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        Field scratchField = null;

        try {
            scratchField = GLU.class.getDeclaredField("sScratch");
            scratchField.setAccessible(true);
            float[] scratch = (float[]) scratchField.get(null);

            synchronized (scratch) {
                Matrix.setLookAtM(scratch, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ,
                        upX, upY, upZ);
                GLES10.glMultMatrixf(scratch, 0);
            }
        } catch (NoSuchFieldException e) {
            LogUtil.exception(TAG, e);
        } catch (IllegalAccessException e) {
            LogUtil.exception(TAG, e);
        }
    }
}
