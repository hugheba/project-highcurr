# project-highcurr

Vertx based high-concurrency websocket application with reloadable Javascript plugins.

The premise is keep the server running and externalize business logic to easy to alter Javascript modules.

## Running

To start the server, run the following command:

    ./gradlew bootRun
    
Then open the URL (http://localhost:8087/static/index.html) in a websocket supported browser:

    http://localhost:8087/static/index.html

## Plugins
    
The chat consists of event-hooked Javascript plugins that are loaded, compiled and cached in the Java.

To create a plugin, create a javascript file in the [:highcurr-plugins/src/chat](highcurr-plugins/src/chat) folder.

Add a function to your plugin named with the event hook it should be called for: i.e. onBeforePublishClient.

The function should accept and return the same object, 
an [EventClass](highcurr-ws/src/main/kotlin/highcurr/highcurrws/vertx/verticle/wsserver/EventClass.kt)

    {"body": ""}

You can reload plugins without restarting the server with the following URL:

    http://localhost:8087/reloadPlugins