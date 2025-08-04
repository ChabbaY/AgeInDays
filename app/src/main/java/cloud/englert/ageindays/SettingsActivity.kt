package cloud.englert.ageindays

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import cloud.englert.ageindays.custom.ThemeSetup.applyTheme

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val pref = preferenceManager.findPreference<Preference>(
                getString(R.string.theme_key))
            if (pref != null) {
                pref.onPreferenceChangeListener =
                    (Preference.OnPreferenceChangeListener { _: Preference?, newValue: Any? ->
                        applyTheme((newValue as String?)!!, requireContext())
                        true
                    })
            }
        }
    }
}