package jp.co.thcomp.util;

import java.util.ArrayList;

import jp.co.thcomp.glsurfaceview.GLContext;
import jp.co.thcomp.glsurfaceview.GLViewSpace;

public class OpenGLESUtils {
	public static class FanInfo{
		public float inSliceAngle;
		public int inCenterX;
		public int inCenterY;
		public int inZOrder;
		public float inAngle;
		public int inRadius;
		public float[] outVertices;
		public float[] outNormals;
	}

	public static class CircleInfo{
		public int inNumSlices;
		public int inCenterX;
		public int inCenterY;
		public int inZOrder;
		public int inCircleWidth;
		public int inCircleHeight;
		public float[] outVertices;
		public float[] outNormals;
	}

	public static class CubeInfo{
		public int inCenterX;
		public int inCenterY;
		public int inCenterZ;
		public int inWidth;
		public int inHeight;
		public int inLength;
		public float[][] outVertices;
		public float[][] outNormals;
		public float[][] outNormalVectors;
	}

	public static class SphereInfo{
		public int inNumSlices;
		public int inRadius;
		public int inCenterX;
		public int inCenterY;
		public int inCenterZ;
		public float[] outVertices;
		public float[] outNormals;
		public float[] outTexCoords;
		public int[] outIndices;
	}

	public static class CylinderInfo{
		public int inNumSlices;
		public int inNumStacks;
		public int inRadius;
		public int inCenterX;
		public int inCenterY;
		public int inCenterZ;
		public int inHeight;
		public float[] outVertices;
		public float[] outNormals;
		public float[] outTexCoords;
		public int[] outIndices;
	}

	public static int esGenFan(GLContext glContext, FanInfo info){
		GLViewSpace viewSpace = glContext.getViewSpace();
		int centerX = info.inCenterX, centerY = info.inCenterY, zOrder = info.inZOrder, radius = info.inRadius;
		float angle = info.inAngle, sliceAngle = info.inSliceAngle;
		float wrCenterX = viewSpace.changeViewPortXtoWorldReferenceX(centerX);
		float wrCenterY = viewSpace.changeViewPortYtoWorldReferenceY(centerY);
		int direction = -1;

		if(angle < 0){
			direction = 1;
			angle *= (-1);
		}

		// Allocate memory for buffers
		float[] vertices = info.outVertices = new float[(int)Math.ceil(3 * 3 * angle / sliceAngle)];
		float[] normals = info.outNormals = new float[(int)Math.ceil(3 * 3 * angle / sliceAngle)];

		int countV = 0;
		int countN = 0;
		for(int i=0; sliceAngle*i<angle; i++){
			float currentAngle = (float) (sliceAngle * i * 2 * Math.PI / 360);
			float nextAngle = (float) (sliceAngle * (i + 1) * 2 * Math.PI / 360);
			float normalPosX = (float) (radius * Math.cos(currentAngle));
			float normalPosY = (float) (radius * Math.sin(currentAngle));
			float nextNormalPosX = (float) (radius * Math.cos(nextAngle));
			float nextNormalPosY = (float) (radius * Math.sin(nextAngle));

			vertices[countV++] = centerX;
			vertices[countV++] = centerY;
			vertices[countV++] = zOrder;
			vertices[countV++] = normalPosX + centerX;
			vertices[countV++] = (direction * normalPosY) + centerY;
			vertices[countV++] = zOrder;
			vertices[countV++] = nextNormalPosX + centerX;
			vertices[countV++] = (direction * nextNormalPosY) + centerY;
			vertices[countV++] = zOrder;

			normals[countN++] = wrCenterX;
			normals[countN++] = wrCenterY;
			normals[countN++] = zOrder;
			normals[countN++] = viewSpace.changeViewPortXtoWorldReferenceX((int) (normalPosX + centerX));
			normals[countN++] = viewSpace.changeViewPortYtoWorldReferenceY((int) ((direction * normalPosY) + centerY));
			normals[countN++] = zOrder;
			normals[countN++] = viewSpace.changeViewPortXtoWorldReferenceX((int) (nextNormalPosX + centerX));
			normals[countN++] = viewSpace.changeViewPortYtoWorldReferenceY((int) ((direction * nextNormalPosY) + centerY));
			normals[countN++] = zOrder;
		}

		return vertices.length;
	}

