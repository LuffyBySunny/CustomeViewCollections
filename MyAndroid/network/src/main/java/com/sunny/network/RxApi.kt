package com.sunny.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by SunShuo.
 * Date: 2019-09-29
 * Time: 15:14
 */
public interface RxApi {
    @GET("/hhh")
    fun getAll(@QueryMap map: Map<String, String>) : Observable<String>
}