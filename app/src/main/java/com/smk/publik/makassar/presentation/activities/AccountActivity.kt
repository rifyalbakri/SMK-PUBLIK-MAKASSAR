package com.smk.publik.makassar.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smk.publik.makassar.R
import com.smk.publik.makassar.databinding.ActivityFragmentsBinding
import com.smk.publik.makassar.interfaces.ActivityInterfaces
import com.smk.publik.makassar.presentation.fragments.LoginFragment
import com.smk.publik.makassar.presentation.fragments.RegisterFragment
import com.smk.publik.makassar.utils.inline.replaceFragment

/**
 * @Author Joseph Sanjaya on 20/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class AccountActivity : AppCompatActivity(R.layout.activity_fragments), ActivityInterfaces {

    private val binding by viewBinding(ActivityFragmentsBinding::bind)
    private val mType: MutableLiveData<Type> = MutableLiveData(Type.LOGIN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
        getIntentData(intent)
    }

    private fun setupObserver() {
        mType.observe(this, {
            when(it) {
                Type.LOGIN -> onFragmentChanges(LoginFragment(), isBackstack = false)
                Type.REGISTER -> onFragmentChanges(RegisterFragment(), isBackstack = false)
                else -> onToolbarChanges("Forgot Password", true, isHide = true)
            }
        })
    }

    override fun onFragmentChanges(fragment: Fragment, bundle: Bundle?, isBackstack: Boolean, isAnimate: Boolean) {
        supportFragmentManager.replaceFragment(binding.flFragments.id, fragment, bundle, isBackstack, isAnimate)
        super.onFragmentChanges(fragment, bundle, isBackstack, isAnimate)
    }

    override fun onToolbarChanges(title: String, isBack: Boolean, isHide: Boolean) {
        supportActionBar?.apply {
            elevation = 0f
            setTitle(title)
            setDisplayHomeAsUpEnabled(isBack)
            if(isHide) hide() else show()
        }
        super.onToolbarChanges(title, isBack, isHide)
    }

    override fun onPopBackStack() {
        supportFragmentManager.popBackStack()
        super.onPopBackStack()
    }

    private fun getIntentData(intent: Intent?) {
        val extras = intent?.extras
        extras?.let {
            mType.postValue(extras.get(TYPE_EXTRA) as Type)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TYPE_EXTRA = "type"
        enum class Type {
            LOGIN, REGISTER, FORGOT
        }

        fun createLoginIntent(context: Context) : Intent = Intent(context, AccountActivity::class.java).apply {
            putExtra(TYPE_EXTRA, Type.LOGIN)
        }

        fun createRegisterIntent(context: Context) : Intent = Intent(context, AccountActivity::class.java).apply {
            putExtra(TYPE_EXTRA, Type.REGISTER)
        }

        fun createForgotIntent(context: Context) : Intent = Intent(context, AccountActivity::class.java).apply {
            putExtra(TYPE_EXTRA, Type.FORGOT)
        }
    }
}