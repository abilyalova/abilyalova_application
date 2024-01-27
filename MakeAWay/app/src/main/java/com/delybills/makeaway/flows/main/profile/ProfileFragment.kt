package com.delybills.makeaway.flows.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.delybills.makeaway.App
import com.delybills.makeaway.common.BaseFragment
import com.delybills.makeaway.common.ViewModelFactory
import com.delybills.makeaway.databinding.FragmentProfileBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import javax.inject.Inject

class ProfileFragment :
    BaseFragment<ProfileViewModel, ProfileRepository, FragmentProfileBinding>() {

    private var navController: NavController? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun provideBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun provideViewModel(): ProfileViewModel {
        (requireContext().applicationContext as App).appComponent.inject(this)
        return ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.getUserInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        observeResponse()
        observeErrorResponse()
        setUpButtons()
    }

    private fun setUpButtons(){
        binding.logout.setOnClickListener {
            userLogout()
        }
    }
    private fun observeResponse() {
        lifecycleScope.launch {
            viewModel.messageDataGetUserInfo.collectLatest {
                binding.usernameProfileTextView.text = it.username
                binding.emailProfileTextView.text = it.email
            }
        }
    }

    private fun observeErrorResponse() {
        viewModel.errorResponse.observe(viewLifecycleOwner) {
            if (it.isNetworkFailure) {
                showToast("Проверьте подключение к интернету")
            } else {
                showToast(it.body?.string() ?: "Непредвиденная ошибка")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}