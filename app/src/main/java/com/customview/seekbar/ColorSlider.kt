package com.customview.seekbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.SeekBar
import androidx.core.content.ContextCompat

@SuppressLint("AppCompatCustomView")
class ColorSlider @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.seekBarStyle,
    defStyleRes: Int = 0
) : SeekBar(context, attributeSet, defStyleAttr, defStyleRes) {

    private var colors: ArrayList<Int>
    private var listeners: ArrayList<(Int) -> Unit> = arrayListOf()
    private val w = getPixelValueFromDP(16f)
    private val h = getPixelValueFromDP(16f)
    private val halfW = w / 2f
    private val halfH = h / 2f
    private val paint = Paint()
    private var w2 = 0
    private var h2 = 0
    private var halfW2 = 1
    private var halfH2 = 1
    private var noColorDrawable: Drawable? = null
        set(value) {
            w2 = value?.intrinsicWidth ?: 0
            h2 = value?.intrinsicHeight ?: 0
            halfW2 = w2 / 2
            halfH2 = h2 / 2
            value?.setBounds(-halfW2, -halfH2, halfW2, halfH2)
            field = value
        }

    var selectedColorValue: Int = android.R.color.transparent
        set(value) {
            val index = colors.indexOf(value)
            progress = if (index == -1) {
                0
            } else {
                index
            }
        }

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ColorSlider)
        try {
            colors = typedArray.getTextArray(R.styleable.ColorSlider_colors)
                .map {
                    Log.e("Log", "ColorSlider ${Color.parseColor(it.toString())}")
                    Color.parseColor(it.toString())
                } as ArrayList<Int>
        } finally {
            typedArray.recycle()
        }
        colors.add(0, Color.TRANSPARENT)
        max = colors.size - 1
        progressBackgroundTintList =
            ContextCompat.getColorStateList(context, android.R.color.transparent)
        progressTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
        splitTrack = false
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + getPixelValueFromDP(16f).toInt())
        thumb = ContextCompat.getDrawable(context, R.drawable.ic_color_slider_thumb)
        noColorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_no_color)

        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                listeners.forEach {
                    if (progress > colors.size - 1) {
                        it(colors[0])
                    } else {
                        it(colors[progress])
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTickMarks(canvas)
    }

    private fun drawTickMarks(canvas: Canvas?) {
        canvas?.let {
            val count = colors.size
            val saveCount = canvas.save()
            canvas.translate(paddingLeft.toFloat(), (height / 2).toFloat() + getPixelValueFromDP(16f))
            if (count > 1) {
                for (i in 0 until count) {
                    val spacing = (width - paddingLeft - paddingRight) / (count - 1).toFloat()
                    if (i == 0) {
                        noColorDrawable?.draw(canvas)
                    } else {
                        paint.color = colors[i]
                        canvas.drawRect(
                            -halfW, -halfH,
                            halfW, halfH, paint
                        )
                    }

                    canvas.translate(spacing, 0f)
                }
                canvas.restoreToCount(saveCount)
            }
        }
    }

    private fun getPixelValueFromDP(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            context.resources.displayMetrics
        )
    }

    fun addListener(function: (Int) -> Unit) {
        listeners.add(function)
    }
}