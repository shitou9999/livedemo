package tv.kuainiu.utils;

import android.os.Handler;

import java.lang.ref.WeakReference;

/**
* @ClassName WeakHandler
* @Description Handler弱引用
* @param <T>
*/ 
public abstract class WeakHandler<T> extends Handler {
	private WeakReference<T> mOwner;

	public WeakHandler(T owner) {
		mOwner = new WeakReference<T>(owner);
	}

	public T getOwner() {
		return mOwner.get();
	}
}
