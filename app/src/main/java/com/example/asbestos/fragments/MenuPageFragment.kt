package com.example.asbestos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.asbestos.databinding.FragmentMenuPageBinding
import kotlinx.android.synthetic.main.fragment_menu_page.*

class MenuPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMenuPageBinding.inflate(inflater)
        binding.aboutBtn.setOnClickListener(onClickListener)
        binding.contactUsBtn.setOnClickListener(onClickListener)
        binding.getEstimateBtn.setOnClickListener(onClickListener)

        return binding.root
    }

    val onClickListener = View.OnClickListener {
        val navigation = when(it.id) {
            about_btn.id -> MenuPageFragmentDirections.actionMenuPageToAboutAsbestos()

            contact_us_btn.id -> MenuPageFragmentDirections.actionMenuPageToContactUs()

            get_estimate_btn.id -> MenuPageFragmentDirections.actionMenuPageToEstimatorFragment()

            else -> throw (Exception("Unexpected button clicked"))
        }

        findNavController().navigate(navigation)
    }
}
