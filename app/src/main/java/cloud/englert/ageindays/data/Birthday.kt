package cloud.englert.ageindays.data

import android.content.ContentValues

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
        return 23 // TODO
    }

    fun getAgeDays(): Int {
        return 9000 // TODO
    }
}