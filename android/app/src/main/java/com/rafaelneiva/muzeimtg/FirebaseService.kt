package com.rafaelneiva.muzeimtg

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage

class FirebaseService {

    fun getWallpaperList(): LiveData<List<Wallpaper>> {
        val storage = FirebaseStorage.getInstance()
        val functions = FirebaseFunctions.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://muzei-mtg.appspot.com/")

        val ret = MutableLiveData<List<Wallpaper>>()
        var wallpaperCount = 0

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
                            ret.value = urls
                    }
                }
            }

        return ret
    }

    data class Wallpaper(
        val name: String,
        val downloadUrl: String
    )
}