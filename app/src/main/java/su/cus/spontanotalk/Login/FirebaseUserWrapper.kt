package su.cus.spontanotalk.Login

import com.google.firebase.auth.FirebaseUser

/**
 * Wrapper around FirebaseUser to implement the IUser interface.
 */
class FirebaseUserWrapper(
    private val firebaseUser: FirebaseUser // Made private as it's an impl detail now
): IUser {
    override val uid: String get() = firebaseUser.uid
    override val email: String? get() = firebaseUser.email
    // If IUser had displayName, it could be:
    // override val displayName: String? get() = firebaseUser.displayName
}
