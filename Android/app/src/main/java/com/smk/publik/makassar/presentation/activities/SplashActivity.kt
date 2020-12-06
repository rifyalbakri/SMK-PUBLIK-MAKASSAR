package com.smk.publik.makassar.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smk.publik.makassar.databinding.ActivitySplashBinding
import com.smk.publik.makassar.utils.inline.viewBinding

/**
 * @Author Joseph Sanjaya on 06/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class SplashActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySplashBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnCrash.setOnClickListener {
            throw RuntimeException("Test Crash")
        }
    }
}