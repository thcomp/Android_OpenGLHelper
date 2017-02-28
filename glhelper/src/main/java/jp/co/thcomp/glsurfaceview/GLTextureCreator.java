package jp.co.thcomp.glsurfaceview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class GLTextureCreator {
	public static GLTexture createGLTexture(GLDrawView view, Bitmap bitmap){
		return createGLTexture(view, bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
	}

	public static GLTexture createGLTexture(GLDrawView view, Bitmap bitmap, Rect srcRect){
		GLTexture texture = new GLTexture(view, true);
		int orgBitmapWidth = bitmap.getWidth();
		int orgBitmapHeight = bitmap.getHeight();
		int newBitmapWidth = orgBitmapWidth;
		int newBitmapHeight = orgBitmapHeight;

		if(!isPowerOf(orgBitmapWidth, 2)){
			newBitmapWidth = minPowerOf(orgBitmapWidth, 2);
		}
		if(!isPowerOf(orgBitmapHeight, 2)){
			newBitmapHeight = minPowerOf(orgBitmapHeight, 2);
		}

		Bitmap texBitmap = Bitmap.createBitmap(newBitmapWidth, newBitmapHeight, bitmap.getConfig());
		Canvas canvas = new Canvas(texBitmap);
		canvas.drawBitmap(bitmap, 0, 0, null);

		if((orgBitmapWidth != newBitmapWidth) || (orgBitmapHeight != newBitmapHeight) || (orgBitmapWidth != srcRect.width()) || (orgBitmapHeight != srcRect.height())){
			float texCoord[] = new float[8];
			texCoord[0] = ((float)srcRect.left) / newBitmapWidth;
			texCoord[1] = ((float)srcRect.top) / newBitmapHeight;
			texCoord[2] = ((float)srcRect.right) / newBitmapWidth;
			texCoord[3] = ((float)srcRect.top) / newBitmapHeight;
			texCoord[4] = ((float)srcRect.right) / newBitmapWidth;
			texCoord[5] = ((float)srcRect.bottom) / newBitmapHeight;
			texCoord[6] = ((float)srcRect.left) / newBitmapWidth;
			texCoord[7] = ((float)srcRect.bottom) / newBitmapHeight;
			texture.registTextureBitmap(texBitmap, texCoord);
		}else{
			texture.registTextureBitmap(texBitmap);
		}

		return texture;
	}

	public static boolean isPowerOf(int target, int base){
		boolean ret = false;
		int dividedNum = target;
		int mod = 0;

		if(target == 0){
			ret = false;
		}else if(target == 1){
			ret = true;
		}else{
			while(true){
				if(dividedNum == base){
					ret = true;
					break;
				}else{
					mod = dividedNum % base;
					if(mod == 0){
						dividedNum /= base;
					}else{
						break;
					}
				}
			}
		}

		return ret;
	}

	public static int minPowerOf(int target, int base){
		int ret = base;

		while(true){
			if(target < ret){
				break;
			}

			ret *= base;
		}

		return ret;
	}
}
