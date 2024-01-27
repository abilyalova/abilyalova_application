package com.delybills.makeaway.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.delybills.makeaway.common.UserPrefsManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPrefsManager = UserPrefsManager(context = this)
        if (!userPrefsManager.accessToken.isNullOrBlank() && !userPrefsManager.refreshToken.isNullOrBlank()) {
            startActivity(
                Intent(
                    this,
                    MainScreenActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        } else {
            startActivity(
                Intent(
                    this,
                    AuthActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }
    }
}