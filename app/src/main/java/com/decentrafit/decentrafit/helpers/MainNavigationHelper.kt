package com.decentrafit.decentrafit.helpers

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.decentrafit.decentrafit.R
import java.lang.ref.WeakReference
import java.util.Objects
import javax.inject.Inject

class MainNavigationHelper @Inject constructor() {

    companion object {
        const val M_HOME = "M_HOME"
        const val M_PROFILE = "M_PROFILE"
    }

    private val stack = mutableListOf<WeakReference<Fragment>>()

    fun showFragment(
        activity: AppCompatActivity,
        currentFragment: Fragment?,
        futureFragment: Fragment,
        tag: String
    ): Fragment {

        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        when (tag) {
            M_HOME, M_PROFILE -> {
                clearStack()
                fragmentManager.findFragmentByTag(tag)?.let {
                    currentFragment?.let {
                        fragmentTransaction.hide(currentFragment)
                        Objects.requireNonNull(
                            fragmentManager.findFragmentByTag(
                                tag
                            )
                        ).let {
                            it?.let { it1 ->
                                fragmentTransaction.show(
                                    it1
                                )
                            }
                        }
                    }
                } ?: run {
                    if (currentFragment != null) {
                        fragmentTransaction.hide(currentFragment)
                    }
                    fragmentTransaction.add(R.id.fragment_container, futureFragment, tag)
                }
            }

            else -> {
                if (currentFragment != null) {
                    fragmentTransaction.hide(currentFragment)
                    stack.add(WeakReference(currentFragment))
                }
                if (futureFragment.isAdded) {
                    fragmentTransaction.show(futureFragment)
                } else {
                    fragmentTransaction.add(R.id.fragment_container, futureFragment, tag)
                }
            }
        }

        fragmentTransaction
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        return futureFragment
    }

    fun backStackFragment(activity: AppCompatActivity, currentFragment: Fragment?): Fragment? {
        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        var lastFragment: Fragment? = null

        if (stack.isEmpty()) {
            activity.finish()
        } else {
            lastFragment =
                fragmentManager.findFragmentByTag(stack.getOrNull(stack.size - 1)?.get()?.tag)
            if (lastFragment != null) {
                currentFragment?.let { fragmentTransaction.remove(currentFragment) }
                fragmentTransaction.show(Objects.requireNonNull(lastFragment))
                fragmentTransaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .commit()
                stack.removeAt(stack.size - 1)
            }
        }
        return lastFragment
    }

    private fun clearStack() {
        val iterator = stack.iterator()
        while (iterator.hasNext()) {
            val fragment = iterator.next().get()
            fragment?.let { fragment ->
                if (fragment.tag != null)
                    iterator.remove()
            }
        }
    }
}