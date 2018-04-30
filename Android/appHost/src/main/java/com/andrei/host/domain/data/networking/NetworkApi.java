package com.andrei.host.domain.data.networking;

import com.andrei.host.domain.data.model.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Andrei on 25/03/2018.
 */

public interface NetworkApi {

    @GET("igoregardia/Autos-Android-Assessment/master/autos.json")
    Call<List<Car>> getCarList();
}
