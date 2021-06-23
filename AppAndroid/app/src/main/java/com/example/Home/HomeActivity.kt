package com.example.Home

import HomeFragment
import ServicesFragment
import SettingsFragment
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.new_home_activity.*

class HomeActivity : AppCompatActivity() {

    var userName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_home_activity)

        val homeElectricityFragment = HomeFragment()
        val servicesFragment = ServicesFragment()
        val settingsFragment = SettingsFragment()

        val sharedPref = this.getSharedPreferences(
            getString(R.string.USER_ACCOUNT_INFO_PREF), Context.MODE_PRIVATE)
        userName = sharedPref.getString(getString(R.string.USER_NAME_PREF_KEY),null).toString()

        val textView : TextView = findViewById(R.id.user_welcome)
        textView.setText("Welcome Home $userName !")


        setCurrentFragment(homeElectricityFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(homeElectricityFragment)
                R.id.service
                ->setCurrentFragment(servicesFragment)
                R.id.settings->setCurrentFragment(settingsFragment)

            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}
