package com.rafaelneiva.muzeimtg

import android.content.Intent
import androidx.core.net.toUri
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource

class FirebaseArtSource : RemoteMuzeiArtSource(SOURCE_NAME) {

    companion object {
        private const val TAG = "FirebaseMTGArt"
        private const val SOURCE_NAME = "FirebaseArtSource"

        private const val COMMAND_ID_VIEW_PROFILE = 1
        private const val COMMAND_ID_VISIT_MTG = 2
    }

    @Throws(RemoteMuzeiArtSource.RetryException::class)
    override fun onTryUpdate(@UpdateReason reason: Int) {
        val currentToken = currentArtwork?.token


    }

    override fun onCustomCommand(id: Int) {
        when (id) {
            COMMAND_ID_VIEW_PROFILE -> {
                currentArtwork?.apply {
                    val profileUri = token?.substringAfter(':')?.toUri()
                            ?: return
                    startActivity(Intent(Intent.ACTION_VIEW, profileUri))
                }
            }
            COMMAND_ID_VISIT_MTG -> {
            }
        }
    }
}

