package com.example.adminblinkitclone.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.adminblinkitclone.Utils
import com.example.adminblinkitclone.models.Product
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class AdminViewModel:ViewModel() {

    private val _isImagesUploaded = MutableStateFlow(false)
    var isImagesUploaded:StateFlow<Boolean> = _isImagesUploaded

    private val _downloadedUrls = MutableStateFlow<ArrayList<String?>>(arrayListOf())
    var downloadedUrls:StateFlow<ArrayList<String?>> = _downloadedUrls

    fun saveImagesInDB(imageUri :ArrayList<Uri>){
        val downloadUrls = ArrayList<String?>()

        imageUri.forEach{uri ->
           val imageRef= FirebaseStorage.getInstance().reference.child(Utils.getCurrentUserId()).child("images").child(UUID.randomUUID().toString())
            imageRef.putFile(uri).continueWithTask {
                imageRef.downloadUrl
            }.addOnCompleteListener {task->
                val url=task.result
                downloadUrls.add(url.toString())

                if (downloadUrls.size==imageUri.size){
                    _isImagesUploaded.value=true
                    _downloadedUrls.value= downloadUrls
                }
            }
        }
        fun saveProduct(product: Product){
            
        }
    }
}