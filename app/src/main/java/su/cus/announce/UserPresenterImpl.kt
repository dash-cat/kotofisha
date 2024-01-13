package su.cus.announce

interface UserPresenter {
    fun sayHello(name : String) : String
}
class UserPresenterImpl(private val repository: UserRepository): UserPresenter {

    override fun sayHello(name : String) : String{
        val foundUser = repository.findUser(name)
        return foundUser?.let { "Hello '$it' from $this" } ?: "User '$name' not found!"
    }
}

