package com.rafaelneiva.muzeimtg

import android.util.Log
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single

class FirebaseService {

    fun getWallpaperList(): Single<List<Wallpaper>> {
        return Single.create { emitter ->
            try {

                val storage = FirebaseStorage.getInstance()
                val functions = FirebaseFunctions.getInstance()
                val storageRef = storage.getReferenceFromUrl("gs://muzei-mtg.appspot.com/")

                var wallpaperCount: Int

                val urls = ArrayList<Wallpaper>()
                functions
                    .getHttpsCallable("helloWorld")
                    .call()
                    .addOnCompleteListener { task ->
                        wallpaperCount = (task.result?.data as ArrayList<*>).size
                        for (map in (task.result?.data as ArrayList<*>)) {
                            val cardRef = storageRef.child((map as HashMap<*, *>)["name"] as String)
                            cardRef.downloadUrl.addOnCompleteListener { t ->
                                urls.add(Wallpaper(map["name"] as String, t.result.toString()))
                                Log.i("TEST", t.result.toString())

                                if (urls.size == wallpaperCount)
                                    emitter.onSuccess(urls)
                            }
                        }
                    }

            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    data class Wallpaper(
        val name: String,
        val downloadUrl: String
    )
}