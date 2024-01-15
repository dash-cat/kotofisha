package su.cus.announce.API

interface ICachedData<T> {
    fun read(): T?

    fun write(obj:T)
}