	public static int esGenCircle(GLContext glContext, CircleInfo info){
		GLViewSpace viewSpace = glContext.getViewSpace();
		int centerX = info.inCenterX, centerY = info.inCenterY, zOrder = info.inZOrder;
		int circleWidth = info.inCircleWidth, circleHeight = info.inCircleHeight;
		float halfCircleWidth = (float)circleWidth / 2, halfCircleHeight = (float)circleHeight / 2;
		float wrCenterX = viewSpace.changeViewPortXtoWorldReferenceX(centerX);
		float wrCenterY = viewSpace.changeViewPortYtoWorldReferenceY(centerY);
		int numSlices = info.inNumSlices;
		float sliceWidth = (float)circleWidth / numSlices;
		float startNormalPosX = - halfCircleWidth;

		// Allocate memory for buffers
		float[] vertices = info.outVertices = new float[3 * 3 * 2 * numSlices];
		float[] normals = info.outNormals = new float[3 * 3 * 2* numSlices];
		//float[] texCoords = info.outTexCoords = new float[3 * 3* 2 * (numSlices - 1)];
		//int [] indices = info.outIndices = new int[3 * 3* numSlices];

		int countV = 0;
		int countN = 0;
		for(int i=0; i<numSlices; i++){
			float normalPosX = startNormalPosX + i * sliceWidth;
			float powY = (float) ((1 - Math.pow(normalPosX, 2) / Math.pow(halfCircleWidth, 2)) * Math.pow(halfCircleHeight, 2));
			float nextNormalPosX = startNormalPosX + (i + 1) * sliceWidth;
			float nextPowY = (float) ((1 - Math.pow(nextNormalPosX, 2) / Math.pow(halfCircleWidth, 2)) * Math.pow(halfCircleHeight, 2));

			vertices[countV++] = centerX;
			vertices[countV++] = centerY;
			vertices[countV++] = zOrder;
			vertices[countV++] = normalPosX + centerX;
			vertices[countV++] = (float) (Math.sqrt(powY) + centerY);
			vertices[countV++] = zOrder;
			vertices[countV++] = nextNormalPosX + centerX;
			vertices[countV++] = (float) (Math.sqrt(nextPowY) + centerY);
			vertices[countV++] = zOrder;

			vertices[countV++] = centerX;
			vertices[countV++] = centerY;
			vertices[countV++] = zOrder;
			vertices[countV++] = normalPosX + centerX;
			vertices[countV++] = (float) (- Math.sqrt(powY) + centerY);
			vertices[countV++] = zOrder;
			vertices[countV++] = nextNormalPosX + centerX;
			vertices[countV++] = zOrder;

			normals[countN++] = wrCenterX;
			normals[countN++] = wrCenterY;
			normals[countN++] = zOrder;
			normals[countN++] = viewSpace.changeViewPortXtoWorldReferenceX((int) (normalPosX + centerX));
			normals[countN++] = viewSpace.changeViewPortYtoWorldReferenceY((int) (Math.sqrt(powY) + centerY));
			normals[countN++] = zOrder;
			normals[countN++] = viewSpace.changeViewPortXtoWorldReferenceX((int) nextNormalPosX + centerX);
			normals[countN++] = viewSpace.changeViewPortYtoWorldReferenceY((int) (Math.sqrt(nextPowY) + centerY));
			normals[countN++] = zOrder;

			normals[countN++] = wrCenterX;
			normals[countN++] = wrCenterY;
			normals[countN++] = zOrder;
			normals[countN++] = viewSpace.changeViewPortXtoWorldReferenceX((int) normalPosX + centerX);
			normals[countN++] = viewSpace.changeViewPortYtoWorldReferenceY((int) (- Math.sqrt(powY) + centerY));
			normals[countN++] = zOrder;
			normals[countN++] = viewSpace.changeViewPortXtoWorldReferenceX((int) nextNormalPosX + centerX);
			normals[countN++] = viewSpace.changeViewPortYtoWorldReferenceY((int) (- Math.sqrt(nextPowY) + centerY));
			normals[countN++] = zOrder;
		}

		return 3 * 2 * numSlices;
	}

