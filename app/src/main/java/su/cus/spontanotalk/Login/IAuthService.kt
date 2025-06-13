package su.cus.spontanotalk.Login

// FirebaseUser import might not be needed here anymore if IUser doesn't expose it directly
// import com.google.firebase.auth.FirebaseUser

interface IAuthService {
    suspend fun signUp(email: String, password: String): IUser
    fun isSignedIn(): Boolean
    fun signOut()
}

// FirebaseUserWrapper and IUser are now in their own files.