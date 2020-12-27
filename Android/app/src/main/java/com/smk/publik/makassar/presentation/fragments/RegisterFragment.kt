package com.smk.publik.makassar.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.StringUtils
import com.google.android.material.button.MaterialButton
import com.smk.publik.makassar.R
import com.smk.publik.makassar.databinding.FragmentRegisterBinding
import com.smk.publik.makassar.interfaces.ActivityInterfaces
import com.smk.publik.makassar.interfaces.BaseOnClickView
import com.smk.publik.makassar.presentation.observer.UserObserver
import com.smk.publik.makassar.utils.inline.viewBinding


/**
 * @Author Joseph Sanjaya on 27/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class RegisterFragment : Fragment(R.layout.fragment_register), BaseOnClickView, UserObserver.Interfaces {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private var mActivityInterfaces: ActivityInterfaces? = null

    private val mButtonRolesList by lazy {
        listOf(binding.btnAdmin, binding.btnGuru, binding.btnSiswa, binding.btnUmum)
    }

    override fun onStart() {
        mActivityInterfaces?.onToolbarChanges(StringUtils.getString(R.string.button_label_sign_up), isBack = true, isHide = false)
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.listener = this
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(p0: View?) {
        when(p0) {
            binding.btnAdmin -> changeSelectedRoles(binding.btnAdmin)
            binding.btnGuru -> changeSelectedRoles(binding.btnGuru)
            binding.btnSiswa -> changeSelectedRoles(binding.btnSiswa)
            binding.btnUmum -> changeSelectedRoles(binding.btnUmum)
            binding.btnSignUp -> {}
            binding.tvSignIn -> mActivityInterfaces?.onPopBackStack()
        }
        super.onClick(p0)
    }

    private fun changeSelectedRoles(view: MaterialButton) {
        mButtonRolesList.forEach {
            it.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primaryColor)
        }
        view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondaryColor)
    }


    override fun onAttach(context: Context) {
        if(context is ActivityInterfaces) mActivityInterfaces = context
        super.onAttach(context)
    }

    override fun onDetach() {
        mActivityInterfaces = null
        super.onDetach()
    }
}