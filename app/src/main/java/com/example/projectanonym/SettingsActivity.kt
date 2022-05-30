package com.example.projectanonym

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.preference.PreferenceManager
import java.util.*


class SettingsActivity : AppCompatActivity() {

    // Locale-related variables and values
    private var currentLocale: Locale = Locale.getDefault()
    private var currentLanguage = currentLocale.toString()
    private lateinit var locale: Locale

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Inflate(show) settings fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settingsActivityLayout, SettingsFragment())
            .commit()
    }

}