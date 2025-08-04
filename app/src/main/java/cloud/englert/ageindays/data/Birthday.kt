package cloud.englert.ageindays.data

import android.content.ContentValues

import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

import cloud.englert.ageindays.db.BirthdayDbHelper

class Birthday(
    var id: Long,
    var date: Date,
    var person: String,
    var selected: Boolean
) {
    val contentValues: ContentValues
        get() {
            val values = ContentValues()
            values.put(BirthdayDbHelper.COLUMN_DATE, date.toString())
            values.put(BirthdayDbHelper.COLUMN_PERSON, person)
            return values
        }

    fun getAgeYears(): Int {
        return Period.between(date.toLocalDate(), LocalDate.now()).years
    }

    fun getAgeDays(): Int {
        return ChronoUnit.DAYS.between(date.toLocalDate(),
            LocalDate.now()).toInt()
    }
}