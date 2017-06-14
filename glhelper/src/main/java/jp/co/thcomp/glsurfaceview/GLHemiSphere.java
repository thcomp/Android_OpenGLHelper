package jp.co.thcomp.glsurfaceview;

public class GLHemiSphere extends GLPolygon {
	private GLVertexFactory.HemiSphereInfo mHemiSphereInfo;

	public GLHemiSphere(GLDrawViewController view) {
		super(view);

		mHemiSphereInfo = GLVertexFactory.getHemiSphereInfo();
		setVertex(mHemiSphereInfo.outVertices);
		mDrawMode = mHemiSphereInfo.outModeType;
	}

	public void setSphereInfo(float centerX, float centerY, float centerZ, float radius){
		addTranslate(centerX, centerY, centerZ);
		addScale(radius, radius, radius);
	}
}
