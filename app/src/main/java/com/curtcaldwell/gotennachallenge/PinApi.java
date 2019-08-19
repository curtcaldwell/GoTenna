package com.curtcaldwell.gotennachallenge;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PinApi {

    @GET("get_map_pins.php")
    Call<List<Pin>> getPins();
}

