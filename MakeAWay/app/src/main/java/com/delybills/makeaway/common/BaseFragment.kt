package com.delybills.makeaway.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.delybills.makeaway.activities.AuthActivity
import kotlinx.coroutines.launch

abstract class BaseFragment<viewModel : BaseViewModel, repo : BaseRepository, viewBinding : ViewBinding> :
    Fragment() {

    protected lateinit var binding: viewBinding
    protected lateinit var viewModel: viewModel

    protected lateinit var userPrefsManager: UserPrefsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = provideBinding(inflater, container)
        userPrefsManager = UserPrefsManager(requireContext())
        viewModel = provideViewModel()

        return binding.root
    }

    abstract fun provideViewModel(): viewModel
    fun userLogout() = lifecycleScope.launch {
        userPrefsManager.clearUser()
        requireActivity().startActivityClearBackStack(AuthActivity::class.java)
    }

    abstract fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): viewBinding
}