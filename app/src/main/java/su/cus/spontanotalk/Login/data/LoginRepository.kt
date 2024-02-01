//package su.cus.spontanotalk.Login.data
//
//import android.content.ContentValues
//import android.content.Context
//import su.cus.spontanotalk.Login.data.model.LoggedInUser
//import su.cus.spontanotalk.UserContract
//import su.cus.spontanotalk.UserDbHelper
//
///**
// * Class that requests authentication and user information from the remote data source and
// * maintains an in-memory cache of login status and user credentials information.
// */
//
//class LoginRepository(val context: Context, val dataSource: LoginDataSource) {
//
//    var user: LoggedInUser? = null
//        private set
//
//    val isLoggedIn: Boolean
//        get() = user != null
//
//    init {
//        val dbHelper = UserDbHelper(context)
//        val db = dbHelper.readableDatabase
//
//        val projection = arrayOf(UserContract.UserEntry.COLUMN_NAME_USERNAME, UserContract.UserEntry.COLUMN_NAME_DISPLAY_NAME)
//        val cursor = db.query(
//            UserContract.UserEntry.TABLE_NAME,
//            projection,
//            null,
//            null,
//            null,
//            null,
//            null
//        )
//
//        with(cursor) {
//            if (moveToFirst()) {
//                val username = getString(getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_USERNAME))
//                val displayName = getString(getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_DISPLAY_NAME))
//                user = LoggedInUser(username, displayName)
//            }
//        }
//        cursor.close()
//    }
//
//    fun logout() {
//        user = null
//        dataSource.logout()
//
//        val dbHelper = UserDbHelper(context)
//        val db = dbHelper.writableDatabase
//        db.delete(UserContract.UserEntry.TABLE_NAME, null, null)
//    }
//
//    suspend fun login(username: String, password: String): Result<LoggedInUser> {
//        val result = dataSource.login(username, password)
//
//        if (result is Result.Success) {
//            setLoggedInUser(result.data)
//        }
//
//        return result
//    }
//
//    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
//        this.user = loggedInUser
//
//        val dbHelper = UserDbHelper(context)
//        val db = dbHelper.writableDatabase
//
//        val values = ContentValues().apply {
//            put(UserContract.UserEntry.COLUMN_NAME_USERNAME, loggedInUser.userId)
//            put(UserContract.UserEntry.COLUMN_NAME_DISPLAY_NAME, loggedInUser.displayName)
//        }
//
//        db.insert(UserContract.UserEntry.TABLE_NAME, null, values)
//    }
//}
