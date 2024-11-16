package com.esoft.emobile.di

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.esoft.emobile.BuildConfig

import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppFileProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getUriForFile(file: File): Uri? {
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    }
}