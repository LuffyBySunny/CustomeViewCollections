package com.sunny.tinkertest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sunny.tinkertest.tablayout.Main2Activity
import com.sunny.tinkertest.tablayout.TabActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainActivityContact.View {


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        gotoTabActivity.setOnClickListener{

           val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)

        }



    }


}

