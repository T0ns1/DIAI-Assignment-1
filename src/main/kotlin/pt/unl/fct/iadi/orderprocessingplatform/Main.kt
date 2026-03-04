package pt.unl.fct.iadi.orderprocessingplatform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderProcessingPlatformApplication

fun main(args: Array<String>) {
    runApplication<OrderProcessingPlatformApplication>(*args)
}