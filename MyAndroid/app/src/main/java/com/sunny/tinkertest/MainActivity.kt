package com.sunny.tinkertest

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.text.TextUtils
import com.sunny.tinkertest.bean.CityBean
import com.sunny.tinkertest.bean.NewsBean
import com.sunny.tinkertest.bean.SectionEnty
import com.sunny.tinkertest.recyclerview.MyAdapter
import com.sunny.tinkertest.recyclerview.MyItemDecoration
import com.sunny.tinkertest.recyclerview.NativeItemDecoration
import com.sunny.tinkertest.recyclerview.QuickAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), MainActivityContact.View {

    private val timer : Timer by lazy {
        Timer()
    }
    //延迟加载
    val presenter : MainActivityPresenter by lazy {
        MainActivityPresenter(this)
    }
    private val adapter = MyAdapter()
    private val decoration : MyItemDecoration by lazy {
        MyItemDecoration(this, adapter.datas)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = adapter
        val datas = ArrayList<CityBean>()

        for (i in 0..4) {
            datas.add(CityBean("$i", "${i+1}"))
            datas.add(CityBean("$i", "${i+2}"))
        }
        adapter.datas = datas
        decoration.headerheight = 60
        recyclerView.addItemDecoration(decoration)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(recyclerView)
        val mTitles = arrayOf("上海",
                "头条推荐", "生活", "娱乐八卦",
                "体育", "段子", "美食", "电影", "科技",
                "搞笑", "社会", "财经", "时尚", "汽车", "军事",
                "小说", "育儿", "职场", "萌宠", "游戏", "健康", "动漫", "互联网")

        mTitles.forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it))
        }
        /*timer.schedule(object : TimerTask(){
            override fun run() {
                runOnUiThread {
                    clock.doInvalidate()
                }
            }
        },0,1000)
*/
    }

    /**
     * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo、三星都可以）
     *
     * @return
     */
    private fun getDeviceInfo(): String {
        val brand = Build.BRAND
        if (TextUtils.isEmpty(brand)) return "navigationbar_is_min"

        return if (brand.equals("HUAWEI", ignoreCase = true) || "HONOR" == brand) {
            "navigationbar_is_min"
        } else if (brand.equals("XIAOMI", ignoreCase = true)) {
            "force_fsg_nav_bar"
        } else if (brand.equals("VIVO", ignoreCase = true)) {
            "navigation_gesture_on"
        } else if (brand.equals("OPPO", ignoreCase = true)) {
            "navigation_gesture_on"
        } else if (brand.equals("samsung", ignoreCase = true)) {
            "navigationbar_hide_bar_enabled"
        } else {
            "navigationbar_is_min"
        }
    }

    /**
     * 判断设备是否存在NavigationBar
     *
     * @return true 存在, false 不存在
     */
    fun deviceHasNavigationBar(): Boolean {
        var haveNav = false
        try {
            //1.通过WindowManagerGlobal获取windowManagerService
            // 反射方法：IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
            val windowManagerGlobalClass = Class.forName("android.view.WindowManagerGlobal")
            val getWmServiceMethod = windowManagerGlobalClass.getDeclaredMethod("getWindowManagerService")
            getWmServiceMethod.isAccessible = true
            //getWindowManagerService是静态方法，所以invoke null
            val iWindowManager = getWmServiceMethod.invoke(null)

            //2.获取windowMangerService的hasNavigationBar方法返回值
            // 反射方法：haveNav = windowManagerService.hasNavigationBar();
            val iWindowManagerClass = iWindowManager.javaClass
            val hasNavBarMethod = iWindowManagerClass.getDeclaredMethod("hasNavigationBar")
            hasNavBarMethod.isAccessible = true
            haveNav = hasNavBarMethod.invoke(iWindowManager) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return haveNav
    }

    private fun navigationGestureEnabled(context: Context): Boolean {
        val `val` = Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0)
        return `val` != 0
    }

    fun hasNavigationBar(context: Context): Boolean {
        //navigationGestureEnabled()从设置中取不到值的话，返回false，因此也不会影响在其他手机上的判断
        return deviceHasNavigationBar() && !navigationGestureEnabled(context)
    }

    fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        if (hasNavigationBar(context)) {
            val res = context.resources
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    fun getForceNavigationBarHeight(context: Context):Int {
        val res = context.resources
        var result = 0
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getStatusBarHeight(context: Context?): Int {
        var statusBarHeight = 0
        if (context != null) {
            try {
                val c = Class.forName("com.android.internal.R\$dimen")
                val o = c.newInstance()
                val field = c.getField("status_bar_height")
                val x = field.get(o) as Int
                statusBarHeight = context.resources.getDimensionPixelSize(x)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return statusBarHeight
    }
}
