# project-highcurr

Vertx based high-concurrency websocket application with reloadable Javascript plugins for chat alteration.

The premise is keep the server running and externalize business logic to easy to alter Javascript modules.

## Running

To start the server, run the following command:

    ./gradlew bootRun
    
Then open the URL [http://localhost:8087/static/index.html](http://localhost:8087/static/index.html) in a websocket supported browser:

## Plugins
    
The chat consists of event-hooked Javascript plugins that are loaded, compiled and cached in the Java Vertx server.

On each event, the plugins are run in order and the corresponding hook is applied against the payload.

### Plugin Development

To create a plugin, create a javascript file in the [:highcurr-plugins/src/chat](highcurr-plugins/src/chat) folder.

Add a function to your plugin named with the event hook it should be called for: i.e. onBeforePublishClient.

The plugins can run generic Javascript libraries loaded with NPM like Lodash, and even call Java services like Spring Beans.

The function should accept and return the same object, 
an [EventClass](highcurr-ws/src/main/kotlin/highcurr/highcurrws/vertx/verticle/wsserver/EventClass.kt)

    {
        "msgTextInput": "",
        "msgTextOutput": ""
    }

### Plugin Hotloading

You can reload plugins without restarting the server with the following URL:

    http://localhost:8087/reloadPlugins