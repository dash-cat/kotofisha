package su.cus.spontanotalk.Login

import com.google.firebase.auth.FirebaseUser

interface IAuthService {
    suspend fun signUp(email: String, password: String): IUser
    fun isSignedIn(): Boolean
    fun signOut()
}

class FirebaseUserWrapper( // XXX move
    val firebaseUser: FirebaseUser
): IUser {

}

interface IUser { // XXX move

}