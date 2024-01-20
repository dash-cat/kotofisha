package su.cus.spontanotalk.Login

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Executor


class FireBaseAuthService(
    private val auth: FirebaseAuth,
    private val executor: Executor?
): IAuthService {

    fun checkAuth() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    override suspend fun signUp(email: String, password: String): IUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        Log.d(TAG, "createUserWithEmail:success")
        return result.user?.let { firebaseUser ->
            FirebaseUserWrapper(firebaseUser)
        } ?: throw Exception("Sign up failed")
    }

    private fun reload() {
    }

    companion object {
        private const val TAG = "FireBaseAuthService"
    }
}