	public static int esGenCube(GLContext glContext, CubeInfo info){
		GLViewSpace viewSpace = glContext.getViewSpace();
		float centerX = info.inCenterX, centerY = info.inCenterY, centerZ = info.inCenterZ;
		float halfWidth = (float)info.inWidth / 2, halfHeight = (float)info.inHeight / 2, halfLength = (float)info.inLength / 2;

		// Allocate memory for buffers
//		float[] vertices = info.outVertices = new float[]{
//				centerX - halfWidth, centerY - halfHeight, centerZ - halfLength,	// P1
//				centerX - halfWidth, centerY + halfHeight, centerZ - halfLength,	// P2
//				centerX + halfWidth, centerY - halfHeight, centerZ - halfLength,	// P3
//				centerX + halfWidth, centerY + halfHeight, centerZ - halfLength,	// P4
//				centerX + halfWidth, centerY + halfHeight, centerZ + halfLength,	// P5
//				centerX + halfWidth, centerY - halfHeight, centerZ - halfLength,	// P6
//				centerX + halfWidth, centerY - halfHeight, centerZ + halfLength,	// P7
//				centerX - halfWidth, centerY - halfHeight, centerZ - halfLength,	// P8
//				centerX - halfWidth, centerY - halfHeight, centerZ + halfLength,	// P9
//				centerX - halfWidth, centerY + halfHeight, centerZ - halfLength,	// P10
//				centerX - halfWidth, centerY + halfHeight, centerZ + halfLength,	// P11
//				centerX + halfWidth, centerY + halfHeight, centerZ - halfLength,	// P12
//				centerX + halfWidth, centerY + halfHeight, centerZ + halfLength,	// P13
//				centerX - halfWidth, centerY + halfHeight, centerZ + halfLength,	// P14
//				centerX + halfWidth, centerY - halfHeight, centerZ + halfLength,	// P15
//				centerX - halfWidth, centerY - halfHeight, centerZ + halfLength,	// P16
//		};
//
//		float[] normals = info.outNormals = new float[vertices.length];
//		for(int i=0, size=vertices.length/3; i<size; i++){
//			normals[i * 3 + 0] = viewSpace.changeViewPortXtoWorldReferenceX((int) vertices[i * 3  + 0]);
//			normals[i * 3  + 1] = viewSpace.changeViewPortYtoWorldReferenceY((int) vertices[i * 3  + 1]);
//			normals[i * 3  + 2] = viewSpace.changeViewPortZtoWorldReferenceZ((int) vertices[i * 3  + 2]);
//			//normals[i * 3  + 2] = 0.0f;
//		}
//
//		return vertices.length;
		float[][] vertices = info.outVertices = new float[][]{
				{
					centerX - halfWidth, centerY - halfHeight, centerZ - halfLength,	// P1
					centerX - halfWidth, centerY + halfHeight, centerZ - halfLength,	// P2
					centerX + halfWidth, centerY - halfHeight, centerZ - halfLength,	// P3
					centerX + halfWidth, centerY + halfHeight, centerZ - halfLength,	// P4
				},
				{
					centerX + halfWidth, centerY + halfHeight, centerZ - halfLength,	// P4
					centerX + halfWidth, centerY + halfHeight, centerZ + halfLength,	// P5
					centerX + halfWidth, centerY - halfHeight, centerZ - halfLength,	// P6
					centerX + halfWidth, centerY - halfHeight, centerZ + halfLength,	// P7
				},
				{
					centerX + halfWidth, centerY - halfHeight, centerZ - halfLength,	// P6
					centerX + halfWidth, centerY - halfHeight, centerZ + halfLength,	// P7
					centerX - halfWidth, centerY - halfHeight, centerZ - halfLength,	// P8
					centerX - halfWidth, centerY - halfHeight, centerZ + halfLength,	// P9
				},
				{
					centerX - halfWidth, centerY - halfHeight, centerZ - halfLength,	// P8
					centerX - halfWidth, centerY - halfHeight, centerZ + halfLength,	// P9
					centerX - halfWidth, centerY + halfHeight, centerZ - halfLength,	// P10
					centerX - halfWidth, centerY + halfHeight, centerZ + halfLength,	// P11
				},
				{
					centerX - halfWidth, centerY + halfHeight, centerZ - halfLength,	// P10
					centerX - halfWidth, centerY + halfHeight, centerZ + halfLength,	// P11
					centerX + halfWidth, centerY + halfHeight, centerZ - halfLength,	// P12
					centerX + halfWidth, centerY + halfHeight, centerZ + halfLength,	// P13
				},
				{
					centerX + halfWidth, centerY + halfHeight, centerZ + halfLength,	// P13
					centerX - halfWidth, centerY + halfHeight, centerZ + halfLength,	// P14
					centerX + halfWidth, centerY - halfHeight, centerZ + halfLength,	// P15
					centerX - halfWidth, centerY - halfHeight, centerZ + halfLength,	// P16
				},
		};
		info.outNormalVectors = new float[][]{
				{
					0.0f, 0.0f, -1.0f,
				},
				{
					1.0f, 0.0f, 0.0f,
				},
				{
					0.0f, -1.0f, 0.0f,
				},
				{
					-1.0f, 0.0f, 0.0f,
				},
				{
					0.0f, 1.0f, 0.0f,
				},
				{
					0.0f, 0.0f, 1.0f,
				},
		};

		float[][] normals = info.outNormals = new float[vertices.length][];
		for(int i=0, sizeI=vertices.length; i<sizeI; i++){
			normals[i] = new float[vertices[i].length];
			for(int j=0, sizeJ=normals[i].length/3; j<sizeJ; j++){
				normals[i][j * 3 + 0] = viewSpace.changeViewPortXtoWorldReferenceX((int) vertices[i][j * 3  + 0]);
				normals[i][j * 3 + 1] = viewSpace.changeViewPortYtoWorldReferenceY((int) vertices[i][j * 3  + 1]);
				normals[i][j * 3 + 2] = viewSpace.changeViewPortZtoWorldReferenceZ((int) vertices[i][j * 3  + 2]);
			}
		}

		return vertices.length;
	}
//
//	public static int esGenSphere(GLContext glContext, SphereInfo info){
//		GLViewSpace viewSpace = glContext.getViewSpace();
//		int radius = info.inRadius;
//		int centerX = info.inCenterX, centerY = info.inCenterY, centerZ = info.inCenterZ;
//		int numSlices = info.inNumSlices;
//		int numParallels = numSlices / 2;
//		int numVertices = ( numParallels + 1 ) * ( numSlices + 1 );
//		int numIndices = numParallels * numSlices * 6;
//		float angleStep = (2.0f * (float)Math.PI) / ((float) numSlices);
//
//		// Allocate memory for buffers
//		float[] vertices = info.outVertices = new float[3 * numVertices];
//		float[] normals = info.outNormals = new float[3 * numVertices];
//		float[] texCoords = info.outTexCoords = new float[2 * numVertices];
//		int [] indices = info.outIndices = new int[numIndices];
//
//		for(int i=0; i<numParallels+1; i++){
//			for(int j=0; j<numSlices+1; j++){
//				int vertex = ( i * (numSlices + 1) + j ) * 3; 
//
////				float temp1 = angleStep * (float)i;
////				float temp2 = angleStep * (float)j;
////				float temp3 = (float) Math.sin(temp1);
////				float temp4 = (float) Math.sin(temp2);
////				float temp5 = (float) Math.cos(temp1);
////				float temp6 = (float) Math.cos(temp2);
//				vertices[vertex + 0] = (float) (radius * Math.sin( angleStep * (float)i ) * Math.sin( angleStep * (float)j )) + centerX;
//				vertices[vertex + 1] = (float) (radius * Math.cos( angleStep * (float)i )) + centerY;
//				vertices[vertex + 2] = (float) (radius * Math.sin( angleStep * (float)i ) * Math.cos( angleStep * (float)j )) + centerZ;
//
//				normals[vertex + 0] = viewSpace.changeViewPortXtoWorldReferenceX((int)vertices[vertex + 0]);
//				normals[vertex + 1] = viewSpace.changeViewPortYtoWorldReferenceY((int)vertices[vertex + 1]);
//				normals[vertex + 2] = viewSpace.changeViewPortZtoWorldReferenceZ((int)vertices[vertex + 2]);
//
//				int texIndex = ( i * (numSlices + 1) + j ) * 2;
//				texCoords[texIndex + 0] = (float) j / (float) numSlices;
//				texCoords[texIndex + 1] = ( 1.0f - (float) i ) / (float) (numParallels - 1 );
//			}
//		}
//
//		// Generate the indices
//		if(indices != null){
//			int count = 0;
//			for(int i=0; i < numParallels; i++){
//				for(int j=0; j < numSlices; j++ ){
//					indices[count++] = i * ( numSlices + 1 ) + j;
//					indices[count++] = ( i + 1 ) * ( numSlices + 1 ) + j;
//					indices[count++] = ( i + 1 ) * ( numSlices + 1 ) + ( j + 1 );
//
//					indices[count++] = i * ( numSlices + 1 ) + j;
//					indices[count++] = ( i + 1 ) * ( numSlices + 1 ) + ( j + 1 );
//					indices[count++] = i * ( numSlices + 1 ) + ( j + 1 );
//				}
//			}
//		}
//
//		return numIndices;
//	}

//	public static int esGenSphere(GLContext glContext, SphereInfo info){
//		GLViewSpace viewSpace = glContext.getViewSpace();
//		int radius = info.inRadius;
//		int centerX = info.inCenterX, centerY = info.inCenterY, centerZ = info.inCenterZ;
//		int numSlices = info.inNumSlices;
//		int numParallels = numSlices / 2;
//		int numVertices = ( numParallels + 1 ) * ( numSlices + 1 );
//		int numIndices = numParallels * numSlices * 6;
//		float angleStep = (2.0f * (float)Math.PI) / ((float) numSlices);
//
//		// Allocate memory for buffers
//		float[] vertices = info.outVertices = new float[3 * numVertices];
//		float[] normals = info.outNormals = new float[3 * numVertices];
//		float[] texCoords = info.outTexCoords = new float[2 * numVertices];
//		int [] indices = info.outIndices = new int[numIndices];
//
//		for(int i=0; i<numParallels+1; i++){
//			for(int j=0; j<numSlices+1; j++){
//				int vertex = ( i * (numSlices + 1) + j ) * 3; 
//
//				vertices[vertex + 0] = (float) (radius * Math.sin( angleStep * (float)i ) * Math.sin( angleStep * (float)j )) + centerX;
//				vertices[vertex + 1] = (float) (radius * Math.cos( angleStep * (float)i )) + centerY;
//				vertices[vertex + 2] = (float) (radius * Math.sin( angleStep * (float)i ) * Math.cos( angleStep * (float)j )) + centerZ;
//
//				normals[vertex + 0] = viewSpace.changeViewPortXtoWorldReferenceX((int)vertices[vertex + 0]);
//				normals[vertex + 1] = viewSpace.changeViewPortYtoWorldReferenceY((int)vertices[vertex + 1]);
//				normals[vertex + 2] = viewSpace.changeViewPortZtoWorldReferenceZ((int)vertices[vertex + 2]);
//
//				int texIndex = ( i * (numSlices + 1) + j ) * 2;
//				texCoords[texIndex + 0] = (float) j / (float) numSlices;
//				texCoords[texIndex + 1] = ( 1.0f - (float) i ) / (float) (numParallels - 1 );
//			}
//		}
//
//		// Generate the indices
//		if(indices != null){
//			int count = 0;
//			for(int i=0; i < numParallels; i++){
//				for(int j=0; j < numSlices; j++ ){
//					indices[count++] = i * ( numSlices + 1 ) + j;
//					indices[count++] = ( i + 1 ) * ( numSlices + 1 ) + j;
//					indices[count++] = ( i + 1 ) * ( numSlices + 1 ) + ( j + 1 );
//
//					indices[count++] = i * ( numSlices + 1 ) + j;
//					indices[count++] = ( i + 1 ) * ( numSlices + 1 ) + ( j + 1 );
//					indices[count++] = i * ( numSlices + 1 ) + ( j + 1 );
//				}
//			}
//		}
//
//		return numIndices;
//	}

