package com.jeongwoochang.sleepapp

import org.junit.Assert
import org.junit.Test
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class StoringSleepTimeUnitTest {
    @Test
    fun addition_isCorrect() {
        val sc = Calendar.getInstance()
        val ec = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.KOREA)
        try {
            val sd = simpleDateFormat.parse("11:00")
            sc.set(Calendar.HOUR_OF_DAY, sd.hours)
            sc.set(Calendar.MINUTE, sd.minutes)
            sc.set(Calendar.SECOND, 0)

            val ed = simpleDateFormat.parse("06:00")
            ec.set(Calendar.HOUR_OF_DAY, ed.hours)
            ec.set(Calendar.MINUTE, ed.minutes)
            ec.set(Calendar.SECOND, 0)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val curr = Calendar.getInstance()
        curr.add(Calendar.DAY_OF_MONTH, -1)
        curr.set(Calendar.HOUR_OF_DAY, 23)
        if (curr.get(Calendar.HOUR_OF_DAY) > ec.get(Calendar.HOUR_OF_DAY)) {
            ec.add(Calendar.DAY_OF_MONTH, 1)
        } else if (curr.get(Calendar.HOUR_OF_DAY) == ec.get(Calendar.HOUR_OF_DAY)) {
            if (curr.get(Calendar.MINUTE) > ec.get(Calendar.MINUTE)) {
                ec.add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        /*if (curr.get(Calendar.HOUR_OF_DAY) ) {
            sc.add(Calendar.DAY_OF_MONTH, -1)
        }*/
        Assert.assertEquals("mola", "start = ${sc.time}, end = ${ec.time}")
    }
}