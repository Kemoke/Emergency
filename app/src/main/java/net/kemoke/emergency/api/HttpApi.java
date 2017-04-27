package net.kemoke.emergency.api;

import android.location.Location;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpApi {
    private static LocationService locationService;
    public static void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://52.166.59.88:1337/")
                .build();
        locationService = retrofit.create(LocationService.class);
    }

    public static LocationService getLocationService() {
        return locationService;
    }
}
