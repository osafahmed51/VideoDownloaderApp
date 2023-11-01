package com.example.myapplication.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.Fragments.FinishFragment
import com.example.myapplication.Fragments.Tabfragment
import com.example.myapplication.Fragments.ProgressFragment
import com.example.myapplication.R
import com.example.myapplication.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    var progressFragment=ProgressFragment()

    private lateinit var fragmentManager: FragmentManager

    var tabfragment=Tabfragment()

    var downloadfrag=FinishFragment()

    private val REQUEST_CODE_STORAGE_PERMISSION = 100

    val utilclass=Utils()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav= findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        fragmentManager=supportFragmentManager


        setCurrentFragment(tabfragment)

        onclickListeners();

        requestStoragePermission()


    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE_PERMISSION)
            }
        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE_PERMISSION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val rootDirPath = utilclass.getRootDirPath(this)
                } else {
                    Toast.makeText(this,"Permission denied! you will not get media in your gallery.",Toast.LENGTH_LONG).show()

                }
            }
        }
    }



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

