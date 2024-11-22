package com.example.mobileapp.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.Activity.LoginFragment.ChooseFragment;
import com.example.mobileapp.Activity.LoginFragment.LoginFragment;
import com.example.mobileapp.R;

public class LoginActivity extends AppCompatActivity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Kiểm tra nếu chưa có Fragment nào trong container thì thêm LoginFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
        }
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Kiểm tra flag từ Intent
        boolean skipLoginFragment = getIntent().getBooleanExtra("Choose", false);

        if (savedInstanceState == null) {
            if (skipLoginFragment) {
                // Bỏ qua LoginFragment và chuyển trực tiếp đến ChooseFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ChooseFragment())
                        .commit();
            } else {
                // Mặc định thêm LoginFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .commit();
            }
        }
    }

}