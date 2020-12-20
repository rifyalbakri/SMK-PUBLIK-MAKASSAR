package com.smk.publik.makassar.domain

import com.google.firebase.auth.FirebaseUser

/**
 * @Author Joseph Sanjaya on 20/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

object UserState {
    sealed class Register {
        object Loading : Register()
        object Cancelled : Register()
        class Failed(val e: Throwable) : Register()
        class Success(val user: FirebaseUser?) : Register()
    }
    sealed class Data {
        object Loading : Data()
        class Failed(val e: Throwable) : Data()
        class Success(val user: Users?) : Data()
    }
}