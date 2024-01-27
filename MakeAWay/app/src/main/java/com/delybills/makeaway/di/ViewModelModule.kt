package com.delybills.makeaway.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.delybills.makeaway.common.ViewModelFactory
import com.delybills.makeaway.domain.model.desk.Desk
import com.delybills.makeaway.flows.auth.login.LoginViewModel
import com.delybills.makeaway.flows.auth.register.RegisterViewModel
import com.delybills.makeaway.flows.main.createdesk.CreateDeskViewModel
import com.delybills.makeaway.flows.main.desk.DeskViewModel
import com.delybills.makeaway.flows.main.menu.MenuViewModel
import com.delybills.makeaway.flows.main.pomodoro.PomodoroViewModel
import com.delybills.makeaway.flows.main.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun registerViewModel(viewModel: RegisterViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    abstract fun menuViewModel(viewModel: MenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateDeskViewModel::class)
    abstract fun createCreateDeskViewModel(viewModel: CreateDeskViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(PomodoroViewModel::class)
    abstract fun createPomodoroViewModel(viewModel: PomodoroViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun createProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeskViewModel::class)
    abstract fun createDeskViewModel(viewModel: DeskViewModel): ViewModel
}