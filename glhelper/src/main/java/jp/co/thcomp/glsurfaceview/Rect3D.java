package jp.co.thcomp.glsurfaceview;

import android.graphics.Rect;

public class Rect3D{
	public int left;
	public int top;
	public int right;
	public int bottom;

	public int ZofLeftTop;
	public int ZofRightTop;
	public int ZofLeftBottom;
	public int ZofRightBottom;

	private int savedLeft;
	private int savedTop;
	private int savedRight;
	private int savedBottom;

	private int savedZofLeftTop;
	private int savedZofRightTop;
	private int savedZofLeftBottom;
	private int savedZofRightBottom;

	private String mString = null;

	public Rect3D() {}

	public Rect3D(int left, int top, int right, int bottom, int zofLeftTop, int zofRightTop, int zofLeftBottom, int zofRightBottom) {
		set(left, top, right, bottom, zofLeftTop, zofRightTop, zofLeftBottom, zofRightBottom);
	}

	public Rect3D(Rect r) {
		if (r == null) {
			set(0, 0, 0, 0, 0, 0, 0, 0);
		} else {
			set(r);
		}
	}

	public Rect3D(Rect3D r) {
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
		int result = left;
		result = 31 * result + top;
		result = 31 * result + right;
		result = 31 * result + bottom;
		result = 31 * result + ZofLeftTop;
		result = 31 * result + ZofRightTop;
		result = 31 * result + ZofLeftBottom;
		result = 31 * result + ZofRightBottom;
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

	public final int width() {
		return right - left;
	}

	public final int height() {
		return bottom - top;
	}

	public final int centerX() {
		return (left + right) >> 1;
	}

	public final int centerY() {
		return (top + bottom) >> 1;
	}

	public final int centerZ() {
		return (ZofLeftTop + ZofRightTop + ZofLeftBottom + ZofRightBottom) >> 2;
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

	public void set(int left, int top, int right, int bottom) {
		set(left, top, right, bottom, 0, 0, 0, 0);
	}

	public void set(Rect src) {
		set(src.left, src.top, src.right, src.bottom, 0, 0, 0, 0);
	}

	public void set(Rect3D src) {
		set(src.left, src.top, src.right, src.bottom, src.ZofLeftTop, src.ZofRightTop, src.ZofLeftBottom, src.ZofRightBottom);
	}

	public void set(int left, int top, int right, int bottom, int zofLeftTop, int zofRightTop, int zofLeftBottom, int zofRightBottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.ZofLeftTop = zofLeftTop;
		this.ZofLeftBottom = zofLeftBottom;
		this.ZofRightTop = zofRightTop;
		this.ZofRightBottom = zofRightBottom;
	}

	public void offset(int dx, int dy) {
		offset(dx, dy, 0);
	}

	public void offset(int dx, int dy, int dz) {
		left += dx;
		top += dy;
		right += dx;
		bottom += dy;
		ZofLeftTop += dz;
		ZofLeftBottom += dz;
		ZofRightTop += dz;
		ZofRightBottom += dz;
	}

	public void offsetTo(int newLeft, int newTop) {
		right += newLeft - left;
		bottom += newTop - top;
		left = newLeft;
		top = newTop;
	}

	public void inset(int dx, int dy) {
		left += dx;
		top += dy;
		right -= dx;
		bottom -= dy;
	}

	public boolean contains(int x, int y) {
		return left < right && top < bottom  // check for empty first
				&& x >= left && x < right && y >= top && y < bottom;
	}

	public boolean contains(int left, int top, int right, int bottom) {
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

	public boolean intersect(int left, int top, int right, int bottom) {
		if (this.left < right && left < this.right && this.top < bottom && top < this.bottom) {
			if (this.left < left) this.left = left;
			if (this.top < top) this.top = top;
			if (this.right > right) this.right = right;
			if (this.bottom > bottom) this.bottom = bottom;
			return true;
		}
		return false;
	}
	
	public boolean intersect(Rect r) {
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

	public boolean intersects(int left, int top, int right, int bottom) {
		return this.left < right && left < this.right && this.top < bottom && top < this.bottom;
	}

	public static boolean intersects(Rect a, Rect b) {
		return a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom;
	}

	public void union(int left, int top, int right, int bottom) {
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

	public void union(int x, int y) {
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
			int temp = left;
			left = right;
			right = temp;
		}
		if (top > bottom) {
			int temp = top;
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
