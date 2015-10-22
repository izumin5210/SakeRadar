package info.izumin.android.sakeradar.module.main;

import android.databinding.DataBindingUtil;

import javax.inject.Inject;

import info.izumin.android.sakeradar.App;
import info.izumin.android.sakeradar.R;
import info.izumin.android.sakeradar.databinding.ActivityMainBinding;
import info.izumin.android.sakeradar.module.sensor.Accelerometer;
import info.izumin.android.sakeradar.module.sensor.DaggerSensorComponent;
import info.izumin.android.sakeradar.module.sensor.MagneticFieldMeter;
import info.izumin.android.sakeradar.module.sensor.SensorModule;
import info.izumin.android.sakeradar.module.sensor.SensorObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by izumin on 10/22/15.
 */
public class MainActivityHelper {
    public static final String TAG = MainActivityHelper.class.getSimpleName();

    @Inject Accelerometer mAccelerometer;
    @Inject MagneticFieldMeter mMagneticFieldMeter;

    private final MainActivity mActivity;

    private CompositeSubscription mSubscriptions;

    public MainActivityHelper(MainActivity activity) {
        mActivity = activity;
        setupComponent();
    }

    public void onCreate() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_main);
        binding.setAcc(mAccelerometer);
        binding.setMag(mMagneticFieldMeter);
    }

    public void onResume() {
        mAccelerometer.start();
        mMagneticFieldMeter.start();
        mSubscriptions = new CompositeSubscription();
        for (SensorObservable observable : new SensorObservable[]{ mAccelerometer, mMagneticFieldMeter }) {
            for (int i = 0; i < 3; i++) {
                mSubscriptions.add(
                        observable.observe(i)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                );
            }
        }
    }

    public void onStop() {
        mAccelerometer.stop();
        mMagneticFieldMeter.stop();
        mSubscriptions.unsubscribe();
        mSubscriptions.clear();
    }

    private void setupComponent() {
        DaggerSensorComponent.builder()
                .appComponent(App.getApp(mActivity).getComponent())
                .sensorModule(new SensorModule())
                .build()
                .inject(this);
    }
}
