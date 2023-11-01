package com.example.myapplication.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.myapplication.utils.AppDataBase
import com.example.myapplication.Model.Entry
import com.example.myapplication.Model.LinkEntity
import com.example.myapplication.Model.Vimeodatamodel
import com.example.myapplication.R
import com.example.myapplication.XmlParser
import com.example.myapplication.databinding.FragmentWebviewBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class Webview : Fragment(R.layout.fragment_webview) {

    lateinit var binding:FragmentWebviewBinding


    var client = OkHttpClient()


    private var urls=ArrayList<Vimeodatamodel>()

    private var urls1=ArrayList<Entry>()

    private var progressiveArray=JSONArray()

    private var args : Bundle?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWebviewBinding.bind(view)

        args=arguments

        Webviews()



    }



    private fun Webviews()
    {



        if(args != null)
        {
            val value1= args!!.getString("Facebook")
            val value2= args!!.getString("Instagram")
            val value3= args!!.getString("Vimeo")
            val value4= args!!.getString("Dailymotion")
            val value5= args!!.getString("Twitter")
            val value6= args!!.getString("Wallpaper")
            val value7= args!!.getString("Whatsapp")




            if(value1 != null)
            {
                if(value1 == "fb")
                {


                    val target_url="https://www.facebook.com/login/"

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

                    binding.webview.loadUrl(target_url)


                }
            }

            if(value2 != null)
            {
                if(value2 == "inst") {


                    val target_url_insta="https://www.instagram.com/accounts/login/"


                    binding.webview.settings.setJavaScriptEnabled(true);
                    binding.webview.settings.setPluginState(WebSettings.PluginState.ON);
                    binding.webview.settings.builtInZoomControls = true;
                    binding.webview.settings.displayZoomControls = true;
                    binding.webview.settings.useWideViewPort = true;
                    binding.webview.settings.loadWithOverviewMode = true;
//                    binding.webview.settings.userAgentString = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Mobile Safari/537.36"
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
         var videoElement = document.querySelectorAll('video');
    for (var i = 0; i < videoElement.length; i++) {
        (function(i) {
            var src_video = videoElement[i].getAttribute('src');
            var download_btn_div = document.createElement("img");
            download_btn_div.style.zIndex = 4;
            console.log(videoElement[i] + ': ' + src_video);

            var svgCode = '<svg version="1.1" xmlns="http://www.w3.org/2000/svg" width="42px" height="42px" viewBox="0 0 42 42"><circle fill="#F73400" cx="21" cy="21" r="21"/><g fill="#FFFFFF"><g transform="translate(310.205 104.469)"><path d="M-290.4-89.1c0-1.1,0-2.3,0-3.4c0-0.3,0.1-0.4,0.4-0.4c0.9,0,1.8,0,2.7,0c0.3,0,0.5,0.1,0.5,0.5c0,2.3,0,4.5,0,6.8c0,0.3,0.1,0.5,0.4,0.4c1.2,0,2.5,0,3.7,0c0.1,0,0.2,0,0.4,0.1c0,0.1-0.1,0.2-0.2,0.4c-1.9,1.8-3.9,3.7-5.8,5.5c-0.2,0.2-0.4,0.2-0.6,0c-1.9-1.8-3.9-3.7-5.8-5.5c-0.1-0.1-0.2-0.2-0.2-0.3c0.1-0.1,0.2-0.1,0.4-0.1c1.2,0,2.5,0,3.8,0c0.3,0,0.4-0.1,0.4-0.4C-290.4-86.9-290.4-88-290.4-89.1z"/><path d="M-288.6-75.9h-7c-0.3,0-0.5-0.1-0.5-0.4c0-0.3,0.2-0.4,0.5-0.4c0.9,0,1.8,0,2.7,0h11c0.1,0,0.2,0,0.3,0c0.1,0.1,0.3,0.2,0.3,0.4s-0.2,0.3-0.3,0.4c-0.1,0.1-0.2,0-0.3,0L-288.6-75.9z"/></g></g></svg>';
            download_btn_div.src = 'data:image/svg+xml;charset=utf-8,' + encodeURIComponent(svgCode);
            download_btn_div.setAttribute("style", "position:absolute;width:50px;height:50px;bottom:0;left:0");
            if (videoElement[i].parentElement.getElementsByTagName('img').length == 0) {
                videoElement[i].parentElement.appendChild(download_btn_div);
            }

            download_btn_div.onclick = function() {
                console.log(videoElement[i] + ': ' + src_video);
                InstDownloader.onInstagramVideoClick(src_video);
            };
        })(i);
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

                    binding.webview.loadUrl(target_url_insta)





                }

            }
            if(value3 != null)
            {
                if(value3 == "vimeo")
                {

                        val target_url_insta="https://vimeo.com/log_in"


                        binding.webview.getSettings().setJavaScriptEnabled(true);
                        binding.webview.getSettings().setPluginState(WebSettings.PluginState.ON);
                        binding.webview.getSettings().setBuiltInZoomControls(true);
                        binding.webview.getSettings().setDisplayZoomControls(true);
                        binding.webview.getSettings().setUseWideViewPort(true);
                        binding.webview.getSettings().setLoadWithOverviewMode(true);
                        binding.webview.addJavascriptInterface(this, "VIMEODownloader");
                        binding.webview.webViewClient=object : WebViewClient()
                        {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)



                            }


                            override fun onLoadResource(view: WebView?, url: String?) {
                                super.onLoadResource(view, url)

                                binding.webview.loadUrl(
                                    "javascript:(function() { " +
                                            "var playerElement = document.querySelectorAll('div[data-config-url]');" +
                                            "for(index = 0; index < playerElement.length; index++) {" +
                                            "let data = playerElement[index].getAttribute(\"data-config-url\");" +
                                            "if (!playerElement[index].querySelector('.download-button')) {" +
                                            "var downloadButton = document.createElement('button');" +
                                            "downloadButton.className = 'download-button';" +
                                            "downloadButton.innerHTML = '<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" width=\"42px\" height=\"42px\" viewBox=\"0 0 42 42\"><circle fill=\"#F73400\" cx=\"21\" cy=\"21\" r=\"21\"/><g fill=\"#FFFFFF\"><g transform=\"translate(310.205 104.469)\"><path d=\"M-290.4-89.1c0-1.1,0-2.3,0-3.4c0-0.3,0.1-0.4,0.4-0.4c0.9,0,1.8,0,2.7,0c0.3,0,0.5,0.1,0.5,0.5c0,2.3,0,4.5,0,6.8c0,0.3,0.1,0.5,0.4,0.4c1.2,0,2.5,0,3.7,0c0.1,0,0.2,0,0.4,0.1c0,0.1-0.1,0.2-0.2,0.4c-1.9,1.8-3.9,3.7-5.8,5.5c-0.2,0.2-0.4,0.2-0.6,0c-1.9-1.8-3.9-3.7-5.8-5.5c-0.1-0.1-0.2-0.2-0.2-0.3c0.1-0.1,0.2-0.1,0.4-0.1c1.2,0,2.5,0,3.8,0c0.3,0,0.4-0.1,0.4-0.4C-290.4-86.9-290.4-88-290.4-89.1z\"/><path d=\"M-288.6-75.9h-7c-0.3,0-0.5-0.1-0.5-0.4c0-0.3,0.2-0.4,0.5-0.4c0.9,0,1.8,0,2.7,0h11c0.1,0,0.2,0,0.3,0c0.1,0.1,0.3,0.2,0.3,0.4s-0.2,0.3-0.3,0.4c-0.1,0.1-0.2,0-0.3,0L-288.6-75.9z\"/></g></g></svg>';" +
                                            "downloadButton.style.position = 'absolute';" +
                                            "downloadButton.style.top = '0';" +
                                            "downloadButton.style.left = '0';" +
                                            "playerElement[index].appendChild(downloadButton);" +
                                            "playerElement[index].setAttribute('onClick', 'VIMEODownloader.getUrlsforVimeo(\"' + data + '\");');" +
//                                            "downloadButton.addEventListener('click', function() {" +
//                                            "VIMEODownloader.getUrlsforVimeo(data);" +
//                                            "});" +
                                            "}" +
                                            "}" +
                                            "})()")




                            }

                        }

                        CookieSyncManager.createInstance(activity?.applicationContext)
                        val cookieManager: CookieManager = CookieManager.getInstance()
                        cookieManager.setAcceptCookie(true)
                        CookieSyncManager.getInstance().startSync()

                        binding.webview.loadUrl(target_url_insta)




                }
            }
            if(value4 != null)
            {
                if(value4 == "dailym")
                {
                    binding.webview.clearCache(true)
                    binding.webview.loadUrl("javascript:window.location.reload(true)")
                    binding.webview.loadUrl("https://www.dailymotion.com/signin?urlback=%2Ff100004724199536")
                }
            }
            if(value5 != null)
            {
                if(value5 == "twitter")
                {
                    binding.webview.clearCache(true)
                    binding.webview.loadUrl("javascript:window.location.reload(true)")
                    binding.webview.loadUrl("https://twitter.com/i/flow/login?lang=en")
                }
            }
            if(value6 != null)
            {
                if(value6 == "wallpaper")
                {
                    binding.webview.clearCache(true)
                    binding.webview.loadUrl("javascript:window.location.reload(true)")
                    binding.webview.loadUrl("https://www.freepik.com/free-photos-vectors/login-background")
                }
            }

            if(value7 != null)
            {
                if(value7 == "whatsapp")
                {
                    binding.webview.clearCache(true)
                    Toast.makeText(activity?.applicationContext,"Whatsapp is pending",Toast.LENGTH_SHORT).show()
                    binding.webview.loadUrl("javascript:window.location.reload(true)")
                    binding.webview.loadUrl("https://web.whatsapp.com/")

                }
            }


        }
    }



    @JavascriptInterface
    fun processVideo(vidID : String) {


        val inputstream : InputStream = vidID.byteInputStream();
        val listOfResulitions =XmlParser().parse(inputstream)
        Log.d("EntryModel", "processVideo: "+listOfResulitions)
        Downloadbottmsheet( listOfResulitions as ArrayList<Entry>).show(requireFragmentManager(),"show")




    }

    @JavascriptInterface
    fun onInstagramVideoClick(instUrl : String)
    {
        Log.d("install", instUrl)
        Log.d("install","Clicked")
        Downloadbottmsheet(instUrl).show(requireFragmentManager(),"show")

    }



    @JavascriptInterface
    fun getUrlsforVimeo(url_video: String) {


        Log.d("vimeolink", url_video)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client = OkHttpClient()
                val request = Request.Builder()
                    .url(url_video)
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val json = JSONObject(responseBody)


                    progressiveArray = json.getJSONObject("request")
                        .getJSONObject("files")
                        .getJSONArray("progressive")

                    for (i in 0 until progressiveArray.length()) {
                        val item = progressiveArray.getJSONObject(i)
                        val videoUrl = item.getString("url")
                        val videoquality=item.getString("quality")
                        urls1.add(Entry(videoUrl,videoquality))
                    }
                    Log.d("vimeourls", urls.toString())

                } else {
                    Log.e("vimeourls", "HTTP request failed with code: ${response.code}")
                }
            } catch (e: Exception) {
                Log.d("Exceptionvideolins", "getUrlsforVimeo expet: "+e.toString())
                e.printStackTrace()
            }

        }



        Downloadbottmsheet(urls1).show(requireFragmentManager(),"show")


    }




}
