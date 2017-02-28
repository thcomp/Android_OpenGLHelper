package jp.co.thcomp.glsurfaceview;

public class GLCube extends GLPolygon {
	private GLVertexFactory.CubeInfo mCubeInfo;

	public GLCube(GLDrawView view) {
		super(view);

		mCubeInfo = GLVertexFactory.getCubeInfo();
		setVertex(mCubeInfo.outVertices);
		mDrawMode = mCubeInfo.outModeType;
	}

	public void setCubeInfo(int centerX, int centerY, int centerZ, int width, int height, int length){
		addTranslate(centerX, centerY, centerZ);
		addScale(width, height, length);
	}
}
