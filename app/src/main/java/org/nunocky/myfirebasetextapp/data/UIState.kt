package org.nunocky.myfirebasetextapp.data

/**
 * A generic UI state class to represent the state of a UI operation.
 */
sealed class UIState<out T> {
    data object Initial : UIState<Nothing>()
    data object Processing : UIState<Nothing>()
    data object Cancelled : UIState<Nothing>()
    class Success<T>(val data: T) : UIState<T>()
    class Failed(val e: Exception) : UIState<Nothing>()
}
