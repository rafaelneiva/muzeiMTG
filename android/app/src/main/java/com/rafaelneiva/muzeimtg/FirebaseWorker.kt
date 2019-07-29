package com.rafaelneiva.muzeimtg

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.net.toUri
import androidx.work.*
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.ProviderContract
import com.rafaelneiva.muzeimtg.BuildConfig.UNSPLASH_AUTHORITY
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FirebaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        private const val TAG = "MuzeiFirebase"

        internal fun enqueueLoad() {
            val workManager = WorkManager.getInstance()
            workManager.enqueue(
                OneTimeWorkRequestBuilder<FirebaseWorker>()
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .build()
            )
        }
    }

    @SuppressLint("CheckResult")
    override fun doWork(): Result {

        FirebaseService().getWallpaperList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { wallpapers ->
                val providerClient = ProviderContract.getProviderClient(applicationContext, UNSPLASH_AUTHORITY)
                val attributionString = applicationContext.getString(R.string.attribution)
                providerClient.setArtwork(wallpapers.map { wallpaper ->
                    Artwork().apply {
                        token = wallpaper.name
                        title = wallpaper.name
                        byline = wallpaper.name
                        attribution = attributionString
                        persistentUri = wallpaper.downloadUrl.toUri()
                        webUri = wallpaper.downloadUrl.toUri()
                        metadata = wallpaper.downloadUrl
                    }
                })
            }

        return Result.success()
    }

}