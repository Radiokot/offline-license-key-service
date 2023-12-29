package ua.com.radiokot.license.service.extension

import org.koin.core.scope.Scope

fun Scope.getNotEmptyProperty(key: String) =
    getProperty<String>(key)
        .also { check(it.isNotEmpty()) { "Property '$key' must not be empty" } }