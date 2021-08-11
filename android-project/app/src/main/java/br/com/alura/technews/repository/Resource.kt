package br.com.alura.technews.repository

import br.com.alura.technews.model.News

class Resource<T>(
    val value: T?,
    val error: String? = null
)

//fun <T> handleFailure(
//    currentValue: Resource<T?>?,
//    error: String?
//): Resource<T?> {
//    return if (currentValue != null) {
//        Resource(value = currentValue.value, error = error)
//    } else {
//        Resource(value = null, error = error)
//    }
//}