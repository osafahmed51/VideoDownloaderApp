import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.example.myapplication.Ads.AdaptiveAds
import com.example.myapplication.Ads.InterstitialAdUpdated
import com.example.myapplication.Ads.NativeAdsHelper
import com.example.myapplication.R
import com.example.myapplication.TinyDB
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

fun Context.isAlreadyPurchased(): Boolean {
    return TinyDB(applicationContext).getBoolean("KEY_PURCHASE")
}
@RequiresApi(Build.VERSION_CODES.M)
fun Context.showBanner(context: Context, frameLayout: FrameLayout, parent: FrameLayout) {
    if (!context.isAlreadyPurchased()) {
        if(isNetworkConnected()) {
            val adView = AdView(context)
            val adaptiveAds = AdaptiveAds(context)
            val adRequest = AdRequest.Builder().build()
            adView.adUnitId = context.resources.getString(R.string.banner_id)

            frameLayout.addView(adView)
            adView.setAdSize(adaptiveAds.adSize)
            adView.loadAd(adRequest)
        } else {
            parent.visibility = View.GONE
        }
    }}


@RequiresApi(Build.VERSION_CODES.M)
fun Context.isNetworkConnected(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities =
        connectivityManager.getNetworkCapabilities(network) ?: return false

    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
}


@RequiresApi(Build.VERSION_CODES.M)
fun Activity.showAdAndGo(afterAdWork: () -> Unit) {

    if (!isAlreadyPurchased()) {
        InterstitialAdUpdated.getInstance().showInterstitialAdNew(this){
            afterAdWork()
        }

    } else {
        afterAdWork()
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.loadNativeAd(adView: View,
                         shimmerViewContainer: ShimmerFrameLayout? = null,
                         frameLayout: FrameLayout,
                         layoutId: Int,
                         sdIs: String?) {

    if (isNetworkConnected()) {
        NativeAdsHelper(this).setNativeAd(shimmerViewContainer, frameLayout, layoutId, sdIs)
    } else {
        adView.Invisible()
    }
}
fun View.Invisible() {
    visibility = View.INVISIBLE
}
fun View.Gone() {
    visibility = View.GONE
}