	public static int esGenSphere(GLContext glContext, SphereInfo info){
		GLViewSpace viewSpace = glContext.getViewSpace();
		int multiSliceNum = 2;
		int radius = info.inRadius;
		int centerX = info.inCenterX, centerY = info.inCenterY, centerZ = info.inCenterZ;
		int angleZSliceNum = info.inNumSlices;
		int angleXYSliceNum = angleZSliceNum * multiSliceNum;
		float angleZStep = (float) (Math.PI / 2 / angleZSliceNum);
		ArrayList<Float> vertexArray = new ArrayList<Float>();
		float radiuses[] = {radius, -radius};

		for(int base=0, baseSize=radiuses.length; base<baseSize; base++){
			float currentPoints[][] = {
					{centerX, centerY, centerZ + radiuses[base]},
			};
			float nextPoints[][] = null;
			for(int i=0; i<angleZSliceNum; i++){
				float nextZPos = (float) (radiuses[base] * Math.cos(angleZStep * (i + 1)) + centerZ);
				float xyRadius = (float) (radiuses[base] * Math.sin(angleZStep * (i + 1)));
				float angleXYStep = (float) (2 * Math.PI / angleXYSliceNum);
				nextPoints = new float[angleXYSliceNum][3];
				for(int j=0; j<angleXYSliceNum; j++){
					nextPoints[j][0] = (float)(xyRadius * Math.cos(angleXYStep * j) + centerX);
					nextPoints[j][1] = (float)(xyRadius * Math.sin(angleXYStep * j) + centerY);
					nextPoints[j][2] = nextZPos;
				}

				if(i==0){
					for(int j=0; j<angleXYSliceNum; j++){
						int nextPos = (j+1)%angleXYSliceNum;
						vertexArray.add((float)currentPoints[0][0]);
						vertexArray.add((float)currentPoints[0][1]);
						vertexArray.add((float)currentPoints[0][2]);
						vertexArray.add(nextPoints[j][0]);
						vertexArray.add(nextPoints[j][1]);
						vertexArray.add(nextPoints[j][2]);
						vertexArray.add(nextPoints[nextPos][0]);
						vertexArray.add(nextPoints[nextPos][1]);
						vertexArray.add(nextPoints[nextPos][2]);
					}
				}else{
					for(int j=0, sizeJ=currentPoints.length; j<sizeJ; j++){
						int curNextPosJ = (j+1)%sizeJ;
						int nxtNextPosJ = (j*2+1)%nextPoints.length;
						int nxtNextNextPosJ = (j*2+2)%nextPoints.length;

						vertexArray.add((float)currentPoints[j][0]);
						vertexArray.add((float)currentPoints[j][1]);
						vertexArray.add((float)currentPoints[j][2]);
						vertexArray.add((float)nextPoints[j*2][0]);
						vertexArray.add((float)nextPoints[j*2][1]);
						vertexArray.add((float)nextPoints[j*2][2]);
						vertexArray.add((float)nextPoints[nxtNextPosJ][0]);
						vertexArray.add((float)nextPoints[nxtNextPosJ][1]);
						vertexArray.add((float)nextPoints[nxtNextPosJ][2]);

						vertexArray.add((float)currentPoints[j][0]);
						vertexArray.add((float)currentPoints[j][1]);
						vertexArray.add((float)currentPoints[j][2]);
						vertexArray.add((float)currentPoints[curNextPosJ][0]);
						vertexArray.add((float)currentPoints[curNextPosJ][1]);
						vertexArray.add((float)currentPoints[curNextPosJ][2]);
						vertexArray.add((float)nextPoints[nxtNextPosJ][0]);
						vertexArray.add((float)nextPoints[nxtNextPosJ][1]);
						vertexArray.add((float)nextPoints[nxtNextPosJ][2]);

						vertexArray.add((float)currentPoints[curNextPosJ][0]);
						vertexArray.add((float)currentPoints[curNextPosJ][1]);
						vertexArray.add((float)currentPoints[curNextPosJ][2]);
						vertexArray.add((float)nextPoints[nxtNextPosJ][0]);
						vertexArray.add((float)nextPoints[nxtNextPosJ][1]);
						vertexArray.add((float)nextPoints[nxtNextPosJ][2]);
						vertexArray.add((float)nextPoints[nxtNextNextPosJ][0]);
						vertexArray.add((float)nextPoints[nxtNextNextPosJ][1]);
						vertexArray.add((float)nextPoints[nxtNextNextPosJ][2]);
					}
				}

				angleXYSliceNum *= multiSliceNum;
				currentPoints = nextPoints;
			}
		}

		// Allocate memory for buffers
		float[] vertices = info.outVertices = new float[vertexArray.size()];
		float[] normals = info.outNormals = new float[vertexArray.size()];

		for(int i=0, sizeI=vertexArray.size()/3; i<sizeI; i++){
			vertices[i*3+0] = vertexArray.get(i*3+0);
			vertices[i*3+1] = vertexArray.get(i*3+1);
			vertices[i*3+2] = vertexArray.get(i*3+2);
			normals[i*3+0] = viewSpace.changeViewPortXtoWorldReferenceX((int)vertices[i*3+0]);
			normals[i*3+1] = viewSpace.changeViewPortYtoWorldReferenceY((int)vertices[i*3+1]);
			normals[i*3+2] = viewSpace.changeViewPortZtoWorldReferenceZ((int)vertices[i*3+2]);
		}

		return vertexArray.size();
	}

