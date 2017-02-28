package jp.co.thcomp.glsurfaceview;

import android.graphics.Rect;
import android.graphics.RectF;

public class RectF3D{
	public float left;
	public float top;
	public float right;
	public float bottom;

	public float ZofLeftTop;
	public float ZofRightTop;
	public float ZofLeftBottom;
	public float ZofRightBottom;

	private float savedLeft;
	private float savedTop;
	private float savedRight;
	private float savedBottom;

	private float savedZofLeftTop;
	private float savedZofRightTop;
	private float savedZofLeftBottom;
	private float savedZofRightBottom;

	private String mString = null;

	public RectF3D() {}

	public RectF3D(float left, float top, float right, float bottom, float zofLeftTop, float zofRightTop, float zofLeftBottom, float zofRightBottom) {
		set(left, top, right, bottom, zofLeftTop, zofRightTop, zofLeftBottom, zofRightBottom);
	}

	public RectF3D(RectF r) {
		if (r == null) {
			set(0, 0, 0, 0, 0, 0, 0, 0);
		} else {
			set(r);
		}
	}

	public RectF3D(RectF3D r) {
		if (r == null) {
			set(0, 0, 0, 0, 0, 0, 0, 0);
		} else {
			set(r);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Rect3D r = (Rect3D) o;
		return left == r.left && top == r.top && right == r.right && bottom == r.bottom
				&& ZofLeftTop == r.ZofLeftTop && ZofRightTop == r.ZofRightTop && ZofLeftBottom == r.ZofLeftBottom && ZofRightBottom == r.ZofRightBottom;
	}

	@Override
	public int hashCode() {
		int result = (int)left;
		result = 31 * result + (int)top;
		result = 31 * result + (int)right;
		result = 31 * result + (int)bottom;
		result = 31 * result + (int)ZofLeftTop;
		result = 31 * result + (int)ZofRightTop;
		result = 31 * result + (int)ZofLeftBottom;
		result = 31 * result + (int)ZofRightBottom;
		return result;
	}

	@Override
	public String toString() {
		boolean needCreate = false;

		if(mString == null){
			needCreate = true;
		}else{
			needCreate = isChanged();
		}

		if(needCreate){
			saveValues();

			StringBuilder sb = new StringBuilder(32);
			sb.append("Rect3D{").append("LT(").append(left).append(",").append(top).append(",").append(ZofLeftTop).append("),");
			sb.append("RT(").append(right).append(",").append(top).append(",").append(ZofRightTop).append("),");
			sb.append("LB(").append(left).append(",").append(bottom).append(",").append(ZofLeftBottom).append("),");
			sb.append("RB(").append(right).append(",").append(bottom).append(",").append(ZofRightBottom).append(")}");
			mString = sb.toString();
		}

		return mString;
	}

	public String toShortString() {
		return toShortString(new StringBuilder(32));
	}

	public String toShortString(StringBuilder sb) {
		sb.setLength(0);
		sb.append(toString());
		return sb.toString();
	}

	public final boolean isEmpty() {
		return left >= right || top >= bottom;
	}

	public final float width() {
		return right - left;
	}

	public final float height() {
		return bottom - top;
	}

	public final float centerX() {
		return (left + right) * 0.5f;
	}

	public final float centerY() {
		return (top + bottom) * 0.5f;
	}

	public final float centerZ() {
		return (ZofLeftTop + ZofRightTop + ZofLeftBottom + ZofRightBottom) * 0.25f;
	}

	public final float exactCenterX() {
		return (left + right) * 0.5f;
	}

	public final float exactCenterY() {
		return (top + bottom) * 0.5f;
	}

	public final float exactCenterZ() {
		return (ZofLeftTop + ZofRightTop + ZofLeftBottom + ZofRightBottom) * 0.25f;
	}

	public void setEmpty() {
		left = right = top = bottom = ZofLeftTop = ZofRightTop = ZofLeftBottom = ZofRightBottom = 0;
	}

	public void set(float left, float top, float right, float bottom) {
		set(left, top, right, bottom, 0, 0, 0, 0);
	}

	public void set(RectF src) {
		set(src.left, src.top, src.right, src.bottom, 0, 0, 0, 0);
	}

	public void set(RectF3D src) {
		set(src.left, src.top, src.right, src.bottom, src.ZofLeftTop, src.ZofRightTop, src.ZofLeftBottom, src.ZofRightBottom);
	}

	public void set(float left, float top, float right, float bottom, float zofLeftTop, float zofRightTop, float zofLeftBottom, float zofRightBottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.ZofLeftTop = zofLeftTop;
		this.ZofLeftBottom = zofLeftBottom;
		this.ZofRightTop = zofRightTop;
		this.ZofRightBottom = zofRightBottom;
	}

	public void offset(float dx, float dy) {
		offset(dx, dy, 0);
	}

	public void offset(float dx, float dy, float dz) {
		left += dx;
		top += dy;
		right += dx;
		bottom += dy;
		ZofLeftTop += dz;
		ZofLeftBottom += dz;
		ZofRightTop += dz;
		ZofRightBottom += dz;
	}

	public void offsetTo(float newLeft, float newTop) {
		right += newLeft - left;
		bottom += newTop - top;
		left = newLeft;
		top = newTop;
	}

	public void inset(float dx, float dy) {
		left += dx;
		top += dy;
		right -= dx;
		bottom -= dy;
	}

	public boolean contains(float x, float y) {
		return left < right && top < bottom  // check for empty first
				&& x >= left && x < right && y >= top && y < bottom;
	}

	public boolean contains(float left, float top, float right, float bottom) {
		// check for empty first
		return this.left < this.right && this.top < this.bottom
				// now check for containment
				&& this.left <= left && this.top <= top
				&& this.right >= right && this.bottom >= bottom;
	}

	public boolean contains(Rect r) {
		// check for empty first
		return this.left < this.right && this.top < this.bottom
				// now check for containment
				&& left <= r.left && top <= r.top && right >= r.right && bottom >= r.bottom;
	}

	public boolean intersect(float left, float top, float right, float bottom) {
		if (this.left < right && left < this.right && this.top < bottom && top < this.bottom) {
			if (this.left < left) this.left = left;
			if (this.top < top) this.top = top;
			if (this.right > right) this.right = right;
			if (this.bottom > bottom) this.bottom = bottom;
			return true;
		}
		return false;
	}
	
	public boolean floatersect(Rect r) {
		return intersect(r.left, r.top, r.right, r.bottom);
	}

	public boolean setIntersect(Rect a, Rect b) {
		if (a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom) {
			left = Math.max(a.left, b.left);
			top = Math.max(a.top, b.top);
			right = Math.min(a.right, b.right);
			bottom = Math.min(a.bottom, b.bottom);
			return true;
		}
		return false;
	}

	public boolean intersects(float left, float top, float right, float bottom) {
		return this.left < right && left < this.right && this.top < bottom && top < this.bottom;
	}

	public static boolean intersects(Rect a, Rect b) {
		return a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom;
	}

	public void union(int left, float top, float right, float bottom) {
		if ((left < right) && (top < bottom)) {
			if ((this.left < this.right) && (this.top < this.bottom)) {
				if (this.left > left) this.left = left;
				if (this.top > top) this.top = top;
				if (this.right < right) this.right = right;
				if (this.bottom < bottom) this.bottom = bottom;
			} else {
				this.left = left;
				this.top = top;
				this.right = right;
				this.bottom = bottom;
			}
		}
	}

	public void union(Rect r) {
		union(r.left, r.top, r.right, r.bottom);
	}

	public void union(float x, float y) {
		if (x < left) {
			left = x;
		} else if (x > right) {
			right = x;
		}
		if (y < top) {
			top = y;
		} else if (y > bottom) {
			bottom = y;
		}
	}

	public void sort() {
		if (left > right) {
			float temp = left;
			left = right;
			right = temp;
		}
		if (top > bottom) {
			float temp = top;
			top = bottom;
			bottom = temp;
		}
	}

	private boolean isChanged(){
		boolean changed = false;

		if(savedLeft != left){
			changed = true;
		}else if(savedTop != top){
			changed = true;
		}else if(savedRight != right){
			changed = true;
		}else if(savedBottom != bottom){
			changed = true;
		}else if(savedZofLeftTop != ZofLeftTop){
			changed = true;
		}else if(savedZofRightTop != ZofRightTop){
			changed = true;
		}else if(savedZofLeftBottom != ZofLeftBottom){
			changed = true;
		}else if(savedZofRightBottom != ZofRightBottom){
			changed = true;
		}

		return changed;
	}

	private void saveValues(){
		savedLeft = left;
		savedTop = top;
		savedRight = right;
		savedBottom = bottom;
		savedZofLeftTop = ZofLeftTop;
		savedZofRightTop = ZofRightTop;
		savedZofLeftBottom = ZofLeftBottom;
		savedZofRightBottom = ZofRightBottom;
	}
}
