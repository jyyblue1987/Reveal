package com.dating.reveal.image;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

public class CropBaseActivity extends Activity {

    private final ArrayList<LifeCycleListener> mListeners =
            new ArrayList<LifeCycleListener>();

    public static interface LifeCycleListener {
        public void onActivityCreated(CropBaseActivity activity);
        public void onActivityDestroyed(CropBaseActivity activity);
        public void onActivityPaused(CropBaseActivity activity);
        public void onActivityResumed(CropBaseActivity activity);
        public void onActivityStarted(CropBaseActivity activity);
        public void onActivityStopped(CropBaseActivity activity);
    }

    public static class LifeCycleAdapter implements LifeCycleListener {
        public void onActivityCreated(CropBaseActivity activity) {
        }

        public void onActivityDestroyed(CropBaseActivity activity) {
        }

        public void onActivityPaused(CropBaseActivity activity) {
        }

        public void onActivityResumed(CropBaseActivity activity) {
        }

        public void onActivityStarted(CropBaseActivity activity) {
        }

        public void onActivityStopped(CropBaseActivity activity) {
        }
    }

    public void addLifeCycleListener(LifeCycleListener listener) {
        if (mListeners.contains(listener)) return;
        mListeners.add(listener);
    }

    public void removeLifeCycleListener(LifeCycleListener listener) {
        mListeners.remove(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityCreated(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityDestroyed(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
         for (LifeCycleListener listener : mListeners) {
            listener.onActivityStarted(this);
        }
    }

    @Override
    protected void onStop() {
         super.onStop();        
        for (LifeCycleListener listener : mListeners) {
            listener.onActivityStopped(this);
        }
    }
    
	@Override 
	protected void onPause( ) {
		super.onPause();
	}

	@Override
	protected void onResume( ) {
		super.onResume();
	}
	

}