package com.smk.publik.makassar.di

import androidx.datastore.createDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.smk.publik.makassar.data.repositories.MataPelajaranRepository
import com.smk.publik.makassar.data.repositories.UserRepository
import com.smk.publik.makassar.presentation.viewmodel.MataPelajaranViewModel
import com.smk.publik.makassar.presentation.viewmodel.UserViewModel
import com.smk.publik.makassar.utils.UserSerializer
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @Author Joseph Sanjaya on 20/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

object AppModule {
    private const val KEYSET_NAME = "master_keyset"
    private const val PREFERENCE_FILE = "master_key_preference"
    private const val MASTER_KEY_URI = "android-keystore://master_key"
    private const val DATASTORE_FILE = "user.pb"

    val dataStore = module {
        single<Aead> {
            AeadConfig.register()
            AndroidKeysetManager.Builder()
                .withSharedPref(androidApplication(), KEYSET_NAME, PREFERENCE_FILE)
                .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .keysetHandle
                .getPrimitive(Aead::class.java)
        }
        factory { androidApplication().createDataStore(
            fileName = DATASTORE_FILE,
            serializer = UserSerializer(get())
        ) }

        single {
            Firebase.auth
        }
        single {
            Firebase.database.reference
        }
        single {
            Firebase.storage.reference
        }

        single {
            UserRepository(get(), get(), get())
        }
        viewModel {
            UserViewModel(get())
        }
        single {
            MataPelajaranRepository(get(), get(), get())
        }
        viewModel {
            MataPelajaranViewModel(get())
        }
    }
}