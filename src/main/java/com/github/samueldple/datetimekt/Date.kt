package com.github.samueldple.datetimekt

import com.github.samueldple.datetimekt.Utils.getLastDateInMonth
import com.github.samueldple.datetimekt.Utils.isLeapYear
import java.time.LocalDate
import java.util.regex.Pattern

/**
 * Represents a date between 1 Jan 0000 and 31 Dec 9999 inclusive.
 *
 * If d < 1 or d > the last day in the month, d will be set to 1 or the last day in the months respectively.
 *
 * If m < 0 or m > 12, 12 will be added or subtracted respectively until it is in the valid range.
 *
 * If y < 0 or y > 9999, it will be set to 0 or 9999 respectively.
 *
 * The correct way to get relative mutations is to convert using toDays and fromDays or modify individual values in a
 * new Date using the constructor.
 */
class Date(
        y: Int,
        m: Int,
        d: Int
): Comparable<Date> {

    private var y: Int
    private var m: Int
    private var d: Int

    init {
        val month = Month(y, m)
        var currentDateValue = d
        if (currentDateValue < 1) {
            currentDateValue = 1
        } else if (currentDateValue > getLastDateInMonth(month.getYear(), month.getMonth())) {
            currentDateValue = getLastDateInMonth(month.getYear(), month.getMonth())
        }
        this.d = currentDateValue
        this.m = month.getMonth()
        this.y = month.getYear()
    }

    fun getYear(): Int = y
    fun getMonth(): Int = m
    fun getDate(): Int = d

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Date
        if (d != other.d) return false
        if (m != other.m) return false
        if (y != other.y) return false
        return true
    }

    override fun hashCode(): Int {
        var result = y
        result = 31 * result + m
        result = 31 * result + d
        return result
    }

    override fun toString(): String = String.format("%04d-%02d-%02d", y, m, d)

    /**
     * Returns a String in the format 8 Jan 2019
     */
    fun toReadableString(): String = String.format("%d %s %04d", d, Month.MONTH_STRINGS[m - 1], y)

    override fun compareTo(other: Date): Int =
            if (y == other.getYear()) {
                if (m == other.getMonth()) {
                    d.compareTo(other.getDate())
                } else {
                    m.compareTo(other.getMonth())
                }
            } else {
                y.compareTo(other.getYear())
            }

    /**
     * Gets the number of days since 1 Jan 0000 (including that date and this one)
     */
    fun toDays(): Int {
        var totalDays = 0
        for (currentY in 0 until y) {
            totalDays += if (isLeapYear(currentY)) 366 else 365
        }
        for (currentM in 1 until m) {
            totalDays += getLastDateInMonth(y, currentM)
        }
        totalDays += d
        return totalDays
    }

    fun toMonth(): Month = Month.fromDate(this)

    companion object {

        private const val STRING_FORMAT_REGEX = """^\d{4}-\d{2}-\d{2}$"""

        /**
         * Gets the current date as a Date.
         */
        @JvmStatic
        @Suppress("unused")
        fun today(): Date {
            val date = LocalDate.now()
            return Date(date.year, date.monthValue, date.dayOfMonth)
        }

        /**
         * Converts a string in the format yyyy-mm-dd as would be given by
         * toString() into a Date.
         *
         * Returns null if the string is invalid.
         */
        @JvmStatic
        fun fromString(string: String): Date? {
            val pattern = Pattern.compile(STRING_FORMAT_REGEX)
            return if (pattern.matcher(string).matches()) {
                val parts = string.split('-')
                Date(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
            } else {
                null
            }
        }

        fun fromDays(totalNumberOfDays: Int): Date? {
            if (totalNumberOfDays < 1) {
                return null
            }
            var totalDays = totalNumberOfDays
            var years = 0
            var months = 1
            while (totalDays > if (isLeapYear(years)) 366 else 365) {
                totalDays -= if (isLeapYear(years)) 366 else 365
                years++
            }
            while (totalDays > getLastDateInMonth(years, months)) {
                totalDays -= getLastDateInMonth(years, months)
                months++
            }
            return Date(years, months, totalDays)
        }

    }

}