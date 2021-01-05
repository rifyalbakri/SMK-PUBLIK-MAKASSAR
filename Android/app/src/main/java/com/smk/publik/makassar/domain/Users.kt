package com.smk.publik.makassar.domain

import android.provider.ContactsContract

/**
 * @Author Joseph Sanjaya on 20/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

data class Users(
    val fullName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val roles: Int? = null,
    val nuptk: String? = null
)