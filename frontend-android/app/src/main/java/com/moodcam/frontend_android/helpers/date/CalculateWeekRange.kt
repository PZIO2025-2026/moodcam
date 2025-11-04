package com.moodcam.frontend_android.helpers.date

import java.util.Calendar
import java.util.Date


/**
 * Calculate week start and end dates based on weeks ago
 */
fun calculateWeekRange(weeksAgo: Int): Pair<Date, Date> {
    val calendar = Calendar.getInstance().apply {
        add(Calendar.WEEK_OF_YEAR, -weeksAgo)
    }

    val weekStart = Calendar.getInstance().apply {
        time = calendar.time
        set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    val weekEnd = Calendar.getInstance().apply {
        time = weekStart
        add(Calendar.DAY_OF_YEAR, 6)
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
    }.time

    return Pair(weekStart, weekEnd)
}