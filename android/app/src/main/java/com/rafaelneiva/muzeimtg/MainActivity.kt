package com.rafaelneiva.muzeimtg

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class MainActivity : AppCompatActivity() {
    private lateinit var functions: FirebaseFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val storage = FirebaseStorage.getInstance()
        functions = FirebaseFunctions.getInstance()

        val storageRef = storage.getReferenceFromUrl("gs://muzei-mtg.appspot.com/")

        val urls = ArrayList<String>()
        functions
            .getHttpsCallable("helloWorld")
            .call()
            .addOnCompleteListener { task ->

                for (map in (task.result?.data as ArrayList<*>)) {
                    val cardRef = storageRef.child((map as HashMap<*, *>)["name"] as String)

                    cardRef.downloadUrl.addOnCompleteListener { t ->
                        urls.add(t.result.toString())
                        Log.i("TEST", t.result.toString())
                    }
                }
            }
    }

}
