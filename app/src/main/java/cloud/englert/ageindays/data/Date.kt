package cloud.englert.ageindays.data

import java.util.Locale

class Date (var year: Int, var month: Int, var day: Int) {
    override fun toString(): String {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d",
            year, month, day)
    }

    companion object {
        fun fromString(date: String): Date {
            val values = date.split("-").toTypedArray()
            return Date(values[0].toInt(),
                values[1].toInt(),
                values[2].toInt())
        }
    }
}