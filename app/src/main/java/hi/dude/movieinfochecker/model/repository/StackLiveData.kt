package hi.dude.movieinfochecker.model.repository

import androidx.lifecycle.LiveData
import java.util.*

class StackLiveData<T> : LiveData<Stack<T>>() {

    init {
        value = Stack<T>()
    }

    fun push(item: T?) {
        value?.push(item)
        value = value
    }

    fun pop(): T? {
        val item = try {
            value?.pop()
        } catch (e: EmptyStackException) {
            null
        }
        value = value
        return item
    }

    fun peek(): T? {
        return try {
            value?.peek()
        } catch (e: EmptyStackException) {
            null
        }
    }

    fun clear() {
        value = Stack<T>()
    }

    fun isEmpty(): Boolean {
        return value?.empty() ?: true
    }
}
