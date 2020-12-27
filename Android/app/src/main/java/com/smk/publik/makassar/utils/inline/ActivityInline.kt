package com.smk.publik.makassar.utils.inline

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding

/**
 * @Author Joseph Sanjaya on 06/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

fun FragmentManager.replaceFragment(placeholder: Int, fragment: Fragment, bundle: Bundle? = null, isBackstack: Boolean = false, isAnimate: Boolean = false) {
    beginTransaction().apply {
        if(isBackstack) addToBackStack(null)
        if(isAnimate) setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
        )
        replace(placeholder, fragment.apply {
            arguments = bundle
        })
    }.commit()
}