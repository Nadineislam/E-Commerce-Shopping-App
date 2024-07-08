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
import com.example.e_commerce.auth_feature.presentation.activity.AuthActivity
import com.example.e_commerce.auth_feature.presentation.viewmodel.UserViewModel
import com.example.e_commerce.databinding.ActivityMainBinding
import com.example.e_commerce.shopping_feature.presentation.adapters.HomeViewPagerAdapter
import com.example.e_commerce.shopping_feature.presentation.fragments.AccountFragment
import com.example.e_commerce.shopping_feature.presentation.fragments.CartFragment
import com.example.e_commerce.shopping_feature.presentation.fragments.ExploreFragment
import com.example.e_commerce.shopping_feature.presentation.fragments.HomeFragment
import com.example.e_commerce.shopping_feature.presentation.fragments.OfferFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val userViewModel by viewModels<UserViewModel>()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)
        val isLoggedIn = runBlocking { userViewModel.isUserLoggedIn().first() }
        if (!isLoggedIn) {
            goToAuthActivity()
            return
        }
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initViews()

    }

    private fun initViews() {
        initViewPager()
        initBottomNavigationView()

    }

    private fun initBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> binding.homeViewPager.currentItem = 0
                R.id.exploreFragment -> binding.homeViewPager.currentItem = 1
                R.id.cartFragment -> binding.homeViewPager.currentItem = 2
                R.id.offerFragment -> binding.homeViewPager.currentItem = 3
                R.id.accountFragment -> binding.homeViewPager.currentItem = 4
            }
            true
        }
    }

    private fun initViewPager() {
        val fragments = listOf(
            HomeFragment(),
            ExploreFragment(),
            CartFragment(),
            OfferFragment(),
            AccountFragment()
        )

        binding.homeViewPager.offscreenPageLimit = fragments.size
        binding.homeViewPager.adapter = HomeViewPagerAdapter(this, fragments)
        binding.homeViewPager.registerOnPageChangeCallback(
            object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNavigationView.menu.getItem(position).isChecked = true
                }
            }
        )
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            runBlocking { userViewModel.getUserDetails().first() }
            userViewModel.userDetailsState.collect {
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
        finish()
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