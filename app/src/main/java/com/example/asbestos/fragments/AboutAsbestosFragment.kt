package com.example.asbestos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.asbestos.databinding.FragmentAboutAsbestosBinding
import com.example.asbestos.readTextFromRawFile

private const val TEXT_FILE_NAME = "about_asbestos"

class AboutAsbestosFragment : Fragment() {

    private lateinit var binding: FragmentAboutAsbestosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAboutAsbestosBinding.inflate(inflater)

        binding.aboutAsbestosBody.text = readTextFromRawFile(context!!, TEXT_FILE_NAME)
        binding.goBackBtn.setOnClickListener {
            findNavController().navigate(
                AboutAsbestosFragmentDirections.actionAboutAsbestosToMenuPage()
            )
        }

        return binding.root
    }
}