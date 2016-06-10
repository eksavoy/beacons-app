package com.isen.urba.beaconapp.service;

import com.isen.urba.beaconapp.pojo.MongoDevise;
import com.isen.urba.beaconapp.pojo.ResponseActionMongoDevise;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by isen on 09/06/2016.
 */
public interface DeviseService {

    @GET("devises/")
    Call<List<MongoDevise>> deviseList();

    @GET("devises/{id}")
    Call<List<MongoDevise>> devise(@Path("id") String id);

    @POST("devises/")
    Call<ResponseActionMongoDevise> insertDevise(@Body MongoDevise devise);

    @PUT("devises/{id}")
    Call<ResponseActionMongoDevise> updateDevise(@Path("id") String id,@Body MongoDevise devise);

    @DELETE("devises/{id}")
    Call<ResponseActionMongoDevise> deleteDevise(@Path("id") String id);
}
