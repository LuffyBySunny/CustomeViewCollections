package com.sunny.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.Observable.create
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import tese.tese

/**
 * Created by SunShuo.
 * Date: 2019-09-29
 * Time: 15:13
 */
object RxNetWork {


    private val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.baidu.com")
            .build()

    @Throws(Exception::class)
    fun get(map: Map<String, String>) : Observable<String>  {

        val rxApi = retrofit.create(RxApi::class.java)
        val observable = rxApi.getAll(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        return observable
    }



    fun init() {
        //被观察者
        val observable = create(ObservableOnSubscribe<String> {
            it.onNext("hhh")
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        val observer = object : Observer<String> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: String) {
            }

            override fun onError(e: Throwable) {
            }
        }

        observable.subscribe(observer)

    }


}