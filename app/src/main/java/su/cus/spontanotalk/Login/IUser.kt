package su.cus.spontanotalk.Login

/**
 * Interface representing a user.
 */
interface IUser {
    val uid: String
    val email: String?
    // Potentially a displayName if common across auth user types
}
