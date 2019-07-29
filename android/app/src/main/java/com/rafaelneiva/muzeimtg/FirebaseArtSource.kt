package com.rafaelneiva.muzeimtg

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.net.toUri
import com.google.android.apps.muzei.api.Artwork
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class FirebaseArtSource : RemoteMuzeiArtSource(SOURCE_NAME) {

    companion object {
        private const val TAG = "FirebaseMTGArt"
        private const val SOURCE_NAME = "FirebaseArtSource"

        private const val COMMAND_ID_VIEW_PROFILE = 1
        private const val COMMAND_ID_VISIT_MTG = 2
    }

    @SuppressLint("CheckResult")
    @Throws(RemoteMuzeiArtSource.RetryException::class)
    override fun onTryUpdate(@UpdateReason reason: Int) {
        val currentToken = currentArtwork?.token

        FirebaseService().getWallpaperList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { wallpapers ->

                val random = Random()
                var wallpaper: FirebaseService.Wallpaper
                var token: String
                while (true) {
                    wallpaper = wallpapers[random.nextInt(wallpapers.size)]
                    token = "${wallpaper.name}:${wallpaper.downloadUrl}"
                    if (wallpapers.size <= 1 || token != currentToken) {
                        break
                    }
                }

                publishArtwork(
                    Artwork.Builder()
                        .title(wallpaper.name)
                        .byline(wallpaper.name)
                        .attribution(wallpaper.name)
                        .imageUri(wallpaper.downloadUrl.toUri())
                        .token(token)
                        .viewIntent(Intent(Intent.ACTION_VIEW, "https://magic.wizards.com/en/articles/media/wallpapers".toUri()))
                        .build()
                )
            }
    }

    override fun onCustomCommand(id: Int) {
        when (id) {
            COMMAND_ID_VIEW_PROFILE -> {
                val profileUri = "https://magic.wizards.com/en/articles/media/wallpapers".toUri()
                startActivity(Intent(Intent.ACTION_VIEW, profileUri))
            }
            COMMAND_ID_VISIT_MTG -> {
            }
        }
    }
}

