package info.izumin.android.sakeradar.module.sensor;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.eccyan.optional.Optional;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import info.izumin.android.sakeradar.BR;
import rx.Observable;
import rx.Subscription;

/**
 * Created by izumin on 10/22/15.
 */
public class LocationObservable extends BaseObservable implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = LocationObservable.class.getSimpleName();

    private static final int[] BR_LIST = {
            BR.time, BR.latitude, BR.longitude, BR.altitude, BR.speed,
            BR.bearing, BR.accuracy, BR.provider, BR.elapsedRealtimeNanos
    };

    private GoogleApiClient mApiClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;

    private List<Listener> mListeners;

    public LocationObservable(Context context) {
        mListeners = new ArrayList<>();
        mLocationRequest = createLocationRequest();
        mApiClient = createGoogleApiClient(context);
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
        for (Listener listener : mListeners) {
            listener.onLocationChanged(location);
        }
    }

    @Bindable
    public long getTime() {
        return Optional.ofNullable(mLocation).map(Location::getTime).orElse(0l);
    }

    @Bindable
    public double getLatitude() {
        return Optional.ofNullable(mLocation).map(Location::getLatitude).orElse(0d);
    }

    @Bindable
    public double getLongitude() {
        return Optional.ofNullable(mLocation).map(Location::getLongitude).orElse(0d);
    }

    @Bindable
    public double getAltitude() {
        return Optional.ofNullable(mLocation).map(Location::getAltitude).orElse(0d);
    }

    @Bindable
    public float getSpeed() {
        return Optional.ofNullable(mLocation).map(Location::getSpeed).orElse(0f);
    }

    @Bindable
    public float getBearing() {
        return Optional.ofNullable(mLocation).map(Location::getBearing).orElse(0f);
    }

    @Bindable
    public float getAccuracy() {
        return Optional.ofNullable(mLocation).map(Location::getAccuracy).orElse(0f);
    }

    @Bindable
    public String getProvider() {
        return Optional.ofNullable(mLocation).map(Location::getProvider).orElse("");
    }

    @Bindable
    public long getElapsedRealtimeNanos() {
        return Optional.ofNullable(mLocation).map(Location::getElapsedRealtimeNanos).orElse(0l);
    }

    public void start() {
        mApiClient.connect();
    }

    public void stop() {
        if (mApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, this);
        }
        mApiClient.disconnect();
    }

    public Observable<Location> observe() {
        return Observable.create(getOnSubscribe())
                .map(location -> {
                    setLocation(location);
                    return location;
                });
    }

    private GoogleApiClient createGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private LocationRequest createLocationRequest() {
        return LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);
    }

    private Observable.OnSubscribe<Location> getOnSubscribe() {
        return subscriber -> {
            final Listener listener = subscriber::onNext;

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

    @DebugLog
    private void setLocation(Location location) {
        mLocation = location;
        for (int fieldId : BR_LIST) {
            notifyPropertyChanged(fieldId);
        }
    }

    private interface Listener {
        void onLocationChanged(Location location);
    }
}
