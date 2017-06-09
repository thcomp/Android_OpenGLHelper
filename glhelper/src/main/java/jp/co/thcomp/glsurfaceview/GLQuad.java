package jp.co.thcomp.glsurfaceview;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Rect;

public class GLQuad extends GLPolygon {
	private GLVertexFactory.QuadInfo mQuadInfo;

	public GLQuad(GLDrawView view) {
		super(view);

		mQuadInfo = GLVertexFactory.getQuadInfo();
		setVertex(mQuadInfo.outVertices);
		mDrawMode = mQuadInfo.outModeType;
	}

	public void setDrawRect(Rect rectInVP){
		mRectInVP = new Rect3D(rectInVP);

		addTranslate(rectInVP.centerX(), rectInVP.centerY(), 0);
		addScale(rectInVP.width(), rectInVP.height(), 0);
	}

	public void setDrawRect(Rect3D rectInVP){
		mRectInVP = new Rect3D(rectInVP);

		addTranslate(rectInVP.centerX(), rectInVP.centerY(), rectInVP.centerZ());
		addScale(rectInVP.width(), rectInVP.height(), 0);
	}

	@Override
	public void draw() {
		if(mVertex == null){
			float tempVertex[] = mView.getViewSpace().getWRRectFromVPRect(mRectInVP);
			if(tempVertex != null){
				setVertex(tempVertex);
			}
		}
		super.draw();
	}
}
