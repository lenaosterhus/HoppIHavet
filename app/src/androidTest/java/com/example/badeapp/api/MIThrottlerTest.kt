package com.example.badeapp.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MIThrottlerTest {

    // We can observe livedata indefinably
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Denne testen tar 10 min
    @Test
    fun testMIThrottler() {

        assertNotNull(MIThrottler.hasHalted.value)
        assertFalse(MIThrottler.hasHalted.value!!)
        MIThrottler.submitCode(203)

        assertTrue(MIThrottler.hasHalted.value!!)
        Thread.sleep(10 * 60 * 1001)

        assertTrue(MIThrottler.canResume())
        assertFalse(MIThrottler.hasHalted.value!!)
    }
}