package com.sunny.tinkertest.tablayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sunny.tinkertest.R
import kotlinx.android.synthetic.main.activity_tab.*

class TabActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        val titles = listOf(
                "第一个","第二个","第三个",
                "第四个","五","第六个",
                "第七个","八","第九个",
                "第十个","十一","第十二个",
                "第十三个","十四","第十五个"
        )
        val fragments = ArrayList<Fragment>()
        titles.forEach {
            val fragment = MyFragment.newFragment(it)
            fragments.add(fragment)
        }
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = MyAdapter(supportFragmentManager, fragments, titles)
    }
}
