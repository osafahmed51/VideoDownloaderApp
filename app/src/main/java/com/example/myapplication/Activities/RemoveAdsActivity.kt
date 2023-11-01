package com.example.myapplication.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityRemoveAdsBinding


class RemoveAdsActivity : AppCompatActivity(),PurchasesUpdatedListener {

    lateinit var binding : ActivityRemoveAdsBinding

    private lateinit var billingClient: BillingClient

    private val skuForOneMonth = "sku_for_one-month"

    private val skuForOneYear = "sku_for_one_year"

    private val skuForLifetime = "sku_for_lifetime"

    var selectedSku : String ? = null

    val ConsumeproductIDs = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRemoveAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)




        initializeBilling()

        onClickListener()



    }

    private fun initializeBilling() {
        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()


        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                retryBillingServiceConnection();
            }

            override fun onBillingSetupFinished(p0: BillingResult) {

                if(p0.responseCode == BillingClient.BillingResponseCode.OK)
                {
                    Log.e("err", "onBillingSetupFinished: "+p0.responseCode.toString())
                }
                else
                {
                    retryBillingServiceConnection()
                }

            }

        })
    }

    private fun retryBillingServiceConnection() {
        var tries = 1
        var maxTries = 3
        var isConnectionEstablished = false
        do {
            try {
                billingClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingServiceDisconnected() {
                        retryBillingServiceConnection()
                    }

                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        tries++
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            isConnectionEstablished = true
                        } else if (tries === maxTries) {
                            handleBillingError(billingResult.responseCode)
                        }
                    }
                })
            } catch (e: Exception) {
                tries++
            }
        } while (tries <= maxTries && !isConnectionEstablished)

        if (isConnectionEstablished === false) {
            handleBillingError(-1)
        }
    }


    private fun onClickListener() {
        binding.oneMonth.setOnClickListener {
            selectedSku=skuForOneMonth

        }
        binding.oneYearbtn.setOnClickListener {
            selectedSku=skuForOneYear
        }
        binding.lifeTimebtn.setOnClickListener {
            selectedSku=skuForLifetime

        }

        binding.buynowNoadsbtn.setOnClickListener {
            selectedSku?.let { it1 -> initiatePurchase(it1) }
        }

        binding.cancelNoadbtn.setOnClickListener {
            val intent= Intent(this@RemoveAdsActivity,MainActivity::class.java)
            startActivity(intent)
        }


    }


    private fun initiatePurchase(sku: String) {

        ConsumeproductIDs.add(skuForOneMonth)
        ConsumeproductIDs.add(skuForOneYear)
        ConsumeproductIDs.add(skuForLifetime)

        Log.d("ConsumeProductIdsList", "onCreate: "+ConsumeproductIDs)

        val productList =
            listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(sku)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )

        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()

        billingClient.queryProductDetailsAsync(params) { billingResult,
                                                         productDetailsList ->

            launchBillingFlow(productDetailsList.get(0))
        }



    }


    private fun launchBillingFlow(productDetails : ProductDetails) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(productDetails.subscriptionOfferDetails.toString())
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(this@RemoveAdsActivity, billingFlowParams)
    }


    private val purchasesUpdatedListener = PurchasesUpdatedListener { responseCode, purchases ->

    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        if (p0.responseCode == BillingClient.BillingResponseCode.OK && p1 != null) {
            for (purchase in p1) {

                handlepurchase(purchase)

            }
        } else if (p0.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {

        } else {

        }
    }

     fun handlepurchase(purchase: Purchase) {
        if (!purchase.isAcknowledged()) {
            billingClient.acknowledgePurchase(
                AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build()
            ) { billingResult: BillingResult ->

         if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (pur in purchase.getProducts()) {
                    if (pur.equals(ConsumeproductIDs)) {
                        Log.d("TAG", "Purchase is successful")


                    }
                }
            } else {

            }
            }
        }

    }


    private fun handleBillingError(responseCode: Int) {
        var errorMessage = ""
        errorMessage = when (responseCode) {
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> "Billing service is currently unavailable. Please try again later."
            BillingClient.BillingResponseCode.DEVELOPER_ERROR -> "An error occurred while processing the request. Please try again later."
            BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED -> "This feature is not supported on your device."
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> "You already own this item."
            BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> "You do not own this item."
            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> "This item is not available for purchase."
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> "Billing service has been disconnected. Please try again later."
            BillingClient.BillingResponseCode.SERVICE_TIMEOUT -> "Billing service timed out. Please try again later."
            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> "Billing service is currently unavailable. Please try again later."
            BillingClient.BillingResponseCode.USER_CANCELED -> "The purchase has been canceled."
            else -> "An unknown error occurred."
        }
        Log.e("BillingError", errorMessage)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::billingClient.isInitialized) {
            billingClient.endConnection()
        }
    }


}