package com.example.myapplication.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Model.Sliderdata
import com.example.myapplication.R
import com.google.android.material.slider.Slider
import com.smarteist.autoimageslider.SliderView

class downloadSlider : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_slider)

        val sliderArrayList= ArrayList<Sliderdata>()

        val SliderView=findViewById<SliderView>(R.id.imgslider)

        sliderArrayList.add(Sliderdata("Image 1",R.drawable.sliderimg1))
        sliderArrayList.add(Sliderdata("Image 1",R.drawable.sliderimg2))
        sliderArrayList.add(Sliderdata("Image 1",R.drawable.sliderimg3))

        val sliderAdpater=SliderAdpater(sliderArrayList)
        SliderView.setSliderAdapter(sliderAdpater)
        SliderView.isAutoCycle
        SliderView.startAutoCycle()


    }
}