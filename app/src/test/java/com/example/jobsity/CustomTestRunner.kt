package com.example.jobsity

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(AndroidJUnit4::class)
class CustomTestRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle?) {
        MultiDex.install(targetContext)
        super.onCreate(arguments)
    }

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}