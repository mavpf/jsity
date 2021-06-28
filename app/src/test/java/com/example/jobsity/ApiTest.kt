package com.example.jobsity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jobsity.network.api.ServiceApiHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class ApiTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var serviceApiHelper: ServiceApiHelper

    @Before
    fun init(){
        hiltRule.inject()
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    @Config(manifest=Config.NONE)
    @Throws(Exception::class)
    fun testApi(){
        runBlocking(Dispatchers.Default) {
            val search = serviceApiHelper.searchPerson("lauren")
            Assert.assertEquals(10, search.count())
            val show = serviceApiHelper.getShowDetail(1)
            Assert.assertEquals("Under the Dome", show.name)
        }
    }
}