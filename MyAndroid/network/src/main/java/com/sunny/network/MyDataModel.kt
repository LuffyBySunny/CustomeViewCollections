package com.sunny.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by SunShuo.
 * Date: 2019-09-30
 * Time: 08:53
 */
class MyDataModel : ViewModel() {
    val liveData = MutableLiveData<BaseModel>()
}