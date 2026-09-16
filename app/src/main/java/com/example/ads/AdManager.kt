package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * AdMob Configuration & Test Ad Unit IDs
 * Production mein aap apni asli AdMob Ad Unit IDs yahan tabdeel kar sakte hain.
 */
object AdConfig {
    // Banner Test Ad Unit ID
    const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

    // Interstitial Test Ad Unit ID
    const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    // Rewarded Video Test Ad Unit ID
    const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    // Har kitne screen changes ke baad Interstitial Ad dikhana hai (3-4 changes)
    const val INTERSTITIAL_CHANGE_THRESHOLD = 3
}

/**
 * Helper class for managing Google AdMob Ads (Banner, Interstitial, Rewarded)
 * Handles background loading, safe failure resilience, and user-friendly display intervals.
 */
object AdManager {
    private const val TAG = "AdManager"

    private var interstitialAd: InterstitialAd? = null
    private var isInterstitialLoading = false

    private var rewardedAd: RewardedAd? = null
    private var isRewardedLoading = false

    private var screenChangeCounter = 0

    /**
     * MobileAds SDK ko initialize karta hai aur ads ko background mein preload karta hai.
     */
    fun initialize(context: Context) {
        try {
            MobileAds.initialize(context) { status ->
                Log.d(TAG, "AdMob SDK Initialized successfully")
                loadInterstitialAd(context.applicationContext)
                loadRewardedAd(context.applicationContext)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize AdMob SDK", e)
        }
    }

    /**
     * Interstitial Ad ko background mein silently load karta hai.
     */
    fun loadInterstitialAd(context: Context) {
        if (interstitialAd != null || isInterstitialLoading) return
        isInterstitialLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            AdConfig.INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isInterstitialLoading = false
                    Log.d(TAG, "Interstitial Ad load ho gaya")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isInterstitialLoading = false
                    Log.w(TAG, "Interstitial Ad load nahi ho saka: ${error.message}")
                }
            }
        )
    }

    /**
     * Screen tab change hone par call hota hai. Har 3-4 screen changes par hi ad show karta hai.
     */
    fun onScreenChanged(activity: Activity) {
        screenChangeCounter++
        Log.d(TAG, "Screen change counter: $screenChangeCounter")
        if (screenChangeCounter >= AdConfig.INTERSTITIAL_CHANGE_THRESHOLD) {
            screenChangeCounter = 0
            showInterstitialAd(activity)
        }
    }

    /**
     * Interstitial Ad dikhata hai agar ready ho, aur dismiss hone par agla ad preload kar leta hai.
     */
    fun showInterstitialAd(activity: Activity, onDismissed: (() -> Unit)? = null) {
        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitialAd(activity.applicationContext)
                    onDismissed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    Log.w(TAG, "Interstitial show fail: ${error.message}")
                    interstitialAd = null
                    loadInterstitialAd(activity.applicationContext)
                    onDismissed?.invoke()
                }
            }
            ad.show(activity)
        } else {
            // Agar ad abhi ready nahi hai, to silently background mein load karein
            loadInterstitialAd(activity.applicationContext)
            onDismissed?.invoke()
        }
    }

    /**
     * Rewarded Video Ad ko preload karta hai.
     */
    fun loadRewardedAd(context: Context) {
        if (rewardedAd != null || isRewardedLoading) return
        isRewardedLoading = true

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            AdConfig.REWARDED_AD_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    isRewardedLoading = false
                    Log.d(TAG, "Rewarded Ad load ho gaya")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    isRewardedLoading = false
                    Log.w(TAG, "Rewarded Ad load nahi ho saka: ${error.message}")
                }
            }
        )
    }

    /**
     * Checks if Rewarded Ad is ready to show.
     */
    fun isRewardedAdReady(): Boolean = rewardedAd != null

    /**
     * Rewarded Video Ad show karta hai aur user ko reward deta hai.
     */
    fun showRewardedAd(
        activity: Activity,
        onRewardEarned: (RewardItem) -> Unit,
        onAdNotReady: () -> Unit,
        onDismissed: (() -> Unit)? = null
    ) {
        val ad = rewardedAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    loadRewardedAd(activity.applicationContext)
                    onDismissed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    Log.w(TAG, "Rewarded ad show fail: ${error.message}")
                    rewardedAd = null
                    loadRewardedAd(activity.applicationContext)
                    onDismissed?.invoke()
                }
            }
            ad.show(activity) { rewardItem ->
                onRewardEarned(rewardItem)
            }
        } else {
            loadRewardedAd(activity.applicationContext)
            onAdNotReady()
        }
    }
}

/**
 * Jetpack Compose Banner Ad View
 * Screen ke bottom par 320x50 size ka Banner Ad render karta hai.
 */
@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adUnitId: String = AdConfig.BANNER_AD_UNIT_ID
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    this.adUnitId = adUnitId
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
