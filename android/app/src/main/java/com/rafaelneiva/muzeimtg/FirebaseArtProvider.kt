package com.rafaelneiva.muzeimtg

import android.content.Intent
import androidx.core.net.toUri
import com.google.android.apps.muzei.api.UserCommand
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.MuzeiArtProvider

class FirebaseArtProvider : MuzeiArtProvider() {

    companion object {
        private const val COMMAND_ID_VIEW_PROFILE = 1
        private const val COMMAND_ID_VISIT_UNSPLASH = 2
    }

    override fun onLoadRequested(initial: Boolean) {
        FirebaseWorker.enqueueLoad()
    }

    override fun getCommands(artwork: Artwork) = context?.run {
        listOf(
            UserCommand(
                COMMAND_ID_VIEW_PROFILE,
                getString(R.string.action_visit_wotc)
            ),
            UserCommand(
                COMMAND_ID_VISIT_UNSPLASH,
                getString(R.string.action_visit_wotc)
            )
        )
    } ?: super.getCommands(artwork)

    override fun onCommand(artwork: Artwork, id: Int) {
        val context = context ?: return
        when (id) {
            COMMAND_ID_VIEW_PROFILE -> {
                context.startActivity(Intent(Intent.ACTION_VIEW, Constants.wizardsUrl.toUri()))
            }
            COMMAND_ID_VISIT_UNSPLASH -> {
                context.startActivity(Intent(Intent.ACTION_VIEW, Constants.wizardsUrl.toUri()))
            }
        }
    }
}