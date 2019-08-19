package com.sunny.tinkertest.recyclerview

import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sunny.tinkertest.R
import com.sunny.tinkertest.bean.SectionEnty
import java.util.*

/**
 * Created by Android Studio.
 * Date: 2019/8/16
 * Time: 2:44 PM
 */
class QuickAdapter(layoutId : Int, headerId : Int, data : ArrayList<SectionEnty>) : BaseSectionQuickAdapter<SectionEnty, BaseViewHolder>(layoutId, headerId, data) {
    override fun convertHead(helper: BaseViewHolder?, item: SectionEnty?) {
        helper?.setText(R.id.groupText, item?.t?.title)
    }

    override fun convert(helper: BaseViewHolder?, item: SectionEnty?) {
        helper?.setText(R.id.tag, item?.t?.title)
    }


}