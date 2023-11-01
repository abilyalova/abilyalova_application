package com.delybills.makeaway.flows.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.delybills.makeaway.R
import com.delybills.makeaway.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    var navController: NavController? = null

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setUpButtons()
    }
    private fun setUpButtons(){
        binding.startLoginButton.setOnClickListener {
            navController?.navigate(R.id.action_startFragment_to_loginFragment)
        }

        binding.startRegisterButton.setOnClickListener {
            navController?.navigate(R.id.action_startFragment_to_registerFragment)
        }
    }
}
