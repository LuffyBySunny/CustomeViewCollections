package com.sunny.tinkertest.bean

import com.chad.library.adapter.base.entity.SectionEntity

/**
 * Created by Android Studio.
 * Date: 2019/8/16
 * Time: 2:50 PM
 */
class SectionEnty(isHeader : Boolean, header : String, newsBean: NewsBean)  : SectionEntity<NewsBean>(isHeader, header){
    init {
        t = newsBean
    }
}
