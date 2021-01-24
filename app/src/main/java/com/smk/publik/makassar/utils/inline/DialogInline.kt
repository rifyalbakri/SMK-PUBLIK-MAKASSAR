package com.smk.publik.makassar.utils.inline

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smk.publik.makassar.R
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
 * [second] akan [return] viewBinding
 */

inline fun<T: ViewBinding> Context.makeCustomViewDialog(
    crossinline bindingInflater: (LayoutInflater) -> T,
    isCancelable: Boolean = true,
    isTransparent: Boolean = false,
    onDismissListener: DialogInterface.OnDismissListener? = null,
) : Pair<T, AlertDialog,> {
    val layout = bindingInflater.invoke(LayoutInflater.from(this@makeCustomViewDialog))
    val dialog = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded).apply {
        setView(layout.root)
        if (isTransparent) background = ColorDrawable(Color.TRANSPARENT)
        setOnDismissListener(onDismissListener)
        setCancelable(isCancelable)
    }.create()
    return Pair(layout, dialog,)
}

fun Context.makeLoadingDialog(isCancelable: Boolean = true, onDismissListener: DialogInterface.OnDismissListener? = null,) : Pair<DialogLoadingBinding, AlertDialog> {
    return makeCustomViewDialog(DialogLoadingBinding::inflate, isCancelable, true, onDismissListener)
}
