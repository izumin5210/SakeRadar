package info.izumin.android.sakeradar.module.sensor;

import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by izumin on 10/22/15.
 */
public class Accelerometer extends SensorObservable {
    public static final String TAG = Accelerometer.class.getSimpleName();

    public Accelerometer(SensorManager manager) {
        super(manager);
    }

    @Override
    public Sensor getSensor() {
        return getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
}
