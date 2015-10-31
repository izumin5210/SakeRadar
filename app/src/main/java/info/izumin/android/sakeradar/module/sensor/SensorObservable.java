package info.izumin.android.sakeradar.module.sensor;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Pair;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.izumin.android.sakeradar.BR;
import rx.Observable;
import rx.Subscription;

/**
 * Created by izumin on 10/22/15.
 */
public abstract class SensorObservable extends BaseObservable implements SensorEventListener {
    public static final String TAG = SensorObservable.class.getSimpleName();

    private SensorManager mSensorManager;

    private List<Listener> mListeners;
    private float[] mValues;
    private SparseIntArray mBrMap;

    private static final float ALPHA = 0.8f;

    public SensorObservable(SensorManager manager) {
        mSensorManager = manager;
        mListeners = new ArrayList<>();
        mValues = new float[3];
        mBrMap = new SparseIntArray() {{
            append(0, BR.x); append(1, BR.y); append(2, BR.z);
        }};
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        for (Listener listener : mListeners) {
            listener.onSensorChanged(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Bindable
    public float getX() {
        return mValues[0];
    }

    @Bindable
    public float getY() {
        return mValues[1];
    }

    @Bindable
    public float getZ() {
        return mValues[2];
    }

    public SensorManager getSensorManager() {
        return mSensorManager;
    }

    public abstract Sensor getSensor();

    public void start() {
        mSensorManager.registerListener(this, getSensor(), SensorManager.SENSOR_DELAY_UI);
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    public Observable<Pair<Integer, Float>> observe() {
        return Observable.merge(observe(0), observe(1), observe(2));
    }

    private Observable<Pair<Integer, Float>> observe(final int index) {
        return Observable.create(getOnSubscribe(index))
                .buffer(10)
                .map(floats -> {
                    Collections.sort(floats);
                    return floats.get(floats.size() / 2);
                })
                .scan((acc, value) -> ALPHA * acc + (1 - ALPHA) * value)
                .doOnNext(aFloat -> {
                    mValues[index] = aFloat;
                    notifyPropertyChanged(mBrMap.get(index));
                })
                .map(aFloat -> new Pair<>(index, aFloat));
    }

    private Observable.OnSubscribe<Float> getOnSubscribe(final int index) {
        return subscriber -> {
            final Listener listener = event -> subscriber.onNext(event.values.clone()[index]);

            final Subscription subscription = new Subscription() {
                @Override
                public void unsubscribe() {
                    mListeners.remove(listener);
                }

                @Override
                public boolean isUnsubscribed() {
                    return false;
                }
            };

            subscriber.add(subscription);
            mListeners.add(listener);
        };
    }

    private interface Listener {
        void onSensorChanged(SensorEvent event);
    }
}
