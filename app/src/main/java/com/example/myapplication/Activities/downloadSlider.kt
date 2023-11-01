package com.example.myapplication.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Adapters.SliderAdpater
import com.example.myapplication.Model.Sliderdata
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDownloadSliderBinding
import com.smarteist.autoimageslider.SliderView

class downloadSlider : AppCompatActivity() {

    lateinit var binding:ActivityDownloadSliderBinding

    val sliderArrayList= ArrayList<Sliderdata>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDownloadSliderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        onClickListener()

        sliderFunc()



    }

    private fun sliderFunc() {
        sliderArrayList.add(Sliderdata("Image 1",R.drawable.sliderimg1))
        sliderArrayList.add(Sliderdata("Image 1",R.drawable.sliderimg2))
        sliderArrayList.add(Sliderdata("Image 1",R.drawable.sliderimg3))

        val sliderAdpater= SliderAdpater(sliderArrayList)
        binding.imgslider.setSliderAdapter(sliderAdpater)
        binding.imgslider.isAutoCycle
        binding.imgslider.startAutoCycle()

    }

    private fun onClickListener() {

        binding.sliderbackbtn.setOnClickListener {
            val intent= Intent(this@downloadSlider,MainActivity::class.java)
            startActivity(intent)
        }

    }
}