package com.sunny.tinkertest

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.subjects.PublishSubject

/**
 * Created by SunShuo.
 * Date: 2019/9/11
 * Time: 4:50 PM
 */

class RxPermission {

    private var mRxFragment : RxPermission.Lazy<RxFragment>
    constructor(fragment: Fragment) {
        mRxFragment = getRxFragment(fragment.childFragmentManager)
    }
    constructor(fragmentActivity: FragmentActivity) {
        mRxFragment = getRxFragment(fragmentActivity.supportFragmentManager)
    }

    //请求权限

    private val TAG  = RxPermission::class.java.simpleName
    private fun getRxFragment(fragmentManager: FragmentManager) : RxPermission.Lazy<RxFragment>{

        return object : RxPermission.Lazy<RxFragment>{
            @Synchronized
            override fun get(): RxFragment {
                var rxFragment = fragmentManager.findFragmentByTag(TAG) as RxFragment?
                if (rxFragment == null) {
                    rxFragment = RxFragment()
                    fragmentManager.beginTransaction().add(rxFragment, TAG).commitNowAllowingStateLoss()
                }
                return rxFragment
            }
        }
    }
    private fun requestImplement(context: Context, permissions: Array<out String>) : Observable<Permission>{
        //根据权限状态，初始化权限
        val list = ArrayList<Observable<Permission>>()
        //还未请求过的权限
        val unrequestPermission = ArrayList<String>()
        permissions.forEach { it ->
            run {
                if (isGranted(it)) {
                    list.add(Observable.just(Permission(it, ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED)))

                    return@run
                }
                if (isDeny(it)) {
                    list.add(Observable.just(Permission(it, false)))
                    return@run
                }
                //将未请求过的加入
                var publishSubject = mRxFragment.get().getSubjectByPermission(it)
                if (publishSubject == null) {
                    publishSubject = PublishSubject.create<Permission>()
                    unrequestPermission.add(it)
                    mRxFragment.get().mSubjects[it] = publishSubject
                }
                list.add(publishSubject)
            }
        }
        //将list转为Array
        if (!unrequestPermission.isEmpty()) {
            val permissionsa = unrequestPermission.toArray(Array(unrequestPermission.size) {it.toString()})
            requestPermission(permissionsa)
        }
        //将发布者连起来
        return Observable.concat(Observable.fromIterable(list))

    }

    fun request(context: Context, vararg permissions: String) : Observable<Boolean> {

        return requestImplement(context, permissions).buffer(permissions.size).flatMap { it ->
            it.forEach{at ->
                if (!at.result) {
                    return@flatMap Observable.just(false)
                }
            }
            return@flatMap Observable.just(true)
        }
    }
    //将所有结果依次返回
    fun requestEach(context: Context, vararg permissions: String) : Observable<Permission> {
        return requestImplement(context, permissions)
    }

    private fun requestPermission(permissions: Array<String>) {
        mRxFragment.get().requestPermission(permissions)
    }
    @TargetApi(Build.VERSION_CODES.M)
    fun isGranted(permission: String): Boolean {
        val fragmentActivity = mRxFragment.get().activity
        return fragmentActivity?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
            /**
             * 已经申请过，但是被拒绝了
             */
    fun isDeny(permission: String): Boolean {
        val fragmentActivity = mRxFragment.get().activity
        return fragmentActivity?.packageManager?.isPermissionRevokedByPolicy(permission, fragmentActivity.packageName)
                ?: false
    }

    @FunctionalInterface
    interface Lazy<V> {
        fun get(): V
    }

}

data class Permission(var permissionName: String, var result: Boolean)


class RxFragment : Fragment() {

    //将权限和任务关联起来
    val mSubjects = HashMap<String, PublishSubject<Permission>>()

    /**
     * 请求权限
     */
    fun requestPermission(permissions: Array<out String>) {
        requestPermissions(permissions, 200)
    }

    //请求完权限之后
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            permissions.forEachIndexed {index, it ->
                run {
                    val publishSubject = mSubjects[it]
                    //将权限的请求结果发出
                    if (publishSubject != null) {
                        mSubjects.remove(it)
                        val granted = grantResults[index] == PackageManager.PERMISSION_GRANTED
                        publishSubject.onNext(Permission(it, granted))
                        publishSubject.onComplete()
                    }
                }

            }
        }

    }

    fun getSubjectByPermission(permission: String): PublishSubject<Permission>? {
        return mSubjects[permission]
    }
}


data class Task(var taskName: String, var taskResult: Boolean)

class Test {

    val taskNames = listOf("第一个需求", "第二个需求", "第三个需求")
    val answer1 = Task("第一个需求", false)
    val answer2 = Task("第二个需求", true)

    //初始化三个发布者
    val task1 = Observable.just<Task>(answer1)
    val task2 = Observable.just<Task>(answer2)
    val task3 = PublishSubject.create<Task>()

    val list = ArrayList<Observable<Task>>()

    fun init() {
        list.add(task1)
        list.add(task2)
        list.add(task3)
        val task4 = ObservableOnSubscribe<Int>{
            it.onNext(1)
        }
        task3.onNext(Task("第三个需求", true))
    }

    //将3个任务连起来
    val total = Observable.concat(Observable.fromIterable(list)).buffer(3)

    //订阅者订阅
    fun subcribe() {

        val displayHelper = total.flatMap {
            return@flatMap Observable.fromIterable(it)
        }.subscribe {
            Log.d("123", it.taskName)
        }
    }

    @SuppressLint("CheckResult")
    fun subcribe2() {
        val directionality = total.flatMap { it ->
            it.forEach { at ->
                if (!at.taskResult) {
                    return@flatMap Observable.just(false)
                }
            }
            return@flatMap Observable.just(true)
        }

        directionality.subscribe {
            if (it) {

            }
        }
        task3.onComplete()
    }


}

