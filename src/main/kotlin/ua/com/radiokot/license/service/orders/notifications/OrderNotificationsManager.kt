package ua.com.radiokot.license.service.orders.notifications

import ua.com.radiokot.license.service.orders.Order

interface OrderNotificationsManager {
    fun notifyBuyerOfOrder(order: Order)
}
