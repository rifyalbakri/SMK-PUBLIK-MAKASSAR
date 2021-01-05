package com.smk.publik.makassar.utils.inline

import android.view.View
import com.blankj.utilcode.util.SnackbarUtils


/**
 * @Author Joseph Sanjaya on 31/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

fun View.createSnackBar(message: String? = null) = SnackbarUtils.with(this).apply {
    if(!message.isNullOrBlank()) setMessage(message)
}