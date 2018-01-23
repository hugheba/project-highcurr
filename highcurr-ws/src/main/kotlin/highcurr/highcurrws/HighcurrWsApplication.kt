package highcurr.highcurrws

import highcurr.highcurrws.vertx.verticle.wsserver.WebsocketVerticle
import io.vertx.core.Vertx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import javax.annotation.PostConstruct

@SpringBootApplication
@ComponentScan("highcurr")
class HighcurrWsApplication {

    @Autowired lateinit var websocketVerticle: WebsocketVerticle

    @PostConstruct
    fun deployVerticle() {
        val vertx:Vertx = Vertx.vertx()
        vertx.deployVerticle(websocketVerticle)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(HighcurrWsApplication::class.java, *args)
}