	public static int esGenHemisphere(GLContext glContext, SphereInfo info){
		GLViewSpace viewSpace = glContext.getViewSpace();
		int radius = info.inRadius;
		int centerX = info.inCenterX, centerY = info.inCenterY, centerZ = info.inCenterZ;
		int angleZSliceNum = info.inNumSlices;
		int angleXYSliceNum = angleZSliceNum * 2;
		float angleZStep = (float) (Math.PI / 2 / angleZSliceNum);
		float angleXYStep = (float) (2 * Math.PI / angleXYSliceNum);
		ArrayList<Float> vertexArray = new ArrayList<Float>();

		float currentPoints[][] = {
				{centerX, centerY, centerZ + radius},
		};
		float nextPoints[][] = null;
		for(int i=0; i<angleZSliceNum; i++){
			float nextZPos = (float) (radius * Math.cos(angleZStep * (i + 1)));
			nextPoints = new float[angleXYSliceNum][3];
			for(int j=0; j<angleXYSliceNum; j++){
				nextPoints[j][0] = (float)(radius * Math.cos(angleXYStep * j));
				nextPoints[j][1] = (float)(radius * Math.sin(angleXYStep * j));
				nextPoints[j][2] = nextZPos;
			}

			if(i==0){
				for(int j=0; j<angleXYSliceNum; j++){
					int nextPos = (j+1)%angleXYSliceNum;
					vertexArray.add((float)currentPoints[0][0]);
					vertexArray.add((float)currentPoints[0][1]);
					vertexArray.add((float)currentPoints[0][2]);
					vertexArray.add(nextPoints[j][0]);
					vertexArray.add(nextPoints[j][1]);
					vertexArray.add(nextPoints[j][2]);
					vertexArray.add(nextPoints[nextPos][0]);
					vertexArray.add(nextPoints[nextPos][1]);
					vertexArray.add(nextPoints[nextPos][2]);
				}
			}else{
				for(int j=0, sizeJ=currentPoints.length; j<sizeJ; j++){
					int curNextPosJ = (j+1)%sizeJ;
					int nxtNextPosJ = (j*2+1)%nextPoints.length;
					int nxtNextNextPosJ = (j*2+2)%nextPoints.length;

					vertexArray.add((float)currentPoints[j][0]);
					vertexArray.add((float)currentPoints[j][1]);
					vertexArray.add((float)currentPoints[j][2]);
					vertexArray.add((float)nextPoints[j*2][0]);
					vertexArray.add((float)nextPoints[j*2][1]);
					vertexArray.add((float)nextPoints[j*2][2]);
					vertexArray.add((float)nextPoints[nxtNextPosJ][0]);
					vertexArray.add((float)nextPoints[nxtNextPosJ][1]);
					vertexArray.add((float)nextPoints[nxtNextPosJ][2]);

					vertexArray.add((float)currentPoints[j][0]);
					vertexArray.add((float)currentPoints[j][1]);
					vertexArray.add((float)currentPoints[j][2]);
					vertexArray.add((float)currentPoints[curNextPosJ][0]);
					vertexArray.add((float)currentPoints[curNextPosJ][1]);
					vertexArray.add((float)currentPoints[curNextPosJ][2]);
					vertexArray.add((float)nextPoints[nxtNextPosJ][0]);
					vertexArray.add((float)nextPoints[nxtNextPosJ][1]);
					vertexArray.add((float)nextPoints[nxtNextPosJ][2]);

					vertexArray.add((float)currentPoints[curNextPosJ][0]);
					vertexArray.add((float)currentPoints[curNextPosJ][1]);
					vertexArray.add((float)currentPoints[curNextPosJ][2]);
					vertexArray.add((float)nextPoints[nxtNextPosJ][0]);
					vertexArray.add((float)nextPoints[nxtNextPosJ][1]);
					vertexArray.add((float)nextPoints[nxtNextPosJ][2]);
					vertexArray.add((float)nextPoints[nxtNextNextPosJ][0]);
					vertexArray.add((float)nextPoints[nxtNextNextPosJ][1]);
					vertexArray.add((float)nextPoints[nxtNextNextPosJ][2]);
				}
			}

			angleXYSliceNum *= 2;
			currentPoints = nextPoints;
		}

		// Allocate memory for buffers
		float[] vertices = info.outVertices = new float[vertexArray.size()];
		float[] normals = info.outNormals = new float[vertexArray.size()];

		for(int i=0, sizeI=vertexArray.size()/3; i<sizeI; i++){
			vertices[i*3+0] = vertexArray.get(i*3+0);
			vertices[i*3+1] = vertexArray.get(i*3+1);
			vertices[i*3+2] = vertexArray.get(i*3+2);
			normals[i*3+0] = viewSpace.changeViewPortXtoWorldReferenceX((int)vertices[i*3+0]);
			normals[i*3+1] = viewSpace.changeViewPortYtoWorldReferenceY((int)vertices[i*3+1]);
			normals[i*3+2] = viewSpace.changeViewPortZtoWorldReferenceZ((int)vertices[i*3+2]);
		}

		return vertexArray.size();
	}

