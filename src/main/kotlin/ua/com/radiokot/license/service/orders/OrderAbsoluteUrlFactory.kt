package ua.com.radiokot.license.service.orders

fun interface OrderAbsoluteUrlFactory {
    fun getOrderAbsoluteUrl(orderId: String): String
}
