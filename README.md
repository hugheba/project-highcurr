# project-highcurr

Vertx based high-concurrency websocket application with reloadable Javascript plugins.

## Running

To start the server, run the following command:

    ./gradlew bootRun
    
Then open the URL (http://localhost:8087/static/index.html) in a websocket supported browser:

    http://localhost:8087/static/index.html

## Plugins
    
You can reload plugins with the following URL:

    http://localhost:8087/reloadPlugins