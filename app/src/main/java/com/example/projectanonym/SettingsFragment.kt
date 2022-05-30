package com.example.projectanonym

import android.app.ProgressDialog.show
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import com.example.projectanonym.R

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    // For proper lifecycle management in the Activity or Fragment,
    // registering and unregistering the listener in the onResume() and onPause() callbacks is advised
    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    // Listener for changes in shared preferences
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
    }
}