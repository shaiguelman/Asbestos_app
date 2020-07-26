package com.example.asbestos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.asbestos.databinding.FragmentContactUsBinding
import com.example.asbestos.readTextFromRawFile

private const val TEXT_FILE_NAME = "contact_us"

class ContactUsFragment: Fragment() {

    private lateinit var binding: FragmentContactUsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContactUsBinding.inflate(inflater)

        binding.bodyText.text = readTextFromRawFile(this.context!!, TEXT_FILE_NAME)
        binding.goBackBtn.setOnClickListener {
            findNavController().navigate(
                ContactUsFragmentDirections.actionContactUsToMenuPage()
            )
        }

        return binding.root
    }
}