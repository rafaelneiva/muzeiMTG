package com.rafaelneiva.muzeimtg

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.*
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.ProviderContract
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.rafaelneiva.muzeimtg.BuildConfig.UNSPLASH_AUTHORITY

class FirebaseWorker(
    private val context: Context,
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

    override fun doWork(): Result {

        val wallpapers = FirebaseService().getWallpaperList().value

        val providerClient = ProviderContract.getProviderClient(applicationContext, UNSPLASH_AUTHORITY)
        val attributionString = applicationContext.getString(R.string.attribution)
        providerClient.setArtwork(wallpapers!!.map { wallpaper ->
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

        return Result.success()
    }

}