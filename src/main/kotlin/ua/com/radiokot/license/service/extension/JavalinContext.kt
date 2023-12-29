package ua.com.radiokot.license.service.extension

import io.javalin.http.Context
import ua.com.radiokot.license.service.jsonapi.JSON_API_CONTENT_TYPE

fun Context.jsonApi(response: ByteArray) =
    contentType(JSON_API_CONTENT_TYPE).result(response)