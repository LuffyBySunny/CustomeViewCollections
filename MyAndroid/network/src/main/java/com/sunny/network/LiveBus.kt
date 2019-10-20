package com.sunny.network

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import java.util.HashMap

/**
 * Created by SunShuo.
 * Date: 2019-09-30
 * Time: 10:15
 */
object LiveBus {
    private final val bus = HashMap<String, MutableLiveData<Any>>()


    public fun <T>getChannel(key : String) : MutableLiveData<T>? {
        if (!bus.containsKey(key)) {
            bus[key] = MutableLiveData<Any>()
        }
        //不会有错
        return bus[key] as MutableLiveData<T>
    }

}