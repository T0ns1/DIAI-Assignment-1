package pt.unl.fct.iadi.orderprocessingplatform.pricing

import pt.unl.fct.iadi.orderprocessingplatform.domain.Order
import org.springframework.stereotype.Component
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@Component
@ConditionalOnProperty(
    prefix = "pricing.promo",
    name = ["enabled"],
    havingValue = "false",
    matchIfMissing = true
)
object BasicPriceCalculator: PriceCalculator {

    override fun calculateTotalPrice(order: Order): Double {
        
        return order.items.sumOf { item -> item.price * item.quantity }
    }
}