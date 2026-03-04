package pt.unl.fct.iadi.orderprocessingplatform.pricing

import pt.unl.fct.iadi.orderprocessingplatform.domain.Order
import org.springframework.stereotype.Component
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@Component
@ConditionalOnProperty(
    prefix = "pricing.promo",
    name = ["enabled"],
    havingValue = "true"
)
object PromoPriceCalculator: PriceCalculator {

    override fun calculateTotalPrice(order: Order): Double {
        
        return order.items.sumOf { item ->
            if (item.quantity <= 5) item.price * item.quantity
            else item.price * item.quantity * 0.8
        }
    }
}