package io.github.achmadhafid.mathscanner.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.github.achmadhafid.mathscanner.R

class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}
