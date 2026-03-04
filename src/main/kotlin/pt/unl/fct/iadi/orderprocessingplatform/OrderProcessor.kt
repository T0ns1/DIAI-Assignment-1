package pt.unl.fct.iadi.orderprocessingplatform

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.orderprocessingplatform.payment.PaymentGateway
import pt.unl.fct.iadi.orderprocessingplatform.pricing.PriceCalculator
import pt.unl.fct.iadi.orderprocessingplatform.domain.Order
import pt.unl.fct.iadi.orderprocessingplatform.domain.OrderItem
import pt.unl.fct.iadi.orderprocessingplatform.domain.PaymentRequest
import pt.unl.fct.iadi.orderprocessingplatform.domain.Receipt
import java.time.Instant
import java.util.Locale

@Component
class OrderProcessor (
    private val priceCalculator: PriceCalculator,
    private val paymentGateway: PaymentGateway
) : CommandLineRunner {

    fun processOrder(order: Order): List<String> {
        val lines = mutableListOf<String>()

        val totalAmount: Double = priceCalculator.calculateTotalPrice(order)

        val paymentRequest = PaymentRequest(
            orderId = order.id,
            amount = totalAmount
        )

        val receipt: Receipt = paymentGateway.processPayment(paymentRequest)

        val calculatorName = priceCalculator::class.simpleName

        val gatewayId = receipt.metadata["gateway"]?.toString() ?: "unknown"
        val transactionId = receipt.metadata["transactionId"]?.toString()
        val reason = receipt.metadata["reason"]?.toString()

        // Output formatting helper for money
        fun money(value: Double): String =
            String.format(Locale.US, "$%.2f", value)

        lines += "Order ID: ${order.id}"
        lines += "User ID: ${order.userId}"
        lines += "Created at: ${order.createdAt}"
        lines += ""
        lines += "Items:"

        order.items.forEach { item ->
            val itemTotal = item.price * item.quantity
            lines += "  - ${item.productId}: ${item.quantity} x ${money(item.price)} = ${money(itemTotal)}"
        }

        lines += ""
        lines += "Total Price: ${money(totalAmount)}"
        lines += "Calculator Used: $calculatorName"
        lines += ""
        lines += "Payment Status: ${receipt.status}"
        lines += "Payment Gateway: $gatewayId"

        if (transactionId != null) {
            lines += "Transaction ID: $transactionId"
        }
        if (reason != null) {
            lines += "Reason: $reason"
        }

        lines += ""
        lines += "=== Processing Complete ==="

        return lines
    }

    override fun run(vararg args: String?) {
        val sampleOrder = Order(
            id = "ORD-2026-001",
            userId = "user123",
            createdAt = Instant.now(),
            items = listOf(
                OrderItem(productId = "LAPTOP-001", quantity = 2, price = 999.99),
                OrderItem(productId = "MOUSE-042", quantity = 3, price = 29.99),
                OrderItem(productId = "KEYBOARD-123", quantity = 1, price = 149.99)
            )
        )

        val outputLines = processOrder(sampleOrder)
        outputLines.forEach { println(it) }
    }
}