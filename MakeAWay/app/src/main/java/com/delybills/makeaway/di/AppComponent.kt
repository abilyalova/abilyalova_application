package com.delybills.makeaway.di

import androidx.viewbinding.ViewBinding
import com.delybills.makeaway.common.BaseFragment
import com.delybills.makeaway.common.BaseRepository
import com.delybills.makeaway.common.BaseViewModel
import com.delybills.makeaway.flows.auth.login.LoginFragment
import com.delybills.makeaway.flows.auth.register.RegisterFragment
import com.delybills.makeaway.flows.main.createdesk.CreateDeskFragment
import com.delybills.makeaway.flows.main.desk.DeskFragment
import com.delybills.makeaway.flows.main.menu.MenuFragment
import com.delybills.makeaway.flows.main.pomodoro.PomodoroFragment
import com.delybills.makeaway.flows.main.profile.ProfileFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, DataModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {
    fun inject(baseFragment: BaseFragment<BaseViewModel, BaseRepository, ViewBinding>)
    fun inject(loginFragment: LoginFragment)
    fun inject(registerFragment: RegisterFragment)
    fun inject(createDeskFragment: CreateDeskFragment)
    fun inject(menuFragment: MenuFragment)
    fun inject(pomodoroFragment: PomodoroFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(deskFragment: DeskFragment)
}