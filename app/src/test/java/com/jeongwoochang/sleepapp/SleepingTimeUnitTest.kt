package com.jeongwoochang.sleepapp

import com.jeongwoochang.sleepapp.util.SleepingTime
import org.junit.Assert
import org.junit.Test

class SleepingTimeUnitTest {
    @Test
    fun addition_isCorrect() {
        val st = SleepingTime() //By default, it uses the Locale of the phone
        st.isSleepingTime() //Is it a bad moment to do noise?
        st.getBedtime() //The average bedtime of the user's country, based in its Locale
        st.getWakeUp() //The average wake up of the user's country, based in its Locale
        Assert.assertEquals("", st.isSleepingTime.toString())
    }
}