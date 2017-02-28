package jp.co.thcomp.glsurfaceview;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

public class GLVertexFactory {
	public static final int AxisX = 0;
	public static final int AxisY = 1;
	public static final int AxisZ = 2;

	// Circle
	private static final HashMap<CircleKey, CircleInfo> StoredCircleInfoMap = new HashMap<CircleKey, CircleInfo>();
	private static int DefaultCircleSliceCount = 32;
	private static int DefaultCircleAxis = AxisZ;
	private static final int DefaultCircleModeType = GL10.GL_TRIANGLES;

	// Cube
	private static final HashMap<CubeKey, CubeInfo> StoredCubeInfoMap = new HashMap<CubeKey, CubeInfo>();
	private static final int DefaultCubeModeType = GL10.GL_TRIANGLE_STRIP;

	// Cylinder
	private static final HashMap<CylinderKey, CylinderInfo> StoredCylinderInfoMap = new HashMap<CylinderKey, CylinderInfo>();
	private static final int DefaultCylinderSliceCount = 32;
	private static final float DefaultCylinderHeightWR = -1.0f;
	private static final int DefaultCylinderAxis = AxisY;
	private static final int DefaultCylinderModeType = GL10.GL_TRIANGLE_STRIP;

	// HemiSphere
	private static final HashMap<HemiSphereKey, HemiSphereInfo> StoredHemiSphereInfoMap = new HashMap<HemiSphereKey, HemiSphereInfo>();
	private static int DefaultHemiSphereSliceCount = 3;
	private static int DefaultHemiSphereAxis = AxisY;
	private static final int DefaultHemiSphereModeType = GL10.GL_TRIANGLES;

	// Quad
	private static final HashMap<QuadKey, QuadInfo> StoredQuadInfoMap = new HashMap<QuadKey, QuadInfo>();
	private static int DefaultQuadAxis = AxisZ;
	private static final int DefaultQuadModeType = GL10.GL_TRIANGLE_FAN;

	// Sphere
	private static final HashMap<SphereKey, SphereInfo> StoredSphereInfoMap = new HashMap<SphereKey, SphereInfo>();
	private static int DefaultSphereSliceCount = 3;
	private static final int DefaultSphereModeType = GL10.GL_TRIANGLES;

	private static class CircleKey{
		public int inSliceCount = DefaultCircleSliceCount;
		public int inAxis = DefaultCircleAxis;

