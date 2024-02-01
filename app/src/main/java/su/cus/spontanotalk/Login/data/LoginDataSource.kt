package su.cus.spontanotalk.Login.data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import su.cus.spontanotalk.Login.data.model.LoggedInUser
import java.io.IOException

class LoginDataSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        Firebase.auth
    }

    suspend fun login(email: String, password: String): Result<LoggedInUser> {
        return try {
            val task = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = task.user
            if (user != null) {
                Result.Success(LoggedInUser(user.uid, user.email ?: ""))
            } else {
                Result.Error(IOException("Login failed"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}

