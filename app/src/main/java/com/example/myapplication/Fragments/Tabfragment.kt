package com.example.myapplication.Fragments

import Gone
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import isNetworkConnected
import loadNativeAd
import com.example.myapplication.Activities.BookmarksActivity
import com.example.myapplication.Activities.ExitActivity
import com.example.myapplication.Activities.FeedbackActivity
import com.example.myapplication.Activities.HistoryClass
import com.example.myapplication.Activities.MainActivity
import com.example.myapplication.Activities.RemoveAdsActivity
import com.example.myapplication.Activities.Setting
import com.example.myapplication.Activities.StatusActivity
import com.example.myapplication.Activities.downloadSlider
import com.example.myapplication.utils.AppDataBase
import com.example.myapplication.Model.BookmarkEntity
import com.example.myapplication.Model.Entry
import com.example.myapplication.Model.LinkEntity
import com.example.myapplication.R
import com.example.myapplication.SharePreferenceClass
import com.example.myapplication.XmlParser
import com.example.myapplication.databinding.FragmentTabfragmentBinding
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream


class Tabfragment : Fragment(R.layout.fragment_tabfragment) {

    lateinit var intent: Intent

    private lateinit var binding: FragmentTabfragmentBinding

    var webviewfragment = Webview()

    var packageName="com.example.myapplication"

    private var args : Bundle?=null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        MobileAds.initialize(requireContext())

        binding = FragmentTabfragmentBinding.bind(view)

        showNativeAd()

        val toolbar = binding.toolbar

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        onClickListeners()

