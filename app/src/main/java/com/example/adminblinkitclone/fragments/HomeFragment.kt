package com.example.adminblinkitclone.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.adminblinkitclone.Constants
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.adapters.CategoriesAdapter
import com.example.adminblinkitclone.databinding.FragmentHomeBinding
import com.example.adminblinkitclone.models.Categories


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        setStatusBarColor()
        setCategories()
        return binding.root
    }

    private fun setCategories() {
        val categoryList = ArrayList<Categories>()
        for (i in 0 until Constants.allProductsCategoryIcon.size){
            categoryList.add(Categories(Constants.allProductsCategory[i],Constants.allProductsCategoryIcon[i]))
        }
        binding.rvCategories.adapter=CategoriesAdapter(categoryList)
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