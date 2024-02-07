package com.example.adminblinkitclone.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.adminblinkitclone.Constants
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.adapters.AdapterProduct
import com.example.adminblinkitclone.adapters.CategoriesAdapter
import com.example.adminblinkitclone.databinding.FragmentHomeBinding
import com.example.adminblinkitclone.models.Categories
import com.example.adminblinkitclone.viewmodel.AdminViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: AdminViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        setStatusBarColor()
        setCategories()
        getAllTheProducts("All")
        return binding.root
    }

    private fun getAllTheProducts(category: String) {
        lifecycleScope.launch {
            viewModel.fetchAllProducts(category).collect{

                if (it.isEmpty()){
                    binding.rvProducts.visibility =View.GONE
                    binding.tvText.visibility =View.VISIBLE
                }
                else
                {
                    binding.rvProducts.visibility =View.VISIBLE
                    binding.tvText.visibility =View.GONE
                }
                val adapterProduct = AdapterProduct()
                binding.rvProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
            }
        }
    }

    private fun setCategories() {
        val categoryList = ArrayList<Categories>()
        val minSize = minOf(Constants.allProductsCategory.size, Constants.allProductsCategoryIcon.size)

        // Iterate over the minimum size
        for (i in 0 until minSize) {
            // Access elements from both arrays if within valid index range
            val category = Categories(Constants.allProductsCategory[i], Constants.allProductsCategoryIcon[i])
            categoryList.add(category)
        }
        binding.rvCategories.adapter=CategoriesAdapter(categoryList, ::onCategoryClicked)
    }

    private fun onCategoryClicked(categories: Categories){
       getAllTheProducts(categories.category)
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