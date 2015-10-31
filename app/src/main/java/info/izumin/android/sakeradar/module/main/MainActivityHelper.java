package info.izumin.android.sakeradar.module.main;

import android.databinding.DataBindingUtil;

import javax.inject.Inject;

import info.izumin.android.sakeradar.App;
import info.izumin.android.sakeradar.R;
import info.izumin.android.sakeradar.databinding.ActivityMainBinding;
import info.izumin.android.sakeradar.module.sensor.Accelerometer;
import info.izumin.android.sakeradar.module.sensor.DaggerSensorComponent;
import info.izumin.android.sakeradar.module.sensor.LocationObservable;
import info.izumin.android.sakeradar.module.sensor.MagneticFieldMeter;
import info.izumin.android.sakeradar.module.sensor.SensorModule;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by izumin on 10/22/15.
 */
public class MainActivityHelper {
    public static final String TAG = MainActivityHelper.class.getSimpleName();

    @Inject Accelerometer mAccelerometer;
    @Inject MagneticFieldMeter mMagneticFieldMeter;
    @Inject LocationObservable mLocationObservable;

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
        binding.setLocation(mLocationObservable);
    }

    public void onResume() {
        mAccelerometer.start();
        mMagneticFieldMeter.start();
        mLocationObservable.start();
        mSubscriptions = new CompositeSubscription();
        mSubscriptions.add(mAccelerometer.observe().subscribe());
        mSubscriptions.add(mMagneticFieldMeter.observe().subscribe());
        mSubscriptions.add(mLocationObservable.observe().subscribe());
    }

    public void onStop() {
        mSubscriptions.unsubscribe();
        mAccelerometer.stop();
        mMagneticFieldMeter.stop();
        mLocationObservable.stop();
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
