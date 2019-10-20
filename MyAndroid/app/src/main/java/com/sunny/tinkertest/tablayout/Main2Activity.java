package com.sunny.tinkertest.tablayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sunny.tinkertest.R;
import com.sunny.tinkertest.fragment.BlankFragment;
import com.sunny.tinkertest.safetoast.ToastUtil;

public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Fragment fragment = BlankFragment.newInstance("1", "2");
                        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commitAllowingStateLoss();
                    }
                }, 1000);



            }
        });
    }
}
