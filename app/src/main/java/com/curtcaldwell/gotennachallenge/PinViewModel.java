package com.curtcaldwell.gotennachallenge;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PinViewModel extends ViewModel {

    public static final String MARKER_IMAGE = "marker-15";

    public static final String BASE_URL = "https://annetog.gotenna.com/development/scripts/";

    public static final String ACCESS_CODE = "mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7";

    public ArrayList<Pin> pinArrayList = new ArrayList();
    public Map<Symbol, Pin> symbolMap = new HashMap<>();


    private MutableLiveData<List<Pin>> pins;

    public LiveData<List<Pin>> getPins() {
        if (pins == null) {
            pins = new MutableLiveData<>();
        }
        return pins;
    }

    public void setPins(List<Pin> p) {
        pins.setValue(p);
    }

    public void loadPinData() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PinApi pinApi = retrofit.create(PinApi.class);

        Call<List<Pin>> call = pinApi.getPins();

        call.enqueue(new Callback<List<Pin>>() {
            @Override
            public void onResponse(Call<List<Pin>> call, Response<List<Pin>> response) {
                setPins(response.body());
            }

            @Override
            public void onFailure(Call<List<Pin>> call, Throwable t) {
                Log.d("On failure: ", t.getMessage());
            }
        });
    }
}
