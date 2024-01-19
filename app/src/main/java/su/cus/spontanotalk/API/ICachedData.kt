package su.cus.spontanotalk.API

interface ICachedData<T> {
    fun read(): T?

    fun write(obj:T)
}
