package highcurr.highcurrws.vertx.verticle.wsserver

import highcurr.plugins.PluginManager
import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.eventbus.Message
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.sockjs.BridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.kotlin.ext.web.handler.sockjs.PermittedOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class WebsocketVerticle: AbstractVerticle() {

    @Autowired lateinit var pluginManager: PluginManager


    override fun start(startFuture: Future<Void>) {
        val router = createRouter()
        val eb = vertx.eventBus()

        eb.consumer<Any>("chat.to.server").handler({ message: Message<Any> ->
            vertx.executeBlocking({future ->
                var eventClass = EventClass(msgTextInput = message.body() as String)
                eventClass = pluginManager.runPlugins("onBeforePublishClient", eventClass)
                future.complete(eventClass)
            }, { res: AsyncResult<EventClass> ->
                var timestamp = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.MEDIUM)
                        .format(java.util.Date.from(java.time.Instant.now()))
                eb.publish("chat.to.client", "${timestamp}: ${res.result().msgTextOutput}")
            })
        })

        vertx.createHttpServer().requestHandler({router.accept(it)}).listen(8087)
    }

    private fun createRouter() = Router.router(vertx).apply {
        val ebHandler = SockJSHandler.create(vertx).bridge(getBridgeOpts())
        // Websocket Eventbus bridge
        route("/eventbus/*").handler(ebHandler)
        // Reload plugins
        route("/reloadPlugins").handler({ctx: RoutingContext ->
            vertx.executeBlocking(
                    { future: Future<Any> -> pluginManager.cachePlugins(); future.complete()},
                    { res: AsyncResult<Any> -> ctx.response().end("Reloaded!")}
            )
        })
        // Static server
        route("/static/*").handler(StaticHandler.create())
    }

    private fun getBridgeOpts(): BridgeOptions {
        var opts = BridgeOptions().apply {
            // Restrict calls from client to server on 'chat.to.server'
            addInboundPermitted(PermittedOptions(address = "chat.to.server"))
            // Restric calls from server to client on 'chat.to.client'
            addOutboundPermitted(PermittedOptions(address = "chat.to.client"))
        }
        return opts
    }


}