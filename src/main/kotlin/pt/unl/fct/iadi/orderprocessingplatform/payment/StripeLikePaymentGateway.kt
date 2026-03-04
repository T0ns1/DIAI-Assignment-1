package pt.unl.fct.iadi.orderprocessingplatform.payment

import pt.unl.fct.iadi.orderprocessingplatform.domain.PaymentRequest
import pt.unl.fct.iadi.orderprocessingplatform.domain.Receipt
import pt.unl.fct.iadi.orderprocessingplatform.domain.ReceiptStatus
import java.util.UUID
import org.springframework.stereotype.Component
import org.springframework.context.annotation.Profile

@Component
@Profile("prod")
object StripeLikePaymentGateway: PaymentGateway {

    override fun processPayment(paymentRequest: PaymentRequest): Receipt {
        
        val amount = paymentRequest.amount
        
        val (status, metadata) = when {
            amount <= 0 -> Pair(
                ReceiptStatus.REJECTED,
                mapOf(
                    "gateway" to "stripe-like",
                    "reason" to "Invalid amount",
                    "amount" to amount
                )
            )

            amount > 10000 -> Pair(
                ReceiptStatus.FLAGGED_FOR_REVIEW,
                mapOf(
                    "gateway" to "stripe-like",
                    "reason" to "High value transaction requires review",
                    "amount" to amount
                )
            )

            else -> Pair(
                ReceiptStatus.PAID,
                mapOf(
                    "gateway" to "stripe-like",
                    "transactionId" to UUID.randomUUID(),
                    "amount" to amount
                )
            )
        }

        return Receipt(
            orderId = paymentRequest.orderId,
            status = status,
            metadata = metadata
        )
    }
}