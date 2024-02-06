package com.example.adminblinkitclone.models

import java.util.UUID

data class Product(
    var productRandomId:String? = null,
    var productTitle:String? = null,
    var productQuantity: Int? = null,
    var productType:String? = null,
    var productUnit:String? = null,
    var productStock: Int? = null,
    var productPrice: Int? = null,
    var productCategory:String? =null,
    var itemCount:Int ? = null,
    var adminUid:String? =null,
    var productImagesUris: ArrayList<String>? = null
)
