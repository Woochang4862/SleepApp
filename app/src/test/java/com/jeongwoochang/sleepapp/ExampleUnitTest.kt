package com.jeongwoochang.sleepapp

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val sdf = SimpleDateFormat("HH:mm")
        var d = sdf.parse("23:00") // Thu Jan 01 23:00:00 KST 1970
        var c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, d.hours)
        c.set(Calendar.MINUTE, d.minutes)
        c.set(Calendar.SECOND, 0) // c.time == Tue Jun 04 23:00:00 KST 2019
        var diff = c.timeInMillis - Calendar.getInstance().timeInMillis
        if(diff < 0) diff = 0

        System.out.println(!Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]\$").matcher(String.format("%02d:%02d",6,0)).matches())
        assertEquals("mola", diff)
    }
}
