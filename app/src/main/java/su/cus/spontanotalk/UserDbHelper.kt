package su.cus.spontanotalk

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class UserDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Login.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${UserContract.UserEntry.TABLE_NAME} (" +
                    "${UserContract.UserEntry.COLUMN_NAME_USERNAME} TEXT PRIMARY KEY," +
                    "${UserContract.UserEntry.COLUMN_NAME_DISPLAY_NAME} TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${UserContract.UserEntry.TABLE_NAME}"
    }
}

object UserContract {
    object UserEntry : BaseColumns {
        const val TABLE_NAME = "user"
        const val COLUMN_NAME_USERNAME = "username"
        const val COLUMN_NAME_DISPLAY_NAME = "displayName"
    }
}