package su.cus.spontanotalk.Login.data

import android.content.ContentValues
import android.content.Context
import su.cus.spontanotalk.Login.data.model.LoggedInUser
// import su.cus.spontanotalk.UserContract // Keep commented if not core to login auth
// import su.cus.spontanotalk.UserDbHelper // Keep commented if not core to login auth

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(private val context: Context, private val dataSource: LoginDataSource) { // Made context private

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // val dbHelper = UserDbHelper(context)
        // val db = dbHelper.readableDatabase
        //
        // val projection = arrayOf(UserContract.UserEntry.COLUMN_NAME_USERNAME, UserContract.UserEntry.COLUMN_NAME_DISPLAY_NAME)
        // val cursor = db.query(
        //     UserContract.UserEntry.TABLE_NAME,
        //     projection,
        //     null,
        //     null,
        //     null,
        //     null,
        //     null
        // )
        //
        // with(cursor) {
        //     if (moveToFirst()) {
        //         val username = getString(getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_USERNAME))
        //         val displayName = getString(getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_DISPLAY_NAME))
        //         user = LoggedInUser(username, displayName)
        //     }
        // }
        // cursor.close()
        // db.close() // Added db.close()
    }

    fun logout() {
        user = null
        dataSource.logout()

        // val dbHelper = UserDbHelper(context)
        // val db = dbHelper.writableDatabase
        // db.delete(UserContract.UserEntry.TABLE_NAME, null, null)
        // db.close() // Added db.close()
    }

    suspend fun login(email: String, password: String): Result<LoggedInUser> { // Changed username to email for clarity
        // Perform login via data source
        val result = dataSource.login(email, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // Typically, you might save the user to SharedPreferences or a local DB here
        // For now, just keeping in memory. DB logic is commented out.

        // val dbHelper = UserDbHelper(context)
        // val db = dbHelper.writableDatabase
        //
        // val values = ContentValues().apply {
        //     put(UserContract.UserEntry.COLUMN_NAME_USERNAME, loggedInUser.userId)
        //     put(UserContract.UserEntry.COLUMN_NAME_DISPLAY_NAME, loggedInUser.displayName)
        // }
        //
        // db.insert(UserContract.UserEntry.TABLE_NAME, null, values)
        // db.close() // Added db.close()
    }
}
