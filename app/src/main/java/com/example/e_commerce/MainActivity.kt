package com.example.e_commerce

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.auth_feature.representation.activity.AuthActivity
import com.example.e_commerce.auth_feature.representation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch(Main) {
            val isLoggedIn = userViewModel.isUserLoggedIn().first()
            if (isLoggedIn) {
                setContentView(R.layout.activity_main)
            } else {
                goToAuthActivity()
            }
        }
    }

    private fun goToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val options = ActivityOptions.makeCustomAnimation(
            this, android.R.anim.fade_in, android.R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
    }

    private fun initSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView, View.TRANSLATION_Y, 0f, -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 1000L

                slideUp.doOnEnd { splashScreenView.remove() }

                slideUp.start()
            }
        } else {
            setTheme(R.style.Theme_E_Commerce)
        }
    }
}