package com.smk.publik.makassar.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseUser
import com.smk.publik.makassar.R
import com.smk.publik.makassar.databinding.FragmentRegisterBinding
import com.smk.publik.makassar.interfaces.ActivityInterfaces
import com.smk.publik.makassar.interfaces.BaseOnClickView
import com.smk.publik.makassar.presentation.observer.UserObserver
import com.smk.publik.makassar.presentation.viewmodel.UserViewModel
import com.smk.publik.makassar.utils.inline.loadingDialog
import com.smk.publik.makassar.utils.inline.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * @Author Joseph Sanjaya on 27/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class RegisterFragment : Fragment(R.layout.fragment_register), BaseOnClickView, UserObserver.Interfaces {

    private val loading by lazy { requireContext().loadingDialog }
    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private var mActivityInterfaces: ActivityInterfaces? = null
    private val mViewModel: UserViewModel by viewModel()

    private val mButtonRolesList by lazy {
        listOf(binding.btnAdmin, binding.btnGuru, binding.btnSiswa, binding.btnUmum)
    }

    override fun onStart() {
        mActivityInterfaces?.onToolbarChanges(StringUtils.getString(R.string.button_label_sign_up), isBack = true, isHide = false)
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(UserObserver(this, mViewModel, viewLifecycleOwner))
        binding.listener = this
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(p0: View?) {
        when(p0) {
            binding.btnAdmin -> changeSelectedRoles(binding.btnAdmin)
            binding.btnGuru -> changeSelectedRoles(binding.btnGuru)
            binding.btnSiswa -> changeSelectedRoles(binding.btnSiswa)
            binding.btnUmum -> changeSelectedRoles(binding.btnUmum)
            binding.btnSignUp -> mViewModel.register("sanjayajosep@gmail.com", "qwerty12#")
            binding.tvSignIn -> {
                mActivityInterfaces?.onPopBackStack()
            }
        }
        super.onClick(p0)
    }

    private fun changeSelectedRoles(view: MaterialButton) {
        mButtonRolesList.forEach {
            it.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primaryColor)
        }
        view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondaryColor)
    }

    override fun onRegisterLoading() {
        loading.first.show()
        super.onRegisterLoading()
    }

    override fun onRegisterFailed(e: Throwable) {
        loading.first.dismiss()
        SnackbarUtils.with(binding.root).setMessage(e.message.toString()).showError()
        super.onLoginFailed(e)
    }

    override fun onRegisterSuccess(user: FirebaseUser?) {
        loading.first.dismiss()
        SnackbarUtils.with(binding.root).setMessage("Success, registered ${user?.uid}").showSuccess()
        super.onLoginSuccess(user)
    }


    override fun onAttach(context: Context) {
        if(context is ActivityInterfaces) mActivityInterfaces = context
        super.onAttach(context)
    }

    override fun onDetach() {
        if(loading.first.isShowing) loading.first.dismiss()
        mActivityInterfaces = null
        super.onDetach()
    }
}