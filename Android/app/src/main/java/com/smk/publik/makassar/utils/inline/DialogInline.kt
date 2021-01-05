package com.smk.publik.makassar.utils.inline

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.smk.publik.makassar.databinding.DialogLoadingBinding


/**
 * @Author Joseph Sanjaya on 31/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

/**
 * Loading Dialog
 * ketika dipanggil akan membuat alert dialog (loading) baru , lalu memberikan 2 value :
 *
 * [first] akan [return] AlertDialog
 *
 * [second] akan [return] viewBinding agar dapat merubah text
 */

inline val Context.loadingDialog: Pair<AlertDialog, DialogLoadingBinding>
    get()  {
        val view = DialogLoadingBinding.inflate(
            LayoutInflater.from(this)
        )
        val dialog = AlertDialog.Builder(this)
            .setView(view.root)
            .create().apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            }
        return Pair(dialog, view)
    }
