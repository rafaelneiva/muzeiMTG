package com.rafaelneiva.muzeimtg

import com.google.android.apps.muzei.api.provider.MuzeiArtProvider

class FirebaseArtProvider : MuzeiArtProvider() {
    override fun onLoadRequested(initial: Boolean) {
        FirebaseWorker.enqueueLoad()
    }
}