		@Override
		public boolean equals(Object o) {
			if(o != null && o instanceof CircleKey){
				CircleKey tempObject = (CircleKey)o;
				if(tempObject.inSliceCount != this.inSliceCount){
					return false;
				}else if(tempObject.inAxis == this.inAxis){
					return true;
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return String.format("CircleKey: slice=%d, axis=%d", inSliceCount, inAxis);
		}
	}

	public static class CircleInfo extends CircleKey{
		public int outModeType = DefaultCircleModeType;
		public float[] outVertices;
		public float[] outNormals;
	}

	private static class CubeKey{
		@Override
		public boolean equals(Object o) {
			if(o != null && o instanceof CubeKey){
				return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return CubeKey.class.getName();
		}
	}

	public static class CubeInfo extends CubeKey{
		public int outModeType = DefaultCubeModeType;
		public float[] outVertices;
		public float[] outNormals;
	}

	private static class CylinderKey{
		public int inSliceCount = DefaultCylinderSliceCount;
		public float inHeightWR = DefaultCylinderHeightWR;
		public int inAxis = DefaultCylinderAxis;

		@Override
		public boolean equals(Object o) {
			if(o != null && o instanceof CylinderKey){
				CylinderKey tempObject = (CylinderKey)o;
				if(tempObject.inSliceCount != this.inSliceCount){
					return false;
				}else if(tempObject.inAxis != this.inAxis){
					return false;
				}else if(tempObject.inHeightWR == this.inHeightWR){
					return true;
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return String.format("CylinderKey: slice=%d, axis=%d, height=%f", inSliceCount, inAxis, inHeightWR);
		}
	}

	public static class CylinderInfo extends CylinderKey{
		public int outModeType = DefaultCylinderModeType;
		public float[] outVertices;
		public float[] outNormals;
		public float[] outTexCoords;
		public int[] outIndices;
	}

	private static class HemiSphereKey{
		public int inSliceCount = DefaultHemiSphereSliceCount;
		public int inAxis = DefaultHemiSphereAxis;

		@Override
		public boolean equals(Object o) {
			if(o != null && o instanceof HemiSphereKey){
				HemiSphereKey tempObject = (HemiSphereKey)o;
				if(tempObject.inSliceCount != this.inSliceCount){
					return false;
				}else if(tempObject.inAxis == this.inAxis){
					return true;
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return String.format("HemiSphereKey: slice=%d, axis=%d", inSliceCount, inAxis);
		}
	}

	public static class HemiSphereInfo extends HemiSphereKey{
		public int outModeType = DefaultHemiSphereModeType;
		public float[] outVertices;
		public float[] outNormals;
	}

	private static class QuadKey{
		public int inAxis = DefaultQuadAxis;

		@Override
		public boolean equals(Object o) {
			if(o != null && o instanceof QuadKey){
				QuadKey tempObject = (QuadKey)o;
				if(tempObject.inAxis == this.inAxis){
					return true;
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return String.format("QuadKey: axis=%d", inAxis);
		}
	}

	public static class QuadInfo extends QuadKey{
		public int outModeType = DefaultQuadModeType;
		public float[] outVertices;
		public float[] outNormals;
	}

	private static class SphereKey{
		public int inSliceCount = DefaultSphereSliceCount;

		@Override
		public boolean equals(Object o) {
			if(o != null && o instanceof SphereKey){
				SphereKey tempObject = (SphereKey)o;
				if(tempObject.inSliceCount == this.inSliceCount){
					return true;
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return String.format("SphereKey: slice=%d", inSliceCount);
		}
	}

	public static class SphereInfo extends SphereKey{
		public int outModeType = DefaultSphereModeType;
		public float[] outVertices;
		public float[] outNormals;
	}

	public static CircleInfo getCircleInfo(){
		return getCircleInfo(null);
	}

	public static CircleInfo getCircleInfo(CircleInfo circleInfo){
		boolean needCreateInfo = false;
		CircleInfo retCircleInfo;

		if(circleInfo != null){
			retCircleInfo = StoredCircleInfoMap.get(circleInfo);
			if(retCircleInfo != null){
				circleInfo.outVertices = retCircleInfo.outVertices;
				circleInfo.outNormals = retCircleInfo.outNormals;
				retCircleInfo = circleInfo;
			}else{
				needCreateInfo = true;
				retCircleInfo = circleInfo;
			}
		}else{
			circleInfo = retCircleInfo = new CircleInfo();
			needCreateInfo = true;
		}

		if(needCreateInfo){
			int sliceCount = circleInfo.inSliceCount;
			int axis = circleInfo.inAxis;

			float circleWidth = 1.0f, circleHeight = 1.0f;
			float halfCircleWidth = circleWidth / 2, halfCircleHeight = circleHeight / 2;
			float sliceWidth = (float)circleWidth / sliceCount;

			float vertex[] = retCircleInfo.outVertices = new float[3 * 3 * 2 * sliceCount];
			float normals[] = retCircleInfo.outNormals = new float[3 * 3 * 2 * sliceCount];

			int posCirclePos1 = 0;
			int posCirclePos2 = 0;
			int posCircleHeight = 0;

			switch(axis){
			case AxisX:
				posCirclePos1 = 1;
				posCirclePos2 = 2;
				posCircleHeight = 0;
				break;
			case AxisZ:
				posCirclePos1 = 0;
				posCirclePos2 = 1;
				posCircleHeight = 2;
				break;
			case AxisY:
			default:
				posCirclePos1 = 0;
				posCirclePos2 = 2;
				posCircleHeight = 1;
				break;
			}

			int position = 0;
			for(int i=0; i<sliceCount; i++, position+=6){
				float pos1 = - halfCircleWidth + i * sliceWidth;
				float pow2 = (float) ((1 - Math.pow(pos1, 2) / Math.pow(halfCircleWidth, 2)) * Math.pow(halfCircleHeight, 2));
				float nextPos1 = pos1 + sliceWidth;
				float nextPow2 = (float) ((1 - Math.pow(nextPos1, 2) / Math.pow(halfCircleWidth, 2)) * Math.pow(halfCircleHeight, 2));

				vertex[position * 3 + posCirclePos1] = 0f;
				vertex[position * 3 + posCirclePos2] = 0f;
				vertex[position * 3 + posCircleHeight] = 0f;
				vertex[(position + 1) * 3 + posCirclePos1] = pos1;
				vertex[(position + 1) * 3 + posCirclePos2] = (float) (Math.sqrt(pow2));
				vertex[(position + 1) * 3 + posCircleHeight] = 0f;
				vertex[(position + 2) * 3 + posCirclePos1] = nextPos1;
				vertex[(position + 2) * 3 + posCirclePos2] = (float) (Math.sqrt(nextPow2));
				vertex[(position + 2) * 3 + posCircleHeight] = 0f;

				vertex[(position + 3) * 3 + posCirclePos1] = 0f;
				vertex[(position + 3) * 3 + posCirclePos2] = 0f;
				vertex[(position + 3) * 3 + posCircleHeight] = 0f;
				vertex[(position + 4) * 3 + posCirclePos1] = pos1;
				vertex[(position + 4) * 3 + posCirclePos2] = (float) (- Math.sqrt(pow2));
				vertex[(position + 4) * 3 + posCircleHeight] = 0f;
				vertex[(position + 5) * 3 + posCirclePos1] = nextPos1;
				vertex[(position + 5) * 3 + posCirclePos2] = (float) (- Math.sqrt(nextPow2));
				vertex[(position + 5) * 3 + posCircleHeight] = 0f;

				normals[position * 3 + posCirclePos1] = 0f;
				normals[position * 3 + posCirclePos2] = 0f;
				normals[position * 3 + posCircleHeight] = 1f;
				normals[(position + 1) * 3 + posCirclePos1] = 0f;
				normals[(position + 1) * 3 + posCirclePos2] = 0f;
				normals[(position + 1) * 3 + posCircleHeight] = 1f;
				normals[(position + 2) * 3 + posCirclePos1] = 0f;
				normals[(position + 2) * 3 + posCirclePos2] = 0f;
				normals[(position + 2) * 3 + posCircleHeight] = 1f;

				normals[(position + 3) * 3 + posCirclePos1] = 0f;
				normals[(position + 3) * 3 + posCirclePos2] = 0f;
				normals[(position + 3) * 3 + posCircleHeight] = 1f;
				normals[(position + 4) * 3 + posCirclePos1] = 0f;
				normals[(position + 4) * 3 + posCirclePos2] = 0f;
				normals[(position + 4) * 3 + posCircleHeight] = 1f;
				normals[(position + 5) * 3 + posCirclePos1] = 0f;
				normals[(position + 5) * 3 + posCirclePos2] = 0f;
				normals[(position + 5) * 3 + posCircleHeight] = 1f;
			}

			StoredCircleInfoMap.put(circleInfo, retCircleInfo);
		}

		return retCircleInfo;
	}

	public static CubeInfo getCubeInfo(){
		return getCubeInfo(null);
	}

	public static CubeInfo getCubeInfo(CubeInfo cubeInfo){
		boolean needCreateInfo = false;
		CubeInfo retCubeInfo;

		if(cubeInfo != null){
			retCubeInfo = StoredCubeInfoMap.get(cubeInfo);
			if(retCubeInfo != null){
				cubeInfo.outVertices = retCubeInfo.outVertices;
				cubeInfo.outNormals = retCubeInfo.outNormals;
				retCubeInfo = cubeInfo;
			}else{
				needCreateInfo = true;
				retCubeInfo = cubeInfo;
			}
		}else{
			cubeInfo = retCubeInfo = new CubeInfo();
			needCreateInfo = true;
		}

		if(needCreateInfo){
			retCubeInfo.outVertices = new float[]{
						-0.5f, -0.5f, -0.5f,	// P1
						-0.5f, 0.5f, -0.5f,	// P2
						0.5f, -0.5f, -0.5f,	// P3
						0.5f, 0.5f, -0.5f,	// P4

						0.5f, 0.5f, -0.5f,	// P4
						0.5f, 0.5f, 0.5f,	// P5
						0.5f, -0.5f, -0.5f,	// P6
						0.5f, -0.5f, 0.5f,	// P7

						0.5f, -0.5f, -0.5f,	// P6
						0.5f, -0.5f, 0.5f,	// P7
						-0.5f, -0.5f, -0.5f,	// P8
						-0.5f, -0.5f, 0.5f,	// P9

						-0.5f, -0.5f, -0.5f,	// P8
						-0.5f, -0.5f, 0.5f,	// P9
						-0.5f, 0.5f, -0.5f,	// P10
						-0.5f, 0.5f, 0.5f,	// P11

						-0.5f, 0.5f, -0.5f,	// P10
						-0.5f, 0.5f, 0.5f,	// P11
						0.5f, 0.5f, -0.5f,	// P12
						0.5f, 0.5f, 0.5f,	// P13

						0.5f, 0.5f, 0.5f,	// P13
						-0.5f, 0.5f, 0.5f,	// P14
						0.5f, -0.5f, 0.5f,	// P15
						-0.5f, -0.5f, 0.5f,	// P16
			};
			retCubeInfo.outNormals = new float[]{
					0f, 0f, 1f,	// P1
					0f, 0f, 1f,	// P2
					0f, 0f, 1f,	// P3
					0f, 0f, 1f,	// P4

					1f, 0f, 0f,	// P4
					1f, 0f, 0f,	// P5
					1f, 0f, 0f,	// P6
					1f, 0f, 0f,	// P7

					0f, -1f, 0f,	// P6
					0f, -1f, 0f,	// P7
					0f, -1f, 0f,	// P8
					0f, -1f, 0f,	// P9

					-1f, 0f, 0f,	// P8
					-1f, 0f, 0f,	// P9
					-1f, 0f, 0f,	// P10
					-1f, 0f, 0f,	// P11

					0f, 1f, 0f,	// P10
					0f, 1f, 0f,	// P11
					0f, 1f, 0f,	// P12
					0f, 1f, 0f,	// P13

					0f, 0f, 1f,	// P13
					0f, 0f, 1f,	// P14
					0f, 0f, 1f,	// P15
					0f, 0f, 1f,	// P16
			};

			StoredCubeInfoMap.put(cubeInfo, retCubeInfo);
		}

		return retCubeInfo;
	}

	public static CylinderInfo getCylinderInfo(){
		return getCylinderInfo(null);
	}

	public static CylinderInfo getCylinderInfo(CylinderInfo cylinderInfo){
		boolean needCreateInfo = false;
		CylinderInfo retCylinderInfo;

		if(cylinderInfo != null){
			retCylinderInfo = StoredCylinderInfoMap.get(cylinderInfo);
			if(retCylinderInfo != null){
				cylinderInfo.outVertices = retCylinderInfo.outVertices;
				cylinderInfo.outNormals = retCylinderInfo.outNormals;
				cylinderInfo.outTexCoords = retCylinderInfo.outTexCoords;
				cylinderInfo.outIndices = retCylinderInfo.outIndices;
				retCylinderInfo = cylinderInfo;
			}else{
				needCreateInfo = true;
				retCylinderInfo = cylinderInfo;
			}
		}else{
			cylinderInfo = retCylinderInfo = new CylinderInfo();
			needCreateInfo = true;
		}

		if(needCreateInfo){
			int sliceCount = cylinderInfo.inSliceCount;
			int axis = cylinderInfo.inAxis;
			float heightWR = cylinderInfo.inHeightWR;

			float angleStep = (float) (2 * Math.PI / sliceCount);

			float vertex[] = retCylinderInfo.outVertices = new float[(sliceCount + 1) * 3 * 2];
			float normals[] = retCylinderInfo.outNormals = new float[(sliceCount + 1) * 3 * 2];

			int posCircleCos = 0;
			int posCircleSin = 0;
			int posCylinderHeight = 0;

			switch(axis){
			case AxisX:
				posCircleCos = 1;
				posCircleSin = 2;
				posCylinderHeight = 0;
				break;
			case AxisZ:
				posCircleCos = 0;
				posCircleSin = 1;
				posCylinderHeight = 2;
				break;
			case AxisY:
			default:
				posCircleCos = 0;
				posCircleSin = 2;
				posCylinderHeight = 1;
				break;
			}

			int j=0;
			for(int i=0; i<sliceCount; i++, j+=2){
				vertex[j * 3 + posCircleSin] = (float) Math.cos(angleStep * i);
				vertex[j * 3 + posCircleCos] = (float) Math.sin(angleStep * i);
				vertex[j * 3 + posCylinderHeight] = 0;
				vertex[(j + 1) * 3 + posCircleSin] = (float) Math.cos(angleStep * i);
				vertex[(j + 1) * 3 + posCircleCos] = (float) Math.sin(angleStep * i);
				vertex[(j + 1) * 3 + posCylinderHeight] = heightWR;

				normals[j * 3 + posCircleSin] = (float) Math.cos(angleStep * i);
				normals[j * 3 + posCircleCos] = (float) Math.sin(angleStep * i);
				normals[j * 3 + posCylinderHeight] = 0;
				normals[(j + 1) * 3 + posCircleSin] = (float) Math.cos(angleStep * i);
				normals[(j + 1) * 3 + posCircleCos] = (float) Math.sin(angleStep * i);
				normals[(j + 1) * 3 + posCylinderHeight] = 0;
			}

			vertex[j * 3 + posCircleSin] = (float) Math.cos(0);
			vertex[j * 3 + posCircleCos] = (float) Math.sin(0);
			vertex[j * 3 + posCylinderHeight] = 0;
			vertex[(j + 1) * 3 + posCircleSin] = (float) Math.cos(0);
			vertex[(j + 1) * 3 + posCircleCos] = (float) Math.sin(0);
			vertex[(j + 1) * 3 + posCylinderHeight] = heightWR;

			normals[j * 3 + posCircleSin] = (float) Math.cos(0);
			normals[j * 3 + posCircleCos] = (float) Math.sin(0);
			normals[j * 3 + posCylinderHeight] = 0;
			normals[(j + 1) * 3 + posCircleSin] = (float) Math.cos(0);
			normals[(j + 1) * 3 + posCircleCos] = (float) Math.sin(0);
			normals[(j + 1) * 3 + posCylinderHeight] = 0;

			StoredCylinderInfoMap.put(cylinderInfo, retCylinderInfo);
		}

		return retCylinderInfo;
	}

	public static HemiSphereInfo getHemiSphereInfo(){
		return getHemiSphereInfo(null);
	}

	public static HemiSphereInfo getHemiSphereInfo(HemiSphereInfo hemiSphereInfo){
		boolean needCreateInfo = false;
		HemiSphereInfo retHemiSphereInfo;

		if(hemiSphereInfo != null){
			retHemiSphereInfo = StoredHemiSphereInfoMap.get(hemiSphereInfo);
			if(retHemiSphereInfo != null){
				hemiSphereInfo.outVertices = retHemiSphereInfo.outVertices;
				hemiSphereInfo.outNormals = retHemiSphereInfo.outNormals;
				retHemiSphereInfo = hemiSphereInfo;
			}else{
				needCreateInfo = true;
				retHemiSphereInfo = hemiSphereInfo;
			}
		}else{
			hemiSphereInfo = retHemiSphereInfo = new HemiSphereInfo();
			needCreateInfo = true;
		}

		if(needCreateInfo){
			int sliceCount = hemiSphereInfo.inSliceCount;
			int axis = hemiSphereInfo.inAxis;

			int multiSliceCount = 2;
			float radiuses[] = {0.5f};
			float angleZStep = (float) (Math.PI / 2 / sliceCount);
			int circleSliceCount = sliceCount * multiSliceCount;
			int arraySize = sliceCount * multiSliceCount;

			float tempCircleSliceCount = circleSliceCount;
			for(int i=1; i<sliceCount; i++){
				tempCircleSliceCount *= multiSliceCount;
				arraySize += (1.5f * tempCircleSliceCount);
			}

			float vertex[] = retHemiSphereInfo.outVertices = retHemiSphereInfo.outNormals = new float[3 * 3 * arraySize];

			int posCircleCos = 0;
			int posCircleSin = 0;
			int posHemiSphereHeight = 0;

			switch(axis){
			case AxisX:
				posCircleCos = 1;
				posCircleSin = 2;
				posHemiSphereHeight = 0;
				break;
			case AxisZ:
				posCircleCos = 0;
				posCircleSin = 1;
				posHemiSphereHeight = 2;
				break;
			case AxisY:
			default:
				posCircleCos = 0;
				posCircleSin = 2;
				posHemiSphereHeight = 1;
				break;
			}

			int position = 0;
			for(int base=0, baseSize=radiuses.length; base<baseSize; base++){
				float currentPoints[][] = {
						{radiuses[base], 0f, 0f},
						{0f, radiuses[base], 0f},
						{0f, 0f, radiuses[base]},
				};
				circleSliceCount = sliceCount * multiSliceCount;
				float nextPoints[][] = null;
				for(int i=0; i<sliceCount; i++){
					float nextZPos = (float) (Math.cos(angleZStep * (i + 1)));
					float xyRadius = (float) (Math.sin(angleZStep * (i + 1)));
					float angleXYStep = (float) (2 * Math.PI / circleSliceCount);
					nextPoints = new float[circleSliceCount][3];
					for(int j=0; j<circleSliceCount; j++){
						nextPoints[j][posCircleCos] = (float)(xyRadius * Math.cos(angleXYStep * j));
						nextPoints[j][posCircleSin] = (float)(xyRadius * Math.sin(angleXYStep * j));
						nextPoints[j][posHemiSphereHeight] = nextZPos;
					}

					if(i==0){
						for(int j=0; j<circleSliceCount; j++){
							int nextPos = (j+1)%circleSliceCount;
							vertex[posCircleCos + (position * 3)] = (float)currentPoints[axis][posCircleCos];
							vertex[posCircleSin + (position * 3)] = (float)currentPoints[axis][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = (float)currentPoints[axis][posHemiSphereHeight];
							position++;
							vertex[posCircleCos + (position * 3)] = nextPoints[j][posCircleCos];
							vertex[posCircleSin + (position * 3)] = nextPoints[j][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = nextPoints[j][posHemiSphereHeight];
							position++;
							vertex[posCircleCos + (position * 3)] = nextPoints[nextPos][posCircleCos];
							vertex[posCircleSin + (position * 3)] = nextPoints[nextPos][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = nextPoints[nextPos][posHemiSphereHeight];
							position++;
						}
					}else{
						for(int j=0, sizeJ=currentPoints.length; j<sizeJ; j++){
							int curNextPosJ = (j+1)%sizeJ;
							int nxtNextPosJ = (j*2+1)%nextPoints.length;
							int nxtNextNextPosJ = (j*2+2)%nextPoints.length;

							vertex[posCircleCos + (position * 3)] = currentPoints[j][posCircleCos];
							vertex[posCircleSin + (position * 3)] = currentPoints[j][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = currentPoints[j][posHemiSphereHeight];
							position++;
							vertex[posCircleCos + (position * 3)] = nextPoints[j*2][posCircleCos];
							vertex[posCircleSin + (position * 3)] = nextPoints[j*2][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = nextPoints[j*2][posHemiSphereHeight];
							position++;
							vertex[posCircleCos + (position * 3)] = nextPoints[nxtNextPosJ][posCircleCos];
							vertex[posCircleSin + (position * 3)] = nextPoints[nxtNextPosJ][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = nextPoints[nxtNextPosJ][posHemiSphereHeight];
							position++;

							vertex[posCircleCos + (position * 3)] = currentPoints[j][posCircleCos];
							vertex[posCircleSin + (position * 3)] = currentPoints[j][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = currentPoints[j][posHemiSphereHeight];
							position++;
							vertex[posCircleCos + (position * 3)] = currentPoints[curNextPosJ][posCircleCos];
							vertex[posCircleSin + (position * 3)] = currentPoints[curNextPosJ][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = currentPoints[curNextPosJ][posHemiSphereHeight];
							position++;
							vertex[posCircleCos + (position * 3)] = nextPoints[nxtNextPosJ][posCircleCos];
							vertex[posCircleSin + (position * 3)] = nextPoints[nxtNextPosJ][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = nextPoints[nxtNextPosJ][posHemiSphereHeight];
							position++;

							vertex[posCircleCos + (position * 3)] = currentPoints[curNextPosJ][posCircleCos];
							vertex[posCircleSin + (position * 3)] = currentPoints[curNextPosJ][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = currentPoints[curNextPosJ][posHemiSphereHeight];
							position++;
							vertex[posCircleCos + (position * 3)] = nextPoints[nxtNextPosJ][posCircleCos];
							vertex[posCircleSin + (position * 3)] = nextPoints[nxtNextPosJ][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = nextPoints[nxtNextPosJ][posHemiSphereHeight];
							position++;
							vertex[posCircleCos + (position * 3)] = nextPoints[nxtNextNextPosJ][posCircleCos];
							vertex[posCircleSin + (position * 3)] = nextPoints[nxtNextNextPosJ][posCircleSin];
							vertex[posHemiSphereHeight + (position * 3)] = nextPoints[nxtNextNextPosJ][posHemiSphereHeight];
							position++;
						}
					}

					circleSliceCount *= multiSliceCount;
					currentPoints = nextPoints;
				}
			}

			StoredHemiSphereInfoMap.put(hemiSphereInfo, retHemiSphereInfo);
		}

		return retHemiSphereInfo;
	}

	public static QuadInfo getQuadInfo(){
		return getQuadInfo(null);
	}

	public static QuadInfo getQuadInfo(QuadInfo quadInfo){
		boolean needCreateInfo = false;
		QuadInfo retQuadInfo;

		if(quadInfo != null){
			retQuadInfo = StoredQuadInfoMap.get(quadInfo);
			if(retQuadInfo != null){
				quadInfo.outVertices = retQuadInfo.outVertices;
				quadInfo.outNormals = retQuadInfo.outNormals;
				retQuadInfo = quadInfo;
			}else{
				needCreateInfo = true;
				retQuadInfo = quadInfo;
			}
		}else{
			quadInfo = retQuadInfo = new QuadInfo();
			needCreateInfo = true;
		}

		if(needCreateInfo){
			int axis = quadInfo.inAxis;

			float vertex[] = retQuadInfo.outVertices = new float[3 * 4];
			float normals[] = retQuadInfo.outNormals = new float[3 * 4];

			int posZero = 0;
			int posPoint1 = 0;
			int posPoint2 = 0;

			switch(axis){
			case AxisX:
				posZero = 0;
				posPoint1 = 1;
				posPoint2 = 2;
				break;
			case AxisY:
				posZero = 1;
				posPoint1 = 0;
				posPoint2 = 2;
				break;
			case AxisZ:
				posZero = 2;
				posPoint1 = 0;
				posPoint2 = 1;
				break;
			}

			float vertex2D[][] = {
					{-0.5f, 0.5f, 0.5f, -0.5f},
					{0.5f, 0.5f, -0.5f, -0.5f},
			};
			for(int i=0, size=vertex2D[0].length; i<size; i++){
				vertex[i*3 + posZero] = 0f;
				vertex[i*3 + posPoint1] = vertex2D[0][i];
				vertex[i*3 + posPoint2] = vertex2D[1][i];
				normals[i*3 + posZero] = 1f;
				normals[i*3 + posPoint1] = 0f;
				normals[i*3 + posPoint2] = 0f;
			}

			StoredQuadInfoMap.put(quadInfo, retQuadInfo);
		}

		return retQuadInfo;
	}

	public static SphereInfo getSphereInfo(){
		return getSphereInfo(null);
	}

	public static SphereInfo getSphereInfo(SphereInfo sphereInfo){
		boolean needCreateInfo = false;
		SphereInfo retSphereInfo;

		if(sphereInfo != null){
			retSphereInfo = StoredSphereInfoMap.get(sphereInfo);
			if(retSphereInfo != null){
				sphereInfo.outVertices = retSphereInfo.outVertices;
				sphereInfo.outNormals = retSphereInfo.outNormals;
				retSphereInfo = sphereInfo;
			}else{
				needCreateInfo = true;
				retSphereInfo = sphereInfo;
			}
		}else{
			sphereInfo = retSphereInfo = new SphereInfo();
			needCreateInfo = true;
		}

		if(needCreateInfo){
			int sliceCount = sphereInfo.inSliceCount;
			int multiSliceCount = 2;
			float radiuses[] = {0.5f, -0.5f};
			float angleZStep = (float) (Math.PI / 2 / sliceCount);
			int angleXYSliceCount = sliceCount * multiSliceCount;
			int arraySize = sliceCount * multiSliceCount;

			float tempAngleXYSliceCount = angleXYSliceCount;
			for(int i=1; i<sliceCount; i++){
				tempAngleXYSliceCount *= multiSliceCount;
				arraySize += (1.5f * tempAngleXYSliceCount);
			}

			float vertex[] = retSphereInfo.outVertices = retSphereInfo.outNormals = new float[3 * 3 * 2 * arraySize];

			int position = 0;
			for(int base=0, baseSize=radiuses.length; base<baseSize; base++){
				float currentPoints[][] = {
						{0f, 0f, radiuses[base]},
				};
				angleXYSliceCount = sliceCount * multiSliceCount;
				float nextPoints[][] = null;
				for(int i=0; i<sliceCount; i++){
					float nextZPos = (float) (Math.cos(angleZStep * (i + 1)));
					float xyRadius = (float) (Math.sin(angleZStep * (i + 1)));
					float angleXYStep = (float) (2 * Math.PI / angleXYSliceCount);
					nextPoints = new float[angleXYSliceCount][3];
					for(int j=0; j<angleXYSliceCount; j++){
						nextPoints[j][0] = (float)(xyRadius * Math.cos(angleXYStep * j));
						nextPoints[j][1] = (float)(xyRadius * Math.sin(angleXYStep * j));
						nextPoints[j][2] = nextZPos;
					}

					if(i==0){
						for(int j=0; j<angleXYSliceCount; j++){
							int nextPos = (j+1)%angleXYSliceCount;
							vertex[position++] = (float)currentPoints[0][0];
							vertex[position++] = (float)currentPoints[0][1];
							vertex[position++] = (float)currentPoints[0][2];
							vertex[position++] = nextPoints[j][0];
							vertex[position++] = nextPoints[j][1];
							vertex[position++] = nextPoints[j][2];
							vertex[position++] = nextPoints[nextPos][0];
							vertex[position++] = nextPoints[nextPos][1];
							vertex[position++] = nextPoints[nextPos][2];
						}
					}else{
						for(int j=0, sizeJ=currentPoints.length; j<sizeJ; j++){
							int curNextPosJ = (j+1)%sizeJ;
							int nxtNextPosJ = (j*2+1)%nextPoints.length;
							int nxtNextNextPosJ = (j*2+2)%nextPoints.length;

							vertex[position++] = (float)currentPoints[j][0];
							vertex[position++] = (float)currentPoints[j][1];
							vertex[position++] = (float)currentPoints[j][2];
							vertex[position++] = (float)nextPoints[j*2][0];
							vertex[position++] = (float)nextPoints[j*2][1];
							vertex[position++] = (float)nextPoints[j*2][2];
							vertex[position++] = (float)nextPoints[nxtNextPosJ][0];
							vertex[position++] = (float)nextPoints[nxtNextPosJ][1];
							vertex[position++] = (float)nextPoints[nxtNextPosJ][2];

							vertex[position++] = (float)currentPoints[j][0];
							vertex[position++] = (float)currentPoints[j][1];
							vertex[position++] = (float)currentPoints[j][2];
							vertex[position++] = (float)currentPoints[curNextPosJ][0];
							vertex[position++] = (float)currentPoints[curNextPosJ][1];
							vertex[position++] = (float)currentPoints[curNextPosJ][2];
							vertex[position++] = (float)nextPoints[nxtNextPosJ][0];
							vertex[position++] = (float)nextPoints[nxtNextPosJ][1];
							vertex[position++] = (float)nextPoints[nxtNextPosJ][2];

							vertex[position++] = (float)currentPoints[curNextPosJ][0];
							vertex[position++] = (float)currentPoints[curNextPosJ][1];
							vertex[position++] = (float)currentPoints[curNextPosJ][2];
							vertex[position++] = (float)nextPoints[nxtNextPosJ][0];
							vertex[position++] = (float)nextPoints[nxtNextPosJ][1];
							vertex[position++] = (float)nextPoints[nxtNextPosJ][2];
							vertex[position++] = (float)nextPoints[nxtNextNextPosJ][0];
							vertex[position++] = (float)nextPoints[nxtNextNextPosJ][1];
							vertex[position++] = (float)nextPoints[nxtNextNextPosJ][2];
						}
					}

					angleXYSliceCount *= multiSliceCount;
					currentPoints = nextPoints;
				}
			}

			StoredSphereInfoMap.put(sphereInfo, retSphereInfo);
		}

		return retSphereInfo;
	}

}
