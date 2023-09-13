package com.example.myapplication.Fragments

import android.graphics.Bitmap
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
import com.example.myapplication.Model.Entry
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
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject


class Webview : Fragment(R.layout.fragment_webview) {

    lateinit var binding:FragmentWebviewBinding


    var client = OkHttpClient()


    var url_vimeo = String()

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


                    binding.webview.getSettings().setJavaScriptEnabled(true);
                    binding.webview.getSettings().setPluginState(WebSettings.PluginState.ON);
                    binding.webview.getSettings().setBuiltInZoomControls(true);
                    binding.webview.getSettings().setDisplayZoomControls(true);
                    binding.webview.getSettings().setUseWideViewPort(true);
                    binding.webview.getSettings().setLoadWithOverviewMode(true);
                    binding.webview.settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"
                    binding.webview.addJavascriptInterface(this, "INSTADownloader");
                    binding.webview.webViewClient=object : WebViewClient()
                    {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)



                            injectJSInsta()



                        }


                        override fun onLoadResource(view: WebView?, url: String?) {
                            super.onLoadResource(view, url)

                            injectJSInsta()



                        }

                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)

                            injectJSInsta()

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
    fun instaprocessvideo(insta_url : String)
    {
        Log.d("instalink", insta_url)

    }



    @JavascriptInterface
    fun getUrlsforVimeo(url_video: String) {

//
//        Log.d("vimeolink", url_video)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                client = OkHttpClient()
//                val request = Request.Builder()
//                    .url(url_video)
//                    .build()
//
//                val response = client.newCall(request).execute()
//                if (response.isSuccessful) {
//                    val responseBody = response.body?.string()
//                    val json = JSONObject(responseBody)
//
//
//                    progressiveArray = json.getJSONObject("request")
//                        .getJSONObject("files")
//                        .getJSONArray("progressive")
//
//                    for (i in 0 until progressiveArray.length()) {
//                        val item = progressiveArray.getJSONObject(i)
//                        val videoUrl = item.getString("url")
//                        val videoquality=item.getString("quality")
//                        urls1.add(Entry(videoUrl,videoquality))
//                    }
//                    Log.d("vimeourls", urls.toString())
//
//                } else {
//                    Log.e("vimeourls", "HTTP request failed with code: ${response.code}")
//                }
//            } catch (e: Exception) {
//                Log.d("Exceptionvideolins", "getUrlsforVimeo expet: "+e.toString())
//                e.printStackTrace()
//            }
//
//        }
//
//        Downloadbottmsheet(urls1).show(requireFragmentManager(),"show")

    }


    @JavascriptInterface
    fun processVideo(vidID : String) {


        val inputstream : InputStream = vidID.byteInputStream();
        val listOfResulitions =XmlParser().parse(inputstream)
        Log.d("EntryModel", "processVideo: "+listOfResulitions)
        Downloadbottmsheet( listOfResulitions as ArrayList<Entry>).show(requireFragmentManager(),"show")




    }

    private fun injectJSInsta() {
        val test =
            "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"40\" height=\"40\" viewBox=\"0 0 54 54\"><defs> <style>.cls-1 {  fill: red;} .cls-2 { fill: #fff; }</style></defs><g id=\"downloader\" transform=\"translate(-40 -427)\"><circle id=\"Ellipse_23\" data-name=\"Ellipse 23\" class=\"cls-1\" cx=\"27\" cy=\"27\" r=\"27\" transform=\"translate(40 427)\"/><g id=\"Group_68\" data-name=\"Group 68\" transform=\"translate(53.882 439.014)\"> <path id=\"Path_187\" data-name=\"Path 187\" class=\"cls-2\" d=\"M35.534-.077A1.088,1.088,0,0,1,36.2,1.065c-.023,3.54,0,7.1,0,10.665v.343h3.334a.905.905,0,0,1,.936.525.922.922,0,0,1-.228,1.051c-1.964,2.238-3.928,4.5-5.892,6.737a.9.9,0,0,1-1.484,0c-1.987-2.284-3.951-4.522-5.915-6.76a.915.915,0,0,1-.228-1.051.938.938,0,0,1,.936-.525h3.312V1.042A1.071,1.071,0,0,1,31.629-.1C32.931-.077,34.233-.077,35.534-.077Z\" transform=\"translate(-20.475)\"/> <path id=\"Path_188\" data-name=\"Path 188\" class=\"cls-2\" d=\"M26.113,90.74A1.751,1.751,0,0,1,25.153,92a2.751,2.751,0,0,1-.959.183H2a1.752,1.752,0,0,1-1.9-1.9v-6.76H3.571v5.161h19.07V83.5h3.494C26.113,85.921,26.113,88.342,26.113,90.74Z\" transform=\"translate(0 -64.507)\"/></g></g></svg>"

        val script = """
                        {           
         mJavaInterface.log('in insta js');
       var vid = document.querySelectorAll('video');
       for(i=0; i<vid.length;i++)
       {
       var src_video = vid[i].getAttribute('src');
        var download_btn_div = document.createElement("img");
        download_btn_div.style.zIndex = 4;
        var svgCode = '<svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200"><defs><style>.cls-1{fill:red;}.cls-2{fill:#fff;}</style></defs><title>download</title><circle class="cls-1" cx="100" cy="100" r="96"/><g id="download_button_" data-name="download button "><path id="Path_20" data-name="Path 20" class="cls-2" d="M106.92,50.81a3.78,3.78,0,0,1,2.34,4c-.06,12.58,0,25.14,0,37.74v1.24H121a3.16,3.16,0,0,1,2.5,5.52c-7,7.93-13.91,15.91-20.87,23.85a3.13,3.13,0,0,1-4.33.89,3.18,3.18,0,0,1-.9-.89L76.54,99.33a3.19,3.19,0,0,1-.8-3.69A3.24,3.24,0,0,1,79,93.82H90.8v-39a3.84,3.84,0,0,1,2.28-4Z"/><path id="Path_21" data-name="Path 21" class="cls-2" d="M53.89,118.46H66.17v18.37h67.59V118.46h12.35V144c-.21.59-.36,1.14-.58,1.68a6.65,6.65,0,0,1-4.57,3.5H59.1c-.58-.21-1.17-.39-1.69-.63A6.57,6.57,0,0,1,53.9,144Z"/></g></svg>';
        download_btn_div.src = 'data:image/svg+xml;charset=utf-8,' + encodeURIComponent(svgCode);
        download_btn_div.setAttribute("style", "position:absolute;width:50px;height:50px;bottom:0;left:0");
        if (vid[i].parentElement.getElementsByTagName('img').length == 0) {
            vid[i].parentElement.appendChild(download_btn_div);
         }
    }
       
        download_btn_div.onclick = function() {
            mJavaInterface.instaprocessvideo(src_video);
        };
        }
    }
"""

        binding.webview.loadUrl(
            ("javascript:" +
                    "window.onscroll=function()\n" + script)
        )



        binding.webview.loadUrl(
            ("javascript:" +
                    "window.onload=function()\n" + script)
        )


    }






}
