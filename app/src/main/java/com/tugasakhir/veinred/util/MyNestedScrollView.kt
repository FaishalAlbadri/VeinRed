package com.tugasakhir.veinred.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.core.widget.NestedScrollView


class MyNestedScrollView : NestedScrollView {

    private var slop: Int = 0
    private lateinit var mInitialMotionX: String
    private lateinit var mInitialMotionY: String

    constructor(context: Context) : super(context) {
        init(context);
    }

    private fun init(context: Context) {
        val config = ViewConfiguration.get(context)
        slop = config.scaledEdgeSlop
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context);
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context);
    }

    private var xDistance: Float = 1.0F
    private var yDistance: Float = 1.0F
    private var lastX: Float = 1.0F
    private var lastY: Float = 1.0F

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x: Float = ev.getX();
        val y: Float = ev.getY();
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    yDistance = 0f
                    xDistance = yDistance
                }
                lastX = ev.x
                lastY = ev.y
                computeScroll()
            }
            MotionEvent.ACTION_MOVE -> {
                val curX = ev.x
                val curY = ev.y
                xDistance += Math.abs(curX - lastX)
                yDistance += Math.abs(curY - lastY)
                lastX = curX
                lastY = curY
                if (xDistance > yDistance) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}