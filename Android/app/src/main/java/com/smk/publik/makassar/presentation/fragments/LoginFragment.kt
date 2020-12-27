package com.smk.publik.makassar.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.smk.publik.makassar.R
import com.smk.publik.makassar.databinding.FragmentLoginBinding
import com.smk.publik.makassar.interfaces.ActivityInterfaces
import com.smk.publik.makassar.interfaces.BaseOnClickView
import com.smk.publik.makassar.presentation.observer.UserObserver
import com.smk.publik.makassar.presentation.viewmodel.UserViewModel
import com.smk.publik.makassar.utils.inline.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * @Author Joseph Sanjaya on 27/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class LoginFragment : Fragment(R.layout.fragment_login), BaseOnClickView, UserObserver.Interfaces {
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private var mActivityInterfaces: ActivityInterfaces? = null
    private val mViewModel: UserViewModel by viewModel()

    override fun onStart() {
        mActivityInterfaces?.onToolbarChanges("Login", true, isHide = true)
        super.onStart()
    }

    override fun onClick(p0: View?) {
        when(p0) {
            binding.btnLogin -> {}
            binding.tvSignUp -> mActivityInterfaces?.onFragmentChanges(RegisterFragment(), isBackstack = true, isAnimate = true)
        }
        super.onClick(p0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ivLogo.load(R.drawable.ic_big_logo)
        binding.listener = this
        super.onViewCreated(view, savedInstanceState)
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