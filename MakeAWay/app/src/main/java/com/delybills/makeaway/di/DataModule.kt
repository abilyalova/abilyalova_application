package com.delybills.makeaway.di

import android.content.Context
import com.delybills.makeaway.data.network.RetrofitInstance
import com.delybills.makeaway.data.network.api.AuthApi
import com.delybills.makeaway.data.network.api.DeskApi
import com.delybills.makeaway.data.network.api.MenuApi
import com.delybills.makeaway.data.network.api.ProfileApi
import com.delybills.makeaway.flows.auth.login.LoginRepository
import com.delybills.makeaway.flows.auth.register.RegisterRepository
import com.delybills.makeaway.flows.main.createdesk.CreateDeskRepository
import com.delybills.makeaway.flows.main.desk.DeskRepository
import com.delybills.makeaway.flows.main.menu.MenuRepository
import com.delybills.makeaway.flows.main.pomodoro.PomodoroRepository
import com.delybills.makeaway.flows.main.profile.ProfileRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideLoginRepository(context: Context): LoginRepository =
        LoginRepository(RetrofitInstance().provideApi(AuthApi::class.java, context))

    @Provides
    fun provideRegisterRepository(context: Context): RegisterRepository =
        RegisterRepository(RetrofitInstance().provideApi(AuthApi::class.java, context))

    @Provides
    fun provideMenuRepository(context: Context): MenuRepository =
        MenuRepository(RetrofitInstance().provideApi(MenuApi::class.java, context))

    @Provides
    fun provideCreateDeskRepository(context: Context): CreateDeskRepository =
        CreateDeskRepository(RetrofitInstance().provideApi(MenuApi::class.java, context))

    @Provides
    fun providePomodoroRepository(context: Context): PomodoroRepository =
        PomodoroRepository(RetrofitInstance().provideApi(MenuApi::class.java, context))

    @Provides
    fun provideProfileRepository(context: Context): ProfileRepository =
        ProfileRepository(RetrofitInstance().provideApi(ProfileApi::class.java, context))

    @Provides
    fun provideDeskRepository(context: Context): DeskRepository =
        DeskRepository(RetrofitInstance().provideApi(DeskApi::class.java, context))
}