package com.redgunner.novanews.view.fragment

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.redgunner.novanews.R
import com.redgunner.novanews.utils.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {


    @Inject
    lateinit var userPreferences: UserPreferences


    override fun onStart() {
        super.onStart()
        userPreferences.themePreferences.asLiveData().observe(viewLifecycleOwner, { isDark ->
            if (isDark != null) {
                DrakSwitch.isChecked = isDark
            }

        })
    }

    override fun onResume() {
        super.onResume()





        settingsBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        rateThisAppIMG.setOnClickListener {
            rateThisApp()
        }


        rateThisAppTXT.setOnClickListener {

            rateThisApp()

        }


        shearThisAppIMG.setOnClickListener {
            shearThisApp()
        }

        shearThisAppTXT.setOnClickListener {
            shearThisApp()
        }


        DrakSwitch.setOnCheckedChangeListener { buttonView, isChecked ->


            lifecycleScope.launch {
                userPreferences.saveUserThemePreferences(isChecked)


            }


            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
        }
    }

    private fun shearThisApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, "See And Zoom Every Instagram Account Profile With ${
                    resources.getText(
                        R.string.app_name
                    )
                } On Play Store Download Now \n https://play.google.com/store/apps/details?id=${requireContext().packageName}  "
            )
            type = "text/plain"


        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun rateThisApp() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${requireContext().packageName}")))

    }


}