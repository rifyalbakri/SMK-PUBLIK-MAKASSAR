package com.smk.publik.makassar.utils.inline

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.color.MaterialColors
import com.smk.publik.makassar.R

/**
 * @Author Joseph Sanjaya on 06/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

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

fun Context.showErrorToast(message: String) {
    ToastUtils.getDefaultMaker()
        .setTopIcon(R.drawable.googleg_disabled_color_18)
        .setBgColor(MaterialColors.getColor(this, R.attr.colorError, null))
        .show(message)
}

fun Context.showSuccessToast(message: String) {
    ToastUtils.getDefaultMaker()
        .setTopIcon(R.drawable.googleg_disabled_color_18)
        .setBgColor(MaterialColors.getColor(this, R.attr.colorApproved, null))
        .show(message)
}