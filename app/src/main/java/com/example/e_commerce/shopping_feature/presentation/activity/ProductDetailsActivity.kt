package com.example.e_commerce.shopping_feature.presentation.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.R
import com.example.e_commerce.shopping_feature.presentation.models.ProductUIModel
import com.example.e_commerce.shopping_feature.presentation.viewmodel.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {
    val productUiModel: ProductUIModel by lazy {
        val product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PRODUCT_UI_MODEL_EXTRA, ProductUIModel::class.java)
        } else {
            intent.getParcelableExtra(PRODUCT_UI_MODEL_EXTRA)
        }

        product ?: throw IllegalArgumentException("ProductUIModel is required")
    }

    val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_details)


        lifecycleScope.launch {
            viewModel.productDetailsState.collectLatest {
            }
        }
    }

    companion object {
        private const val TAG = "ProductDetailsActivity"
        const val PRODUCT_UI_MODEL_EXTRA = "PRODUCT_UI_MODEL_EXTRA"
    }
}