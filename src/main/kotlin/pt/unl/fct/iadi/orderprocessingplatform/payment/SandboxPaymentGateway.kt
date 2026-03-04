package pt.unl.fct.iadi.orderprocessingplatform.payment

import pt.unl.fct.iadi.orderprocessingplatform.domain.PaymentRequest
import pt.unl.fct.iadi.orderprocessingplatform.domain.Receipt
import pt.unl.fct.iadi.orderprocessingplatform.domain.ReceiptStatus
import org.springframework.stereotype.Component
import org.springframework.context.annotation.Profile

@Component
@Profile("!prod")
object SandboxPaymentGateway: PaymentGateway {

    override fun processPayment(paymentRequest: PaymentRequest): Receipt {
        
        val metadata = mapOf(
            "gateway" to "sandbox",
            "amount" to paymentRequest.amount
        )

        return Receipt(
            orderId = paymentRequest.orderId,
            status = ReceiptStatus.PAID,
            metadata = metadata
        )
    }
}