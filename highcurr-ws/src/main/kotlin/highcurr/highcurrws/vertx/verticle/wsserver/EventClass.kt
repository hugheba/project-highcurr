package highcurr.highcurrws.vertx.verticle.wsserver

data class EventClass(var body: String, var services: MutableMap<String, Any> = mutableMapOf())
