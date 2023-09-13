package com.example.myapplication.Fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.Activities.Setting
import com.example.myapplication.R
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Activities.MainActivity
import com.example.myapplication.Activities.downloadSlider
import com.example.myapplication.databinding.FragmentTabfragmentBinding
import com.google.android.material.button.MaterialButton


class Tabfragment : Fragment(R.layout.fragment_tabfragment) {

    lateinit var intent: Intent
    private lateinit var binding: FragmentTabfragmentBinding
    var webviewfragment = Webview()
    var args = Bundle()
    var packageName="com.example.myapplication"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding = FragmentTabfragmentBinding.bind(view)
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)




        onClickListeners()


    }


    private fun onClickListeners() {


        binding.imageViewRemoveads.setOnClickListener{
//             intent = Intent(requireActivity(), ::class.java)
//            startActivity(intent)

        }

        binding.downloadbtn.setOnClickListener {
            Log.i("Button clicked", "Button is clicked")
            Toast.makeText(activity?.applicationContext, "Click", Toast.LENGTH_LONG).show()
        }


        binding.facebookimgv.setOnClickListener {
            args.putString("Facebook", "fb")
            webviewfragment.arguments = args

            (requireActivity() as MainActivity).setCurrentFragment(webviewfragment)
//            setCurrentFragment(webviewfragment)
        }
        binding.instaimgv.setOnClickListener {
            args.putString("Instagram", "inst")
            webviewfragment.arguments = args
            (requireActivity() as MainActivity).setCurrentFragment(webviewfragment)
//            setCurrentFragment(webviewfragment)

        }
        binding.vimeoimgv.setOnClickListener {
            args.putString("Vimeo", "vimeo")
            webviewfragment.arguments = args
            (requireActivity() as MainActivity).setCurrentFragment(webviewfragment)
//            setCurrentFragment(webviewfragment)
        }

        binding.dailymotionimgv.setOnClickListener {
            args.putString("Dailymotion", "dailym")
            webviewfragment.arguments = args
            (requireActivity() as MainActivity).setCurrentFragment(webviewfragment)
//            setCurrentFragment(webviewfragment)
        }
        binding.twitterimgv.setOnClickListener {
            args.putString("Twitter", "twitter")
            webviewfragment.arguments = args
            (requireActivity() as MainActivity).setCurrentFragment(webviewfragment)
//            setCurrentFragment(webviewfragment)
        }
        binding.wallpaperimgv.setOnClickListener {
            args.putString("Wallpaper", "wallpaper")
            webviewfragment.arguments = args
            (requireActivity() as MainActivity).setCurrentFragment(webviewfragment)
//            setCurrentFragment(webviewfragment)
        }
        binding.whatsappimgv.setOnClickListener {
            args.putString("Whatsapp", "whatsapp")
            webviewfragment.arguments = args
            (requireActivity() as MainActivity).setCurrentFragment(webviewfragment)
//            setCurrentFragment(webviewfragment)
        }
        binding.removeadsimgv.setOnClickListener {
//            setCurrentFragment(packagesfragment)
            intent = Intent(activity?.applicationContext, MainActivity::class.java)
            intent.putExtra("Removeads", "removeads")
            startActivity(intent)


        }
        binding.downloadbtn.setOnClickListener {
            activity.let {
                val intent = Intent(it, downloadSlider::class.java)
                if (it != null) {
                    it.startActivity(intent)
                }
            }
        }
        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                activity?.finishAffinity()
                System.exit(0)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.optionmenu_tab, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_tab -> {
                // Handle Item 1 selection
                return true
            }

            R.id.share_optionmenu -> {
                val intent= Intent()
                intent.action=Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT,"Hey Check out this Great app:")
                intent.type="text/plain"
                startActivity(Intent.createChooser(intent,"Share To:"))
                return true
            }
            R.id.history -> {
                // Handle Item 1 selection
                return true
            }

            R.id.copy_link -> {
                // Handle Item 2 selection
                return true
            }
            R.id.bookmarks -> {
                // Handle Item 1 selection
                return true
            }

            R.id.addbookmark -> {
                // Handle Item 2 selection
                return true
            }
            R.id.desktopsite -> {
                // Handle Item 1 selection
                return true
            }

            R.id.sharewithfriends -> {
               return true
            }
            R.id.scan_ad -> {
                // Handle Item 2 selection
                return true
            }
            R.id.familyapps -> {
                // Handle Item 1 selection
                return true
            }

            R.id.settings_optionmenu -> {
                // Handle Item 2 selection
                return true
            }
            R.id.rateus ->{
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                }

                return true
            }
            R.id.exit_optionmenu -> {
                activity?.finishAffinity()
                System.exit(0)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}