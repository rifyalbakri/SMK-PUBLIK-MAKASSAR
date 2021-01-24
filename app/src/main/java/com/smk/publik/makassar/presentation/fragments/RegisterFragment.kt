package com.smk.publik.makassar.presentation.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.vvalidator.form
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.StringUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors
import com.google.firebase.auth.FirebaseUser
import com.smk.publik.makassar.R
import com.smk.publik.makassar.databinding.FragmentRegisterBinding
import com.smk.publik.makassar.interfaces.ActivityInterfaces
import com.smk.publik.makassar.interfaces.BaseOnClickView
import com.smk.publik.makassar.presentation.observer.UserObserver
import com.smk.publik.makassar.presentation.viewmodel.UserViewModel
import com.smk.publik.makassar.utils.inline.errorAnimation
import com.smk.publik.makassar.utils.inline.makeLoadingDialog
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * @Author Joseph Sanjaya on 27/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class RegisterFragment : Fragment(R.layout.fragment_register), BaseOnClickView, UserObserver.Interfaces {

    private val loading by lazy { requireContext().makeLoadingDialog(false) }
    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private var mActivityInterfaces: ActivityInterfaces? = null
    private val mViewModel: UserViewModel by viewModel()
    private val isTeacher = ObservableBoolean(false)

    private val mButtonRolesList by lazy {
        listOf(binding.btnGuru, binding.btnSiswa)
    }

    override fun onStart() {
        mActivityInterfaces?.onToolbarChanges(StringUtils.getString(R.string.button_label_sign_up), isBack = true, isHide = false)
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(UserObserver(this, mViewModel, viewLifecycleOwner))
        binding.isTeacher = isTeacher
        binding.listener = this
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(p0: View?) {
        when(p0) {
            binding.btnGuru -> changeSelectedRoles(binding.btnGuru)
            binding.btnSiswa -> changeSelectedRoles(binding.btnSiswa)
            binding.btnRegister -> mViewModel.register("sanjayajosep@gmail.com", "qwerty12#")
        }
        super.onClick(p0)
    }

    private fun changeSelectedRoles(view: MaterialButton) {
        isTeacher.set(view == binding.btnGuru)
        mButtonRolesList.forEach {
            it.backgroundTintList = ColorStateList.valueOf(MaterialColors.getColor(it, R.attr.colorPrimaryVariant))
            it.setTextColor(MaterialColors.getColor(it, R.attr.colorOnApproved))
        }
        view.apply {
            backgroundTintList = ColorStateList.valueOf(MaterialColors.getColor(this, R.attr.colorSecondary))
            setTextColor(MaterialColors.getColor(this, R.attr.colorPrimaryVariant))
        }
    }

    override fun onRegisterIdle() {
        loading.second.dismiss()
        super.onRegisterIdle()
    }

    override fun onRegisterLoading() {
        loading.second.show()
        super.onRegisterLoading()
    }

    override fun onRegisterFailed(e: Throwable) {
        SnackbarUtils.with(binding.root).setMessage(e.message.toString()).showError()
        mViewModel.resetRegisterState()
        super.onLoginFailed(e)
    }

    override fun onRegisterSuccess(user: FirebaseUser?) {
        SnackbarUtils.with(binding.root).setMessage("Success, registered ${user?.uid}").showSuccess()
        mViewModel.resetRegisterState()
        super.onLoginSuccess(user)
    }


    override fun onAttach(context: Context) {
        if(context is ActivityInterfaces) mActivityInterfaces = context
        super.onAttach(context)
    }

    override fun onDetach() {
        if(loading.second.isShowing) loading.second.dismiss()
        mActivityInterfaces = null
        super.onDetach()
    }
}