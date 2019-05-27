import Consts.MINUTES_IN_AN_HOUR
import Consts.SECONDS_IN_AN_HOUR
import Consts.SECONDS_IN_A_MINUTE

abstract class TimeType (
    private var h: Int,
    private var m: Int,
    private var s: Int
): Comparable<TimeType> {

    fun getHours(): Int = h
    fun getMinutes(): Int = m
    fun getSeconds(): Int = s

    fun toTime(): Time = Time.new(h, m, s)
    fun toDuration(): Duration = Duration.new(h, m, s)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TimeType
        if (h != other.h) return false
        if (m != other.m) return false
        if (s != other.s) return false
        return true
    }

    override fun hashCode(): Int {
        var result = h
        result = 31 * result + m
        result = 31 * result + s
        return result
    }

    override fun toString(): String {
        return String.format("%02d:%02d:%02d", h, m, s)
    }

    override fun compareTo(other: TimeType): Int = this.toSeconds().compareTo(other.toSeconds())

    /**
     * Produces a string such as 08:30 for 8 hours and 30 minutes.
     *
     * Ignores seconds.
     */
    fun toHHMMString(): String {
        return String.format("%02d:%02d", h, m)
    }

    /**
     * Gets the total number of seconds in the time.
     */
    fun toSeconds(): Int = SECONDS_IN_AN_HOUR * h + SECONDS_IN_A_MINUTE * m + s

    /**
     * Gets the total number of minutes in the time, ignoring seconds.
     */
    fun toMinutes(): Int = MINUTES_IN_AN_HOUR * h + m

}