package com.example.e_commerce.core.views


import android.content.Context
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R

fun sliderIndicatorsView(
    context: Context,
    viewPager2: ViewPager2,
    indicatorLayout: LinearLayout,
    indicators: MutableList<CircleView>,
    count: Int,
    onCircleClick: (Int) -> Unit = {}
) {
    for (i in 0 until count) {
        val circleView = CircleView(context)
        val params = LinearLayout.LayoutParams(
            20, 20
        )
        params.setMargins(8, 0, 8, 0) // Margin between circles
        circleView.layoutParams = params
        circleView.setRadius(10f) // Set radius
        circleView.setColor(
            if (i == 0) context.getColor(R.color.primary_color) else context.getColor(
                R.color.neutral_grey
            )
        )
        circleView.setOnClickListener {
            viewPager2.setCurrentItem(i, true)
            onCircleClick(i)
        }
        indicators.add(circleView)
        indicatorLayout.addView(circleView)
    }
}

fun updateIndicators(
    context: Context, indicators: MutableList<CircleView>, position: Int
) {
    for (i in 0 until indicators.size) {
        indicators[i].setColor(
            if (i == position) context.getColor(R.color.primary_color) else context.getColor(
                R.color.neutral_grey
            )
        )
    }
}