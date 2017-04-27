package net.kemoke.emergency.api;

import net.kemoke.emergency.ApiLocation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationService {
    @POST("location/create")
    Call<String> sendLocation(@Body ApiLocation apiLocation);
}
