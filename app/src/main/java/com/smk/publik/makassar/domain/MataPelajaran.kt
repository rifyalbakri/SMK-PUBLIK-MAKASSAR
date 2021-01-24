package com.smk.publik.makassar.domain

import android.provider.ContactsContract
import com.google.firebase.database.PropertyName

/**
 * @Author Joseph Sanjaya on 20/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

object MataPelajaran {

    data class Detail(

            @PropertyName("nama")
            var nama: String? = null,

            @PropertyName("despripsi")
            var despripsi: String? = null,

            @PropertyName("materi")
            var materi: String? = null,

    )

    data class Materi(

            @PropertyName("id")
            var id: String? = null,

            @PropertyName("judul")
            var judul: String? = null,

            @PropertyName("deskripsi")
            var deskripsi: String? = null,

            @PropertyName("attachment")
            var attachment: String? = null

    )
}
