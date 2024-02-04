package com.example.adminblinkitclone.fragments

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.adminblinkitclone.Constants
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.Utils
import com.example.adminblinkitclone.adapters.AdapterSelectedImage
import com.example.adminblinkitclone.databinding.FragmentAddProductBinding
import com.example.adminblinkitclone.models.Product
import com.example.adminblinkitclone.viewmodel.AdminViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddProductFragment : Fragment() {

    private lateinit var binding:FragmentAddProductBinding
    private val imageUris: ArrayList<Uri> = arrayListOf()
    private val viewModel:AdminViewModel by viewModels()

    val selectedImage =  registerForActivityResult(ActivityResultContracts.GetMultipleContents()){ listOfUri->
        val fiveImages= listOfUri.take(5)
        imageUris.clear()
        imageUris.addAll(fiveImages)

        binding.rvProductImages.adapter = AdapterSelectedImage(imageUris)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAddProductBinding.inflate(layoutInflater)
        setStatusBarColor()
        setAutoCompleteTextViews()
        onImageSelectClicked()
        onAddButtonClicked()
        return binding.root
    }

    private fun onAddButtonClicked() {
        binding.btnAddProduct.setOnClickListener {
            Utils.showDialog(requireContext(),"Uploading Images")
            val productTitle = binding.etProductTitle.text.toString()
            val productQuantity = binding.etProductQuantity.text.toString()
            val productUnit = binding.etProductUnit.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productStock = binding.etProductStock.text.toString()
            val productCategory = binding.etProductCategory.text.toString()
            val productType = binding.etProductType.text.toString()

            if (productTitle.isEmpty()|| productCategory.isEmpty() || productPrice.isEmpty()|| productQuantity.isEmpty()
                ||productStock.isEmpty()||productUnit.isEmpty()||productType.isEmpty()){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(),"Empty fields are not allowed")
                }
            }
            else if(imageUris.isEmpty()){
                Utils.apply {
                    hideDialog()
                    showToast(requireContext(),"Please Upload some images")
                }
            }
            else {
                val product= Product(
                    productTitle = productTitle,
                    productQuantity = productQuantity.toInt(),
                    productCategory =  productCategory,
                    productUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getCurrentUserId()
                )

                saveImage(product)
            }
        }
    }

    private fun saveImage(product: Product) {
         viewModel.saveImagesInDB(imageUris)
        lifecycleScope.launch {
            viewModel.isImagesUploaded.collect{
                if (it){
                    Utils.apply {
                        hideDialog()
                        showToast(requireContext(),"image saved")
                    }
                    getUrls(product)
                }
            }
        }
    }

    private fun getUrls(product: Product) {
        lifecycleScope.launch {
            viewModel.downloadedUrls.collect{
             val urls = it
                product.productImagesUris = urls
                 saveProduct(product)
            }
        }
    }

    private fun saveProduct(product: Product) {

    }

    private fun onImageSelectClicked(){
        binding.btnSelectImage.setOnClickListener {
       selectedImage.launch("image/*")
       }
    }

    private fun setAutoCompleteTextViews(){
       val units= ArrayAdapter(requireContext(), R.layout.show_list,Constants.allUnitsOfProducts)
       val category= ArrayAdapter(requireContext(), R.layout.show_list,Constants.allProductsCategory)
       val productType= ArrayAdapter(requireContext(), R.layout.show_list,Constants.allProductType)

        binding.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(productType)
            etProductType.setAdapter(productType)
        }
    }

    private fun setStatusBarColor(){
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.yellow)
            statusBarColor=statusBarColors
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}