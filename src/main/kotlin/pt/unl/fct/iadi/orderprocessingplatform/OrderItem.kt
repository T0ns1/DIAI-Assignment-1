package pt.unl.fct.iadi.orderprocessingplatform

data class OrderItem (
    val productId: String,
    val quantity: Int,
    val price: Double
)