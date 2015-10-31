package info.izumin.android.sakeradar.module.sensor;

import android.content.Context;
import android.hardware.SensorManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by izumin on 10/22/15.
 */
@Module
public class SensorModule {
    public static final String TAG = SensorModule.class.getSimpleName();

    @Provides
    @Singleton
    SensorManager provideSensorManager(Context context) {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Provides
    @Singleton
    Accelerometer provideAccelerometer(SensorManager manager) {
        return new Accelerometer(manager);
    }

    @Provides
    @Singleton
    MagneticFieldMeter provideMagneticFieldMeter(SensorManager manager) {
        return new MagneticFieldMeter(manager);
    }

    @Provides
    @Singleton
    LocationObservable provideLocationObservable(Context context) {
        return new LocationObservable(context);
    }
}
