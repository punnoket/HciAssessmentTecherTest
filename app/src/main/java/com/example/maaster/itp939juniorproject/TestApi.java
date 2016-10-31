package com.example.maaster.itp939juniorproject;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Administrator on 19/9/2559.
 */
public interface TestApi {

    @GET("api/status/")
    Call<People> getUser();
}
