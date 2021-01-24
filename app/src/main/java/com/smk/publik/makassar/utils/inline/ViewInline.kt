package com.smk.publik.makassar.utils.inline

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.blankj.utilcode.util.SnackbarUtils
import com.github.florent37.viewanimator.ViewAnimator
import com.smk.publik.makassar.R


/**
 * @Author Joseph Sanjaya on 31/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

fun View.createSnackBar(message: String? = null) = SnackbarUtils.with(this).apply {
    if(!message.isNullOrBlank()) setMessage(message)
}

fun View.errorAnimation() {
    requestFocus()
    ViewAnimator.animate(this).pulse().duration(500).start()
}

@BindingAdapter("srcDrawable", "srcUri", "radius", "isCircular", "placeholder", "error", requireAll = false)
fun ImageView.setupImage(
    srcDrawable: Drawable? = null,
    srcUri: String? = null,
    radius: Float? = null,
    isCircular: Boolean? = null,
    placeholder: Drawable? = null,
    error: Drawable? = null,
)  = ImageRequest.Builder(context).data(srcDrawable ?: srcUri ?: R.drawable.ic_big_logo).target(this).apply {
    if (isCircular == true) CircleCropTransformation()
    else RoundedCornersTransformation(radius ?: 0f)
    if(placeholder != null) placeholder(placeholder)
    if(error != null) error(error)
}.build().let { ImageLoader(context).enqueue(it) }