	public static int esGenCylinder(GLContext glContext, CylinderInfo info){
		GLViewSpace viewSpace = glContext.getViewSpace();
		int height = info.inHeight;
		float halfHeight = (float)height / 2;
		int radius = info.inRadius;
		int centerX = info.inCenterX, centerY = info.inCenterY, centerZ = info.inCenterZ;
		int numStacks = info.inNumStacks;
		float zStep = (float)height / numStacks;
		int angleXYSliceNum = info.inNumSlices;
		float angleXYStep = (float) (2 * Math.PI / angleXYSliceNum);

		float vertex[] = info.outVertices = new float[(angleXYSliceNum + 1) * numStacks * 3 * 2];
		float normals[] = info.outNormals = new float[(angleXYSliceNum + 1) * numStacks * 3 * 2];
		float xPosArray[] = new float[angleXYSliceNum];
		float yPosArray[] = new float[angleXYSliceNum];

		for(int i=0; i<angleXYSliceNum; i++){
			xPosArray[i] = (float) (radius * Math.cos(angleXYStep * i) + centerX);
			yPosArray[i] = (float) (radius * Math.sin(angleXYStep * i) + centerY);
		}

		int position = 0;
		for(int i=0; i<numStacks; i++){
			float zPos = centerZ - halfHeight + i * zStep;
			float nextZPos = zPos + zStep;

			for(int j=0; j<angleXYSliceNum; j++){
				vertex[position] = xPosArray[j];
				normals[position] = viewSpace.changeViewPortXtoWorldReferenceX((int)vertex[position++]);
				vertex[position] = yPosArray[j];
				normals[position] = viewSpace.changeViewPortYtoWorldReferenceY((int)vertex[position++]);
				vertex[position] = zPos;
				normals[position] = viewSpace.changeViewPortZtoWorldReferenceZ((int)vertex[position++]);
				vertex[position] = xPosArray[j];
				normals[position] = viewSpace.changeViewPortXtoWorldReferenceX((int)vertex[position++]);
				vertex[position] = yPosArray[j];
				normals[position] = viewSpace.changeViewPortYtoWorldReferenceY((int)vertex[position++]);
				vertex[position] = nextZPos;
				normals[position] = viewSpace.changeViewPortZtoWorldReferenceZ((int)vertex[position++]);
			}

			vertex[position] = xPosArray[0];
			normals[position] = viewSpace.changeViewPortXtoWorldReferenceX((int)vertex[position++]);
			vertex[position] = yPosArray[0];
			normals[position] = viewSpace.changeViewPortYtoWorldReferenceY((int)vertex[position++]);
			vertex[position] = zPos;
			normals[position] = viewSpace.changeViewPortZtoWorldReferenceZ((int)vertex[position++]);
			vertex[position] = xPosArray[0];
			normals[position] = viewSpace.changeViewPortXtoWorldReferenceX((int)vertex[position++]);
			vertex[position] = yPosArray[0];
			normals[position] = viewSpace.changeViewPortYtoWorldReferenceY((int)vertex[position++]);
			vertex[position] = nextZPos;
			normals[position] = viewSpace.changeViewPortZtoWorldReferenceZ((int)vertex[position++]);
		}

		return vertex.length;
	}
}
