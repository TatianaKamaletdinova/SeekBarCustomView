package com.customview.seekbar

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var noteColor: Int = Color.TRANSPARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val color = findViewById<TextView>(R.id.tvInfoSelectColor)

        val colorSlider = findViewById<ColorSlider>(R.id.colorSlider)
        colorSlider.selectedColorValue = noteColor
        colorSlider.addListener {
            color.text = it.toString()
        }

        val colorDialView = findViewById<ColorDialView>(R.id.colorDialView)
        colorDialView.selectedColorValue = noteColor
        colorDialView.addListener {
            color.text = it.toString()
        }
    }
}