        mainActivityBackPress()


    }


        private fun showNativeAd() {
            with(binding) {
                if (requireContext().isNetworkConnected()) {
                    requireContext().loadNativeAd(
                        frame.adContainer,
                        frame.ShimmerContainerSmall,
                        frame.FrameLayoutSmall,
                        com.example.myapplication.R.layout.small_native_ad,
                        getString(R.string.native_id)
                    )
                }else {
                    nativeAd.Gone()
                }

            }
        }



    private fun onClickListeners() {


        binding.imageViewRemoveads.setOnClickListener{

            intent = Intent(activity?.applicationContext, RemoveAdsActivity::class.java)
            intent.putExtra("Removeads", "removeads")
            startActivity(intent)
        }

        binding.downloadbtn.setOnClickListener {
            Log.i("Button clicked", "Button is clicked")
            Toast.makeText(activity?.applicationContext, "Click", Toast.LENGTH_LONG).show()
        }


        binding.facebookimgv.setOnClickListener {

            val targetUrl="https://www.facebook.com/login/"
            LoadFb(targetUrl)

        }
        binding.instaimgv.setOnClickListener {
            val targetUrlinsta="https://www.instagram.com/accounts/login/"
            LoadInsta(targetUrlinsta)
        }
        binding.vimeoimgv.setOnClickListener {
            loadVimeo()
        }

        binding.dailymotionimgv.setOnClickListener {

            loadDailymotion()

        }
        binding.twitterimgv.setOnClickListener {

            loadTwitter()
        }
        binding.wallpaperimgv.setOnClickListener {
            loadwallpaper()

        }
        binding.whatsappimgv.setOnClickListener {
            intent = Intent(requireContext(),StatusActivity::class.java)
            startActivity(intent)

        }
        binding.removeadsimgv.setOnClickListener {

            intent = Intent(activity?.applicationContext, RemoveAdsActivity::class.java)
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

        binding.backbtntabfragmnt.setOnClickListener {

            val intnt=Intent(requireContext(),ExitActivity::class.java)
            startActivity(intnt)

        }


        binding.imageViewRemoveads.setOnClickListener {
            val intentremoveads=Intent(requireContext(),RemoveAdsActivity::class.java)
            startActivity(intentremoveads)
        }

        binding.searchIcon.setOnClickListener {
            loadSearchBox()
        }


        binding.feedbacktextview.setOnClickListener {
            val intentfeedbacktext=Intent(requireContext(),FeedbackActivity::class.java)
            startActivity(intentfeedbacktext)
        }

        binding.feedbackimgview.setOnClickListener {
            val intentfeedbackimg=Intent(requireContext(),FeedbackActivity::class.java)
            startActivity(intentfeedbackimg)
        }



        onBackpresfunc()


    }

      fun loadwallpaper() {


        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.pluginState = WebSettings.PluginState.ON
        binding.webview.settings.builtInZoomControls = true
        binding.webview.settings.displayZoomControls = true
        binding.webview.settings.useWideViewPort = true
        binding.webview.settings.loadWithOverviewMode = true
        binding.webview.settings.userAgentString = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Mobile Safari/537.36"
        binding.webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.downloadbtn.visibility = View.INVISIBLE
          binding.feedbacktextview.visibility=View.INVISIBLE
          binding.cardView2.visibility=View.INVISIBLE
          binding.feedbackimgview.visibility=View.INVISIBLE
        binding.webview.visibility = View.VISIBLE
        binding.webview.loadUrl("https://www.freepik.com/free-photos-vectors/wallpaper")

        backArrowTabFrag()

          onBackpresfunc()


    }
    fun loadVimeo() {


        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.pluginState = WebSettings.PluginState.ON
        binding.webview.settings.builtInZoomControls = true
        binding.webview.settings.displayZoomControls = true
        binding.webview.settings.useWideViewPort = true
        binding.webview.settings.loadWithOverviewMode = true
        binding.webview.settings.userAgentString = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Mobile Safari/537.36"
        binding.webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.downloadbtn.visibility = View.INVISIBLE
        binding.feedbacktextview.visibility=View.INVISIBLE
        binding.cardView2.visibility=View.INVISIBLE
        binding.feedbackimgview.visibility=View.INVISIBLE
        binding.webview.visibility = View.VISIBLE
        binding.webview.loadUrl("https://vimeo.com/log_in")

        backArrowTabFrag()

        onBackpresfunc()


    }

    fun loadDailymotion() {


        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.pluginState = WebSettings.PluginState.ON
        binding.webview.settings.builtInZoomControls = true
        binding.webview.settings.displayZoomControls = true
        binding.webview.settings.useWideViewPort = true
        binding.webview.settings.loadWithOverviewMode = true
        binding.webview.settings.userAgentString = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Mobile Safari/537.36"
        binding.webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.downloadbtn.visibility = View.INVISIBLE
        binding.feedbacktextview.visibility=View.INVISIBLE
        binding.cardView2.visibility=View.INVISIBLE
        binding.feedbackimgview.visibility=View.INVISIBLE
        binding.webview.visibility = View.VISIBLE
        binding.webview.loadUrl("https://www.dailymotion.com/signin")

        backArrowTabFrag()

        onBackpresfunc()


    }

    fun loadTwitter() {


        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.pluginState = WebSettings.PluginState.ON
        binding.webview.settings.builtInZoomControls = true
        binding.webview.settings.displayZoomControls = true
        binding.webview.settings.useWideViewPort = true
        binding.webview.settings.loadWithOverviewMode = true
        binding.webview.settings.userAgentString = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Mobile Safari/537.36"
        binding.webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.downloadbtn.visibility = View.INVISIBLE
        binding.feedbacktextview.visibility=View.INVISIBLE
        binding.cardView2.visibility=View.INVISIBLE
        binding.feedbackimgview.visibility=View.INVISIBLE
        binding.webview.visibility = View.VISIBLE
        binding.webview.loadUrl("https://twitter.com/i/flow/login")

        backArrowTabFrag()

        onBackpresfunc()


    }


    private fun loadSearchBox() {

        val sharepref = SharePreferenceClass(requireContext())

        val targetUrl = binding.searchTabfrag.text.toString().trim()

        if (targetUrl.isNotEmpty()) {

            if (isURL(targetUrl)) {
                binding.webview.loadUrl(targetUrl)
                binding.downloadbtn.visibility=View.INVISIBLE
                binding.cardView2.visibility=View.INVISIBLE
                binding.feedbackimgview.visibility=View.INVISIBLE
                binding.feedbacktextview.visibility=View.INVISIBLE
                binding.webview.visibility = WebView.VISIBLE
            } else {
                val getselectedoption = sharepref.getSelectedOption(requireContext())
                Log.d("SelectedOption", "loadSearchBox: "+getselectedoption)
                if (getselectedoption != null) {
                   if(getselectedoption.equals("Google"))
                   {
                       performGoogleSearch(targetUrl)
                   }
                    else if(getselectedoption.equals("Bing"))
                   {
                       performBingSearch(targetUrl)
                   }
                    else if(getselectedoption.equals("Yahoo"))
                   {
                        performYahooSearch(targetUrl)
                   }
                }

                binding.webview.visibility = WebView.VISIBLE
            }
        } else {
            Toast.makeText(requireContext(), "Enter something to proceed", Toast.LENGTH_LONG).show()
        }
    }

    private fun isURL(text: String): Boolean {
        return android.util.Patterns.WEB_URL.matcher(text).matches()
    }

    private fun performGoogleSearch(query: String) {
        val searchUrl = "https://www.google.com/search?q=${Uri.encode(query)}"
        binding.webview.loadUrl(searchUrl)

        Handler().postDelayed({
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
            startActivity(browserIntent)
        }, 5000)

        binding.downloadbtn.visibility=View.INVISIBLE
        binding.cardView2.visibility=View.INVISIBLE
        binding.feedbackimgview.visibility=View.INVISIBLE
        binding.feedbacktextview.visibility=View.INVISIBLE
        binding.webview.loadUrl(searchUrl)

        backArrowTabFrag()
        onBackpresfunc()
    }
    private fun performBingSearch(targetUrl: String) {
        val searchUrl = "https://www.bing.com/search?q=${Uri.encode(targetUrl)}"
        binding.downloadbtn.visibility=View.INVISIBLE
        binding.cardView2.visibility=View.INVISIBLE
        binding.feedbackimgview.visibility=View.INVISIBLE
        binding.feedbacktextview.visibility=View.INVISIBLE
        binding.webview.loadUrl(searchUrl)

        backArrowTabFrag()
        onBackpresfunc()
    }

    private fun performYahooSearch(targetUrl: String) {
        val searchUrl = "https://search.yahoo.com/search?p=${Uri.encode(targetUrl)}"
        binding.downloadbtn.visibility=View.INVISIBLE
        binding.cardView2.visibility=View.INVISIBLE
        binding.feedbackimgview.visibility=View.INVISIBLE
        binding.feedbacktextview.visibility=View.INVISIBLE
        binding.webview.loadUrl(searchUrl)

        backArrowTabFrag()
        onBackpresfunc()

    }



       fun LoadInsta(targetUrlinsta : String) {


        binding.webview.settings.setJavaScriptEnabled(true);
        binding.webview.settings.setPluginState(WebSettings.PluginState.ON);
        binding.webview.settings.builtInZoomControls = true;
        binding.webview.settings.displayZoomControls = true;
        binding.webview.settings.useWideViewPort = true;
        binding.webview.settings.loadWithOverviewMode = true;
        binding.webview.addJavascriptInterface(this, "InstDownloader");
        binding.webview.webViewClient=object : WebViewClient()
        {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val database by lazy {
                    Room.databaseBuilder(requireContext(), AppDataBase::class.java, "app_database")
                        .build()
                }

                val linkEntity = url?.let { LinkEntity(link = it) }
                val linkDao = database.linkDao()

                CoroutineScope(Dispatchers.IO).launch {
                    if (linkEntity != null) {
                        Log.d("link", "onPageFinished: "+linkEntity)
                        linkDao.insert(linkEntity)
                    }
                }
            }


            override fun onLoadResource(view: WebView, url: String) {

                binding.webview.settings.userAgentString = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Mobile Safari/537.36"


                val script = """
    {
        console.log('in insta js');
        var videoElement = document.querySelectorAll('video');
        for(i = 0; i<videoElement.length; i++){

        var src_video = videoElement[i].getAttribute('src');
        let download_btn_div = document.createElement("img");
        download_btn_div.style.zIndex = 4;
        console.log(src_video);

        let svgCode = `<svg version="1.1" xmlns="http://www.w3.org/2000/svg" width="42px" height="42px" viewBox="0 0 42 42"><circle fill="#F73400" cx="21" cy="21" r="21"/><g fill="#FFFFFF"><g transform="translate(310.205 104.469)"><path d="M-290.4-89.1c0-1.1,0-2.3,0-3.4c0-0.3,0.1-0.4,0.4-0.4c0.9,0,1.8,0,2.7,0c0.3,0,0.5,0.1,0.5,0.5c0,2.3,0,4.5,0,6.8c0,0.3,0.1,0.5,0.4,0.4c1.2,0,2.5,0,3.7,0c0.1,0,0.2,0,0.4,0.1c0,0.1-0.1,0.2-0.2,0.4c-1.9,1.8-3.9,3.7-5.8,5.5c-0.2,0.2-0.4,0.2-0.6,0c-1.9-1.8-3.9-3.7-5.8-5.5c-0.1-0.1-0.2-0.2-0.2-0.3c0.1-0.1,0.2-0.1,0.4-0.1c1.2,0,2.5,0,3.8,0c0.3,0,0.4-0.1,0.4-0.4C-290.4-86.9-290.4-88-290.4-89.1z"/><path d="M-288.6-75.9h-7c-0.3,0-0.5-0.1-0.5-0.4c0-0.3,0.2-0.4,0.5-0.4c0.9,0,1.8,0,2.7,0h11c0.1,0,0.2,0,0.3,0c0.1,0.1,0.3,0.2,0.3,0.4s-0.2,0.3-0.3,0.4c-0.1,0.1-0.2,0-0.3,0L-288.6-75.9z"/></g></g></svg>`;
        download_btn_div.src = 'data:image/svg+xml;charset=utf-8,' + encodeURIComponent(svgCode);
        download_btn_div.setAttribute("style", "position:absolute;width:50px;height:50px;bottom:0;left:0");
        if (videoElement[i].parentElement.getElementsByTagName('img').length == 0) {
            videoElement[i].parentElement.appendChild(download_btn_div);
        }

        download_btn_div.onclick = function() {
            InstDownloader.onInstagramVideoClick(src_video);
        };
        }
    }
"""
                binding.webview.evaluateJavascript(script, null)

            }

        }



        CookieSyncManager.createInstance(activity?.applicationContext)
        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        CookieSyncManager.getInstance().startSync()
           binding.feedbacktextview.visibility=View.INVISIBLE
           binding.cardView2.visibility=View.INVISIBLE
           binding.feedbackimgview.visibility=View.INVISIBLE
           binding.downloadbtn.visibility=View.INVISIBLE
           binding.webview.visibility=View.VISIBLE



        binding.webview.loadUrl(targetUrlinsta)

           backArrowTabFrag()

           onBackpresfunc()

        binding.backbtntabfragmnt.setOnClickListener {

            val intnt=Intent(requireContext(),MainActivity::class.java)
            startActivity(intnt)
        }



    }


    private fun LoadFb(targetUrl : String)
    {


        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webview.getSettings().setBuiltInZoomControls(true);
        binding.webview.getSettings().setDisplayZoomControls(true);
        binding.webview.getSettings().setUseWideViewPort(true);
        binding.webview.getSettings().setLoadWithOverviewMode(true);
        binding.webview.addJavascriptInterface(this, "FBDownloader");
        binding.webview.webViewClient=object : WebViewClient()
        {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)


                val database by lazy {
                    Room.databaseBuilder(requireContext(), AppDataBase::class.java, "app_database")
                        .build()
                }

                val linkEntity = url?.let { LinkEntity(link = it) }
                val linkDao = database.linkDao()

                CoroutineScope(Dispatchers.IO).launch {
                    if (linkEntity != null) {
                        Log.d("link", "onPageFinished: "+linkEntity)
                        linkDao.insert(linkEntity)
                    }
                }

            }


            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)

                binding.webview.loadUrl(
                    "javascript:(function() { " +
                            "var videos = document.querySelectorAll('div[data-extra]');" +
                            "for(index = 0; index < videos.length; index++) {" +
                            "let data = videos[index].getAttribute(\"data-extra\");" +
                            "let json = JSON.parse(data);" +
                            "let dash = json.dash_manifest;" +
                            "videos[index].setAttribute(\"dash\", dash);" +
                            "var videoId = json['data-video_id'];" +
                            "if (!videos[index].querySelector('.download-button')) {" +
                            "var downloadButton = document.createElement('button');" +
                            "downloadButton.className = 'download-button';" +
                            "downloadButton.innerHTML = '<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" width=\"42px\" height=\"42px\" viewBox=\"0 0 42 42\"><circle fill=\"#F73400\" cx=\"21\" cy=\"21\" r=\"21\"/><g fill=\"#FFFFFF\"><g transform=\"translate(310.205 104.469)\"><path d=\"M-290.4-89.1c0-1.1,0-2.3,0-3.4c0-0.3,0.1-0.4,0.4-0.4c0.9,0,1.8,0,2.7,0c0.3,0,0.5,0.1,0.5,0.5c0,2.3,0,4.5,0,6.8c0,0.3,0.1,0.5,0.4,0.4c1.2,0,2.5,0,3.7,0c0.1,0,0.2,0,0.4,0.1c0,0.1-0.1,0.2-0.2,0.4c-1.9,1.8-3.9,3.7-5.8,5.5c-0.2,0.2-0.4,0.2-0.6,0c-1.9-1.8-3.9-3.7-5.8-5.5c-0.1-0.1-0.2-0.2-0.2-0.3c0.1-0.1,0.2-0.1,0.4-0.1c1.2,0,2.5,0,3.8,0c0.3,0,0.4-0.1,0.4-0.4C-290.4-86.9-290.4-88-290.4-89.1z\"/><path d=\"M-288.6-75.9h-7c-0.3,0-0.5-0.1-0.5-0.4c0-0.3,0.2-0.4,0.5-0.4c0.9,0,1.8,0,2.7,0h11c0.1,0,0.2,0,0.3,0c0.1,0.1,0.3,0.2,0.3,0.4s-0.2,0.3-0.3,0.4c-0.1,0.1-0.2,0-0.3,0L-288.6-75.9z\"/></g></g></svg>';" +
                            "downloadButton.style.position = 'absolute';" +
                            "downloadButton.style.bottom = '0';" +
                            "downloadButton.style.left = '0';" +
                            "videos[index].appendChild(downloadButton);" +
                            "downloadButton.addEventListener('click', function() {" +
                            "let mainUrl = this.parentElement.getAttribute(\"dash\");" +
                            "FBDownloader.processVideo(mainUrl);" +
                            "});" +
                            "}" +
                            "}" +
                            "})()")


            }

        }

        CookieSyncManager.createInstance(activity?.applicationContext)
        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        CookieSyncManager.getInstance().startSync()


        binding.downloadbtn.visibility=View.INVISIBLE
        binding.feedbacktextview.visibility=View.INVISIBLE
        binding.cardView2.visibility=View.INVISIBLE
        binding.webview.visibility=View.VISIBLE
        binding.feedbackimgview.visibility=View.INVISIBLE
        binding.webview.loadUrl(targetUrl)

        backArrowTabFrag()

        onBackpresfunc()

        binding.backbtntabfragmnt.setOnClickListener {

            val intnt=Intent(requireContext(),MainActivity::class.java)
            startActivity(intnt)
        }


    }


    @JavascriptInterface
    fun onInstagramVideoClick(instUrl : String)
    {
        Log.d("install", instUrl)
        Log.d("install","Clicked")
        Downloadbottmsheet(instUrl).show(requireFragmentManager(),"show")

    }



    @JavascriptInterface
    fun processVideo(vidID : String) {


        val inputstream : InputStream = vidID.byteInputStream();
        val listOfResulitions = XmlParser().parse(inputstream)
        Log.d("EntryModel", "processVideo: "+listOfResulitions)
        Downloadbottmsheet( listOfResulitions as ArrayList<Entry>).show(requireFragmentManager(),"show")

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.optionmenu_tab, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.share_optionmenu -> {
                val intent= Intent()
                intent.action=Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT,"Hey Check out this Great app:")
                intent.type="text/plain"
                startActivity(Intent.createChooser(intent,"Share To:"))
                return true
            }
            R.id.history -> {
                val intent = Intent(requireContext(), HistoryClass::class.java)
                startActivity(intent)
                return true
            }

            R.id.bookmarks -> {
                val intentb=Intent(requireContext(),BookmarksActivity::class.java)
                startActivity(intentb)
                return true
            }

            R.id.addbookmark -> {

                val currentUrl: String? = binding.webview.url


                val database by lazy {
                    Room.databaseBuilder(requireContext(), AppDataBase::class.java, "app_database")
                        .build()
                }

                val linkEntityBookmark = currentUrl?.let { BookmarkEntity(link = it) }
                val linkDaoBookmark = database.bookmarkDao()

                CoroutineScope(Dispatchers.IO).launch {
                    if (linkEntityBookmark != null) {
                        Log.d("linkBookmark", "onPageFinished: "+linkEntityBookmark)
                        linkDaoBookmark.insert(linkEntityBookmark)
                    }
                }

                return true
            }

            R.id.settings_optionmenu -> {
                val intentsetting=Intent(requireContext(),Setting::class.java)
                startActivity(intentsetting)

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
                val intnt=Intent(requireContext(),ExitActivity::class.java)
                startActivity(intnt)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }



    private fun backArrowTabFrag()
    {
        binding.backbtntabfragmnt.setOnClickListener {
            val intentbackfrag=Intent(requireContext(),MainActivity::class.java)
            startActivity(intentbackfrag)
        }
    }

    fun onBackpresfunc()
    {
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                val intnt=Intent(requireContext(),MainActivity::class.java)
                startActivity(intnt)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )
    }

     fun mainActivityBackPress()
    {
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                val intnt=Intent(requireContext(),ExitActivity::class.java)
                startActivity(intnt)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )
    }

}