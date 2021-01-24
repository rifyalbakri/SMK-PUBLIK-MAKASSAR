package com.smk.publik.makassar

import ando.file.core.FileOperator
import androidx.multidex.MultiDexApplication
import com.smk.publik.makassar.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @Author Joseph Sanjaya on 20/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class MakassarApplication : MultiDexApplication() {

    override fun onCreate() {
        startKoin{
            androidLogger()
            androidContext(this@MakassarApplication)
            modules(AppModule.dataStore)
        }
        FileOperator.init(this,BuildConfig.DEBUG)
        super.onCreate()
    }
}