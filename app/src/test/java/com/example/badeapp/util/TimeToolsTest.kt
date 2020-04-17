package com.example.badeapp.util

import org.junit.Test

import org.junit.Assert.*

class TimeToolsTest {

    @Test
    fun minBetwene() {

        //Test that the time between a time and itself is zero
        var res = minBetwene("2020-04-17T14:00:00Z","2020-04-17T14:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)


        res = minBetwene("2020-04-17T16:00:00Z","2020-04-17T16:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)

        res = minBetwene("2020-04-17T17:00:00Z","2020-04-17T17:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)


        res = minBetwene("2020-04-17T23:00:00Z","2020-04-17T23:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)


        res = minBetwene("2020-04-18T06:00:00Z","2020-04-18T06:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)


        res = minBetwene("2020-04-17T15:00:00Z","2020-04-17T15:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)

        //Test that time delta is correct

        var from = "2020-04-17T21:00:00Z"
        var to = "2020-04-17T22:00:00Z"
        res = minBetwene(from,to)
        assertNotNull(res)
        assertTrue(res?.toInt() == 60) //1h

        res = minBetwene(to,from)
        assertNotNull(res)
        assertTrue(res?.toInt() == -60) //-1h

    }
}