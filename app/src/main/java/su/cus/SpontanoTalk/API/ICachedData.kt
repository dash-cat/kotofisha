package su.cus.SpontanoTalk.API

interface ICachedData<T> {
    fun read(): T?

    fun write(obj:T)
}
