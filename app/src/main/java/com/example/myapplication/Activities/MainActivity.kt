package com.example.myapplication.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.Fragments.DownloadFragment
import com.example.myapplication.Fragments.Tabfragment
import com.example.myapplication.Fragments.Webview
import com.example.myapplication.Fragments.packages
import com.example.myapplication.Fragments.progressFragment
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    var progressFragment=progressFragment()
    lateinit var fragmentManager: FragmentManager
    var tabfragment=Tabfragment()
    var downloadfrag=DownloadFragment()
    var intent_fb=Intent()
    var packagesfragment=packages()
    var intntt : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav= findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        fragmentManager=supportFragmentManager


        setCurrentFragment(tabfragment)
//        gettingIntentt()

        onclickListeners();

    }





//    private fun gettingIntentt()
//    {
//        intntt= intent_fb.getStringExtra("Removeads")
//        if(intntt.equals("removeads"))
//        {
//            setCurrentFragment(packagesfragment)
//        }
//
//        else{
//
//            setCurrentFragment(tabfragment);
//        }
//
//    }


    private fun onclickListeners() {


        bottomNav.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.tab -> {

                    setCurrentFragment(tabfragment)
                    true

                }

                R.id.progress -> {setCurrentFragment(progressFragment)
                    true}
                R.id.finished -> {setCurrentFragment(downloadfrag)
                    true}

                else -> {
                    setCurrentFragment(tabfragment)
                    true
                }
            }

        }

        }

    fun setCurrentFragment(fragment: Fragment){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


}

