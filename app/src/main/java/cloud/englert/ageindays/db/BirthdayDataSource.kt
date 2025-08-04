package cloud.englert.ageindays.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import cloud.englert.ageindays.data.Birthday
import cloud.englert.ageindays.data.Date

class BirthdayDataSource(context: Context?) {
    private lateinit var database: SQLiteDatabase
    private val dbHelper: BirthdayDbHelper

    private val columns = arrayOf(
        BirthdayDbHelper.COLUMN_ID,
        BirthdayDbHelper.COLUMN_DATE,
        BirthdayDbHelper.COLUMN_PERSON
    )

    init {
        Log.d(LOG_TAG, "creating dbHelper")
        dbHelper = BirthdayDbHelper(context)
    }

    fun open() {
        Log.d(LOG_TAG, "opening database")
        database = dbHelper.writableDatabase
        Log.d(LOG_TAG, "database opened")
    }

    fun close() {
        dbHelper.close()
        Log.d(LOG_TAG, "database closed")
    }

    private fun cursorToBirthday(cursor: Cursor): Birthday {
        val idIndex = cursor.getColumnIndex(BirthdayDbHelper.COLUMN_ID)
        val idDate = cursor.getColumnIndex(BirthdayDbHelper.COLUMN_DATE)
        val idPerson = cursor.getColumnIndex(BirthdayDbHelper.COLUMN_PERSON)

        val id = cursor.getLong(idIndex)
        val date = Date.fromString(cursor.getString(idDate))
        val person = cursor.getString(idPerson)

        return Birthday(id, date, person, false)
    }

    fun getAllBirthdays(): List<Birthday> {
        val birthdays = mutableListOf<Birthday>()

        val cursor = database.query(BirthdayDbHelper.TABLE_BIRTHDAYS, columns,
            null, null, null, null,
            BirthdayDbHelper.COLUMN_DATE + " DESC")

        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            val birthday = cursorToBirthday(cursor)
            birthdays.add(birthday)
            cursor.moveToNext()
        }

        cursor.close()
        return birthdays
    }

    fun getBirthday(id: Long): Birthday {
        val cursor = database.query(BirthdayDbHelper.TABLE_BIRTHDAYS, columns,
            BirthdayDbHelper.COLUMN_ID + " = " + id,
            null, null, null, null)

        cursor.moveToFirst()
        val birthday = cursorToBirthday(cursor)
        cursor.close()
        return birthday
    }

    fun createBirthday(birthday: Birthday): Birthday {
        val insertId = database.insert(
            BirthdayDbHelper.TABLE_BIRTHDAYS, null, birthday.contentValues
        )

        return getBirthday(insertId)
    }

    fun updateBirthday(birthday: Birthday): Birthday {
        database.update(
            BirthdayDbHelper.TABLE_BIRTHDAYS, birthday.contentValues,
            BirthdayDbHelper.COLUMN_ID + " = " + birthday.id, null
        )

        return getBirthday(birthday.id)
    }

    fun deleteBirthday(birthday: Birthday) {
        database.delete(
            BirthdayDbHelper.TABLE_BIRTHDAYS,
            BirthdayDbHelper.COLUMN_ID + " = " + birthday.id, null
        )
    }

    companion object {
        private val LOG_TAG = BirthdayDataSource::class.java.simpleName
    }
}