package com.delybills.makeaway.flows.auth.register

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
import com.delybills.makeaway.databinding.FragmentRegisterBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterFragment :
    BaseFragment<RegisterViewModel, RegisterRepository, FragmentRegisterBinding>() {
    private var navController: NavController? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun provideViewModel(): RegisterViewModel {
        (requireContext().applicationContext as App).appComponent.inject(this)
        return ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
    }

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        setUpButtons()
        collectRegisterDataResponse()
        observeErrorResponse()
    }

    private fun setUpButtons() {
        binding.registerButton.setOnClickListener {
            register()
        }
        binding.alreadyHasAnAccountButton.setOnClickListener {
            navController?.navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun register() {
        val username = binding.usernameRegister.text.toString()
        val email = binding.emailRegister.text.toString()
        val password = binding.passwordRegister.text.toString()
        val passwordCheck = binding.passwordCheckRegister.text.toString()

        if (validateRegistrationData(username, email, password, passwordCheck)) {
            viewModel.register(username, email, password)
        }
    }

    private fun collectRegisterDataResponse() {
        lifecycleScope.launch {
            viewModel.messageDataRegister.collectLatest {
                userPrefsManager.accessToken = it.access_token
                userPrefsManager.refreshToken = it.refresh_token
                navController?.navigate(R.id.action_registerFragment_to_mainScreenActivity)
            }
        }
    }

    private fun validateRegistrationData(
        username: String,
        email: String,
        password: String,
        password_check: String
    ): Boolean {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showToast("Заполните все поля")
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Неправильно введена почта")
            return false
        } else if (password != password_check) {
            showToast("Пароли не совпадают")
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