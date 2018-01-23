package highcurr.plugins

import com.coveo.nashorn_modules.FilesystemFolder
import com.coveo.nashorn_modules.Require
import highcurr.highcurrws.vertx.verticle.wsserver.WebsocketVerticle
import jdk.nashorn.api.scripting.NashornScriptEngine
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileReader
import javax.script.Invocable
import javax.script.ScriptEngineManager

@Component
class PluginManager {

    val PLUGINS_PATH: String = System.getenv("HC_PLUGINS_PATH")
    val REQUIRE_PATH: String = System.getenv("HC_PLUGINS_REQUIRE_PATH")
    val plugins: MutableMap<String, Invocable> = mutableMapOf()

    init {
        cachePlugins()
    }

    fun cachePlugins() {
        File(PLUGINS_PATH).walk().forEach {file: File ->
            if (file.extension == "js") {
                val engine: NashornScriptEngine = ScriptEngineManager().getEngineByName("nashorn") as NashornScriptEngine
                var requireFolder: FilesystemFolder = FilesystemFolder.create(File(REQUIRE_PATH), "UTF-8")
                Require.enable(engine, requireFolder)
                engine.eval(FileReader(file))
                plugins.put(file.nameWithoutExtension, engine as Invocable)
            }
        }
    }

    fun runPlugins(event: String, eventClass: WebsocketVerticle.EventClass): WebsocketVerticle.EventClass {
        var eventClass1 = eventClass
        // TODO: Load scripts into memory on start and only call invokeFunction
        for(entry in plugins) {
            try {
                eventClass1 = entry.value.invokeFunction(event, eventClass1) as WebsocketVerticle.EventClass
            } catch(e: Exception) {
                println("Unable to apply ${event} on ${entry.key}: ${e.message}")
            }
        }
        return eventClass1
    }
}