package jp.co.thcomp.glsurfaceview;

import java.util.Comparator;
import java.util.List;

import android.os.HandlerThread;
import android.os.SystemClock;

import jp.co.thcomp.util.HashMultimap;
import jp.co.thcomp.util.LooperThread;

public class GLElementUpdater {
	public static final int NO_MORE_ANIMATION = -1;

	protected static final int MIN_POLLING_INTERVAL = 10;
	protected GLDrawView mView;
	protected Object mSemapho = new Object();
	protected boolean mResumed = false;
	protected HandlerThread mUpdaterThread;
	protected LooperThread.Runnable mUpdaterRunnable;
	protected HashMultimap<Integer, GLDrawElement> mUpdateElementMap = new HashMultimap<Integer, GLDrawElement>();

	public GLElementUpdater(GLDrawView view){
		mView = view;
		mUpdaterRunnable = new LooperThread.Runnable() {
			public Object run(Object param) {
				return updateDrawElement(param);
			}
		};
		mUpdaterThread = new LooperThread(mUpdaterRunnable, null);
	}

	public void add(GLDrawElement drawElement){
		synchronized(mSemapho){
			mUpdateElementMap.add(0, drawElement);
			mUpdaterThread.startRunnable();
		}
	}

	public void remove(GLDrawElement drawElement){
		synchronized(mSemapho){
			mUpdateElementMap.removeValue(drawElement);
		}
	}

	public void removeAll(){
		synchronized(mSemapho){
			mUpdateElementMap.clearAll();
		}
	}

	public void onResume() {
		mResumed = true;
		if(mUpdateElementMap.size() > 0){
			mUpdaterThread.startRunnable();
		}
	}

	public void onPause() {
		mResumed = false;
		if(mUpdateElementMap.size() > 0){
			synchronized(mUpdateElementMap){
				mUpdateElementMap.notify();
			}
			mUpdaterThread.stopRunnable();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object updateDrawElement(Object param){
		GLDrawElement drawElement = null;
		List<Integer> sleepIntervalList = null;
		List<GLDrawElement> tempElementList = null;
		int sleepInterval = 0;
		int nextSleepInterval = -1;
		int targetSleepInterval = 0;
		long currentTime1 = 0;
		long currentTime2 = 0;
		boolean needRender = false;

		synchronized(mSemapho){
			while(mResumed && mUpdateElementMap.size()>0){
				currentTime1 = SystemClock.elapsedRealtime();
				mUpdateElementMap.sort(new Comparator(){
					public int compare(Object object1, Object object2) {
						int ret = 0;

						if((object1 instanceof Integer) && (object2 instanceof Integer)){
							Integer i1 = (Integer)object1;
							Integer i2 = (Integer)object2;

							ret = (i1 == i2) ? 0 : i1 < i2 ? -1 : 1;
						}
		
						return ret;
					}
				});
				sleepIntervalList = mUpdateElementMap.keyList();

				needRender = false;
				for(int i=0, size=sleepIntervalList.size(); i<size; i++){
					targetSleepInterval = sleepIntervalList.get(i);
					tempElementList = mUpdateElementMap.remove(targetSleepInterval);

					if(targetSleepInterval == 0){
						nextSleepInterval = -1;
						for(int j=0, sizeJ=tempElementList.size(); j<sizeJ; j++){
							drawElement = tempElementList.get(j);
							if(drawElement != null){
								if((nextSleepInterval = drawElement.update()) < 0){
									mUpdateElementMap.removeValue(drawElement);
								}else{
									nextSleepInterval = nextSleepInterval - (nextSleepInterval % MIN_POLLING_INTERVAL);
									if(sleepInterval > nextSleepInterval){
										sleepInterval = nextSleepInterval;
									}

									// add to sleep+MIN_POLLING_INTERVAL, because over 0 lists will move to current - MIN_POLLING_INTERVAL
									mUpdateElementMap.add(nextSleepInterval + MIN_POLLING_INTERVAL, drawElement);
								}
							}
						}
						needRender = true;
					}else{
						targetSleepInterval -= MIN_POLLING_INTERVAL;
						mUpdateElementMap.addAll(targetSleepInterval, tempElementList);
					}
				}

				if(needRender){
					mView.requestRender();
				}
				try {
					currentTime2 = SystemClock.elapsedRealtime();
					sleepInterval = sleepInterval - (int)(currentTime2 - currentTime1);
					if(sleepInterval > 0){
						synchronized(mUpdateElementMap){
							mUpdateElementMap.wait(sleepInterval);
						}
					}
				} catch (InterruptedException e) {
					break;
				}
			}
		}

		return null;
	}

	public interface OnUpdateListener{
		int onUpdate(GLDrawElement drawElement);
	}
}
