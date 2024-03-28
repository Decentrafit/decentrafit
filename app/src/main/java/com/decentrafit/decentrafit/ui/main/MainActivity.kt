package com.decentrafit.decentrafit.ui.main

import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.decentrafit.decentrafit.R
import com.decentrafit.decentrafit.databinding.ActivityMainBinding
import com.decentrafit.decentrafit.helpers.MainNavigationHelper
import com.decentrafit.decentrafit.ui.home.HomeFragment
import com.decentrafit.decentrafit.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentManager: FragmentManager
    private var currentFragment: Fragment? = null

    @Inject
    lateinit var mainNavigationHelper: MainNavigationHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initFragments()
        initBottomBarListeners()
        openHome()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        currentFragment = mainNavigationHelper.backStackFragment(this, currentFragment)
    }

    private fun initFragments() {
        homeFragment = HomeFragment()
        profileFragment = ProfileFragment()
        fragmentManager = supportFragmentManager
    }



    private fun navigateTo(destinationFragment: Fragment, label: String) {
        currentFragment = mainNavigationHelper.showFragment(
            this,
            currentFragment,
            destinationFragment,
            label,
        )
    }

    private fun openHome() {
        changeBottomBarIcon(R.id.item_home)
        navigateTo(homeFragment, MainNavigationHelper.M_HOME)
    }
    private fun openProfile() {
        changeBottomBarIcon(R.id.item_profile)
        navigateTo(profileFragment, MainNavigationHelper.M_PROFILE)
    }

    private fun changeBottomBarIcon(itemId: Int) {
        with(binding.bottomBarMenu) {
            setOnItemSelectedListener { false }
            menu.forEach { item ->
                if (item.itemId == itemId) {
                    setSelectedIcon(item)
                } else {
                    setDeselectedIcon(item)
                }
            }
        }
        initBottomBarListeners()
    }

    private fun initBottomBarListeners() {
        binding.bottomBarMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> {
                    openHome()
                }
                R.id.item_profile -> {
                    openProfile()
                }
            }
            true
        }
    }


    private fun setSelectedIcon(item: MenuItem) {
        item.isChecked = true
        item.icon = when (item.itemId) {
            R.id.item_home -> ContextCompat.getDrawable(this, R.drawable.ic_home_selected)
            R.id.item_profile -> ContextCompat.getDrawable(this, R.drawable.ic_profile_selected)
            else -> item.icon
        }
    }

    private fun setDeselectedIcon(item: MenuItem) {
        item.icon = when (item.itemId) {
            R.id.item_home -> ContextCompat.getDrawable(this, R.drawable.ic_home_unselected)
            R.id.item_profile -> ContextCompat.getDrawable(this, R.drawable.ic_profile_unselected)
            else -> item.icon
        }
    }

}