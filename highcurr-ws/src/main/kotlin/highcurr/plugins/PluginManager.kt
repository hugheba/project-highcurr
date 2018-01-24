package highcurr.plugins

import com.coveo.nashorn_modules.FilesystemFolder
import com.coveo.nashorn_modules.Require
import highcurr.highcurrws.vertx.verticle.wsserver.EventClass
import highcurr.highcurrws.vertx.verticle.wsserver.TranslateService
import highcurr.highcurrws.vertx.verticle.wsserver.WebsocketVerticle
import jdk.nashorn.api.scripting.NashornScriptEngine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileReader
import javax.annotation.PostConstruct
import javax.script.Invocable
import javax.script.ScriptEngineManager
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Component
class PluginManager {

    @Autowired lateinit var translateService: TranslateService

    val PLUGINS_PATH: String = System.getenv("HC_PLUGINS_PATH")
    val REQUIRE_PATH: String = System.getenv("HC_PLUGINS_REQUIRE_PATH")
    val plugins: MutableMap<String, Invocable> = mutableMapOf()
    val services: MutableMap<String, Any> = mutableMapOf()

    init {
        cachePlugins()
    }

    @PostConstruct
    fun setServices() {
        services.put("translateService", translateService)
    }

    fun cachePlugins() {
        File(PLUGINS_PATH).walk().forEach {file: File ->
            if (file.extension == "js") {
                // Create new NashornScriptEngine
                val engine: NashornScriptEngine = ScriptEngineManager().getEngineByName("nashorn") as NashornScriptEngine
                // Add services to engine
                engine.put("services", services)
                // Add path to node_modules parent folder
                var requireFolder: FilesystemFolder = FilesystemFolder.create(File(REQUIRE_PATH), "UTF-8")
                // Add folder to Require Engine
                Require.enable(engine, requireFolder)
                // Load script
                engine.eval(FileReader(file))
                // Add invokable Engine to cache
                plugins.put(file.nameWithoutExtension, engine as Invocable)
            }
        }
    }

    fun runPlugins(event: String, eventClass: EventClass): EventClass {
        var eventClass1 = eventClass
        // TODO: Load scripts into memory on start and only call invokeFunction
        for(entry in plugins) {
            try {
                eventClass1 = entry.value.invokeFunction(event, eventClass1) as EventClass
            } catch(e: Exception) {
                println("Unable to apply ${event} on ${entry.key}: ${e.message}")
            }
        }
        return eventClass1
    }
}