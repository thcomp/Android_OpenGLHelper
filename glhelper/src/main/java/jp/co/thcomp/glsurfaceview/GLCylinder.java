package jp.co.thcomp.glsurfaceview;

public class GLCylinder extends GLPolygon {
	private GLVertexFactory.CylinderInfo mCylinderInfo;

	public GLCylinder(GLDrawView view) {
		super(view);

		mCylinderInfo = GLVertexFactory.getCylinderInfo();
		setVertex(mCylinderInfo.outVertices);
		mDrawMode = mCylinderInfo.outModeType;
	}

	public void setCylinderInfo(float centerX, float centerY, float centerZ, float radius, float height){
		addTranslate(centerX, centerY, centerZ);

		switch(mCylinderInfo.inAxis){
		case GLVertexFactory.AxisX:
			addScale(height, radius, radius);
			break;
		case GLVertexFactory.AxisZ:
			addScale(radius, radius, height);
			break;
		case GLVertexFactory.AxisY:
		default:
			addScale(radius, height, radius);
			break;
		}
	}
//
//	@Override
//	public void draw(GL10 gl) {
//		super.draw(gl);
//	}
}
