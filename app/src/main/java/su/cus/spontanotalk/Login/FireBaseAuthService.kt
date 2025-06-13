package su.cus.spontanotalk.Login

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await


class FireBaseAuthService(
    private val auth: FirebaseAuth
    // private val executor: Executor? // Executor was unused
): IAuthService {

    // Removed checkAuth() method as it was unused and not part of IAuthService

    override fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun signUp(email: String, password: String): IUser {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Log.d(TAG, "createUserWithEmail:success")
            return result.user?.let { firebaseUser ->
                FirebaseUserWrapper(firebaseUser)
            } ?: throw AuthSignUpFailedException("Sign up resulted in a null user.")
        } catch (e: FirebaseAuthWeakPasswordException) {
            throw AuthSignUpFailedException("Password is too weak.", e)
        } catch (e: FirebaseAuthUserCollisionException) {
            throw AuthSignUpFailedException("User with this email already exists.", e)
        } catch (e: Exception) {
            // Catching general Exception to wrap it in our custom exception type
            Log.w(TAG, "Unhandled signUp error: ${e.javaClass.name} - ${e.message}")
            throw AuthSignUpFailedException("An unexpected error occurred during sign up: ${e.message}", e)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    companion object {
        private const val TAG = "FireBaseAuthService"
    }
}


