package jp.co.thcomp.glsurfaceview;

public class GLSphere extends GLPolygon {
	private GLVertexFactory.SphereInfo mSphereInfo;

	public GLSphere(GLDrawView view) {
		super(view);

		mSphereInfo = GLVertexFactory.getSphereInfo();
		mDrawMode = mSphereInfo.outModeType;
		setVertex(mSphereInfo.outVertices);
	}

	public void setSphereInfo(int centerX, int centerY, int centerZ, int radius){
		addTranslate(centerX, centerY, centerZ);
		addScale(radius, radius, radius);
	}
//
//	@Override
//	public void draw(GL10 gl) {
//		super.draw(gl);
//	}
}
