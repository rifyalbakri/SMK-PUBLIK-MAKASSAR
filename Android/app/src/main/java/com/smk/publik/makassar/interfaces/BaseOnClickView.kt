package com.smk.publik.makassar.interfaces

import android.view.View
import com.github.florent37.viewanimator.ViewAnimator


/**
 * @Author Joseph Sanjaya on 27/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

interface BaseOnClickView : View.OnClickListener {
    override fun onClick(p0: View?) {
        ViewAnimator.animate(p0).scale(0.95f,1f).duration(200).start()
    }
}