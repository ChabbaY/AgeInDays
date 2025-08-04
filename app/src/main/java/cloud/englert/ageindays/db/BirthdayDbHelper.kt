package cloud.englert.ageindays.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class BirthdayDbHelper (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    init {
        Log.d(LOG_TAG, "created database: $DB_NAME")
    }

    override fun onCreate(db: SQLiteDatabase) {
        try {
            Log.d(LOG_TAG, "creating table with: $SQL_CREATE")
            db.execSQL(SQL_CREATE)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "error creating table", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(LOG_TAG, "deleting old table")
        db.execSQL(SQL_DROP)
        onCreate(db)
    }

    companion object {
        private val LOG_TAG: String = BirthdayDbHelper::class.java.simpleName
        const val DB_NAME: String = "birthdays.db"
        const val DB_VERSION: Int = 1

        const val TABLE_BIRTHDAYS: String = "birthdays"
        const val COLUMN_ID: String = "_id"
        const val COLUMN_DATE: String = "date"
        const val COLUMN_PERSON: String = "person"

        const val SQL_CREATE: String = "CREATE TABLE $TABLE_BIRTHDAYS(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_PERSON + " TEXT NOT NULL);"

        const val SQL_DROP: String = "DROP TABLE IF EXISTS $TABLE_BIRTHDAYS"
    }
}