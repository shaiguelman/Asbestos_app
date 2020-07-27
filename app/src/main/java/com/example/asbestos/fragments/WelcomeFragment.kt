package layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.asbestos.databinding.FragmentWelcomePageBinding

class WelcomeFragment: Fragment() {

    private lateinit var binding: FragmentWelcomePageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWelcomePageBinding.inflate(inflater)
        val enterButton = binding.enterButton

        enterButton.setOnClickListener {
            findNavController().navigate(
                WelcomeFragmentDirections.actionWelcomeFragmentToMenuPage()
            )
        }

        return binding.root
    }
}