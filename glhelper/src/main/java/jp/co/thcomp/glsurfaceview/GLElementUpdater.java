package jp.co.thcomp.glsurfaceview;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

import java.util.Comparator;
import java.util.List;

import jp.co.thcomp.util.HashMultimap;

public class GLElementUpdater implements Handler.Callback {
    public static final int NO_MORE_ANIMATION = -1;

    protected static final int NOTIFY_ADD_ELEMENT = 1;
    protected static final int MIN_POLLING_INTERVAL = 10;
    protected View mView;
    protected Object mSemaphore = new Object();
    protected boolean mResumed = false;
    protected HandlerThread mUpdaterThread;
    protected Handler mUpdaterHandler;
    protected HashMultimap<Integer, GLDrawElement> mUpdateElementMap = new HashMultimap<Integer, GLDrawElement>();

    public GLElementUpdater(View view) {
        if (view == null) {
            throw new NullPointerException("view == null");
        }
        mView = view;
        mUpdaterThread = new HandlerThread(GLElementUpdater.class.getSimpleName(), Thread.MIN_PRIORITY);
        mUpdaterThread.start();
        mUpdaterHandler = new Handler(mUpdaterThread.getLooper(), this);
    }

    public void add(GLDrawElement drawElement) {
        synchronized (mSemaphore) {
            mUpdateElementMap.add(0, drawElement);
            mUpdaterHandler.sendEmptyMessage(NOTIFY_ADD_ELEMENT);
        }
    }

    public void remove(GLDrawElement drawElement) {
        synchronized (mSemaphore) {
            mUpdateElementMap.removeValue(drawElement);
        }
    }

    public void removeAll() {
        synchronized (mSemaphore) {
            mUpdateElementMap.clearAll();
        }
    }

    public void onResume() {
        mResumed = true;
        if (mUpdateElementMap.size() > 0) {
            mUpdaterHandler.sendEmptyMessage(NOTIFY_ADD_ELEMENT);
        }
    }

    public void onPause() {
        mResumed = false;
        if (mUpdateElementMap.size() > 0) {
            mUpdaterHandler.removeMessages(NOTIFY_ADD_ELEMENT);
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        boolean ret = false;

        if (message.what == NOTIFY_ADD_ELEMENT) {
            ret = true;

            GLDrawElement drawElement = null;
            List<Integer> sleepIntervalList = null;
            List<GLDrawElement> tempElementList = null;
            int sleepInterval = 0;
            int nextSleepInterval = -1;
            int targetSleepInterval = 0;
            long currentTime1 = 0;
            long currentTime2 = 0;
            boolean needRender = false;

            synchronized (mSemaphore) {
                if (mResumed && mUpdateElementMap.size() > 0) {
                    currentTime1 = SystemClock.elapsedRealtime();
                    mUpdateElementMap.sort(new Comparator() {
                        public int compare(Object object1, Object object2) {
                            int ret = 0;

                            if ((object1 instanceof Integer) && (object2 instanceof Integer)) {
                                Integer i1 = (Integer) object1;
                                Integer i2 = (Integer) object2;

                                ret = (i1 == i2) ? 0 : i1 < i2 ? -1 : 1;
                            }

                            return ret;
                        }
                    });
                    sleepIntervalList = mUpdateElementMap.keyList();

                    needRender = false;
                    for (int i = 0, size = sleepIntervalList.size(); i < size; i++) {
                        targetSleepInterval = sleepIntervalList.get(i);
                        tempElementList = mUpdateElementMap.remove(targetSleepInterval);

                        if (targetSleepInterval == 0) {
                            nextSleepInterval = -1;
                            for (int j = 0, sizeJ = tempElementList.size(); j < sizeJ; j++) {
                                drawElement = tempElementList.get(j);
                                if (drawElement != null) {
                                    if ((nextSleepInterval = drawElement.update()) < 0) {
                                        mUpdateElementMap.removeValue(drawElement);
                                    } else {
                                        nextSleepInterval = nextSleepInterval - (nextSleepInterval % MIN_POLLING_INTERVAL);
                                        if (sleepInterval > nextSleepInterval) {
                                            sleepInterval = nextSleepInterval;
                                        }

                                        // add to sleep+MIN_POLLING_INTERVAL, because over 0 lists will move to current - MIN_POLLING_INTERVAL
                                        mUpdateElementMap.add(nextSleepInterval + MIN_POLLING_INTERVAL, drawElement);
                                    }
                                }
                            }
                            needRender = true;
                        } else {
                            targetSleepInterval -= MIN_POLLING_INTERVAL;
                            mUpdateElementMap.addAll(targetSleepInterval, tempElementList);
                        }
                    }

                    if (needRender && (mView instanceof GLDrawView)) {
                        ((GLDrawView) mView).requestRender();
                    }

                    currentTime2 = SystemClock.elapsedRealtime();
                    sleepInterval = sleepInterval - (int) (currentTime2 - currentTime1);
                    if (sleepInterval > 0) {
                        mUpdaterHandler.sendEmptyMessageDelayed(NOTIFY_ADD_ELEMENT, sleepInterval);
                    } else {
                        mUpdaterHandler.sendEmptyMessage(NOTIFY_ADD_ELEMENT);
                    }
                }
            }
        }

        return ret;
    }

    public interface OnUpdateListener {
        int onUpdate(GLDrawElement drawElement);
    }
}
