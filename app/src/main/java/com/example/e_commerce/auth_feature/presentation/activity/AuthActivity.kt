package com.example.e_commerce.auth_feature.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_commerce.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}