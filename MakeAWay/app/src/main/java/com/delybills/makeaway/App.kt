package com.delybills.makeaway

import android.app.Application
import com.delybills.makeaway.di.AppComponent
import com.delybills.makeaway.di.AppModule
import com.delybills.makeaway.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(context = this))
            .build()
    }

}