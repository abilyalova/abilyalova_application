package com.delybills.makeaway.flows.auth.login

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
import com.delybills.makeaway.R
import com.delybills.makeaway.common.BaseFragment
import com.delybills.makeaway.common.ViewModelFactory
import com.delybills.makeaway.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginViewModel, LoginRepository, FragmentLoginBinding>() {

    var navController: NavController? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun provideViewModel(): LoginViewModel {
        (requireContext().applicationContext as App).appComponent.inject(this)
        return ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setUpButtons()
        collectLoginDataResponse()
        observeErrorResponse()
    }

    private fun setUpButtons() {
        binding.loginButton.setOnClickListener {
            login()
        }

        binding.dontHaveAccountButton.setOnClickListener {
            navController?.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun login() {
        val username = binding.usernameLogin.text.toString()
        val password = binding.passwordLogin.text.toString()

        if (validateLoginData(username, password)) {
            viewModel.login(username, password)
        }
    }

    private fun collectLoginDataResponse() {
        lifecycleScope.launch {
            viewModel.messageDataLogin.collectLatest {
                userPrefsManager.accessToken = it.access_token
                userPrefsManager.refreshToken = it.refresh_token
                navController?.navigate(R.id.action_loginFragment_to_mainScreenActivity)
            }
        }
    }

    private fun validateLoginData(
        username: String,
        password: String
    ): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            showToast("Заполните все поля")
            return false
        }
        return true
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