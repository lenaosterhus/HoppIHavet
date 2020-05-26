package com.example.badeapp.util

import org.junit.Assert.*
import org.junit.Test
import java.lang.Math.abs

class TimeToolsTest {

    @Test
    fun minBetwene() {

        //@TODO write more tests, maybe gather some more unusual ISO8601 gmt times

        //Test that the time between a time and itself is zero
        var res = minBetween("2020-04-17T14:00:00Z","2020-04-17T14:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)


        res = minBetween("2020-04-17T16:00:00Z","2020-04-17T16:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)

        res = minBetween("2020-04-17T17:00:00Z","2020-04-17T17:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)


        res = minBetween("2020-04-17T23:00:00Z","2020-04-17T23:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)


        res = minBetween("2020-04-18T06:00:00Z","2020-04-18T06:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)


        res = minBetween("2020-04-17T15:00:00Z","2020-04-17T15:00:00Z")
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)

        //Test that time delta is correct

        var from = "2020-04-17T21:00:00Z"
        var to = "2020-04-17T22:00:00Z"
        res = minBetween(from,to)
        assertNotNull(res)
        assertTrue(res?.toInt() == 60) //1h

        res = minBetween(to,from)
        assertNotNull(res)
        assertTrue(res?.toInt() == -60) //-1h

        //-----------------------------
        from = "2020-04-18T04:00:00Z"
        to =   "2020-04-18T10:00:00Z"

        res = minBetween(from,from)
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)

        res = minBetween(to,to)
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)

        res = minBetween(from,to)
        assertNotNull(res)
        assertTrue(0L < res!!)


        //-----------------------------
        from = "2020-04-18T00:00:00Z"
        to =   "2020-04-18T06:00:00Z"

        res = minBetween(from,from)
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)

        res = minBetween(to,to)
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)

        res = minBetween(from,to)
        assertNotNull(res)
        assertTrue(0L < res!!)

        //-----------------------------
        from = "2020-04-17T14:00:00Z"
        to =   "2020-04-17T20:00:00Z"

        res = minBetween(from,from)
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)

        res = minBetween(to,to)
        assertNotNull(res)
        assertTrue(res?.toInt() == 0)

        res = minBetween(from,to)
        assertNotNull(res)
        assertTrue(0L < res!!)
    }

    @Test
    fun inTheFutureFromNow() {
        val now = inTheFutureFromNow(0)
        var future = inTheFutureFromNow(10)
        assertTrue(now.before(future))

        Thread.sleep(100L)
        future = inTheFutureFromNow(0)
        assertTrue(now.before(future))
    }

    @Test
    fun toGmtIsoStringAndParseGmtIsoDate() {

        val nowS = "2020-04-17T20:00:00Z"
        val onePassS = nowS.parseAsGmtIsoDate()!!.toGmtIsoString()
        assertEquals(nowS,onePassS)

        val now = inTheFutureFromNow(0)
        val onePass = now.toGmtIsoString().parseAsGmtIsoDate()
        assertNotNull(onePass)
        assertEquals(now.toGmtIsoString(),onePass!!.toGmtIsoString())

    }

    @Test
    fun testAddOneHour() {
        val now = inTheFutureFromNow(0)
        val future = inTheFutureFromNow(60)

        assertTrue(abs(future.time - now.addOneHour().time) < 1000)
    }




}