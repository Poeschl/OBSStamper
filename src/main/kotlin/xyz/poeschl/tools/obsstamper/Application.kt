package xyz.poeschl.tools.obsstamper

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import mu.KotlinLogging
import net.twasi.obsremotejava.OBSRemoteController
import java.nio.file.Path

fun main(args: Array<String>) {
    ArgParser(args).parseInto(::Args).run {
        ObsStamper(host, Path.of(parentFolder), websocketPassword).start()
    }
}

class ObsStamper(private val host: String, private val parentFolder: Path, private val websocketPassword: String?) {

    companion object {
        val LOGGER = KotlinLogging.logger { }
    }

    fun start() {
        LOGGER.info { "Started ObsStamper" }

        val obsController = when {
            (websocketPassword != null && websocketPassword.isNotBlank()) -> OBSRemoteController(host, false, websocketPassword)
            else -> OBSRemoteController(host, false)
        }

        if (obsController.isFailed) {
            LOGGER.error { "OBS Connection failed" }
        } else {
            // Do stuff via websockets
        }
    }
}

class Args(parser: ArgParser) {
    val host by parser.storing("-host", help = "The host where OBS is running. Defaults to 'ws://localhost:4444'.").default("ws://localhost:4444")
    val parentFolder by parser.storing("--f", "--folder", help = "The folder where the textfiles are generated. Defaults to the current directory").default(".")
    val websocketPassword by parser.storing("--p", "--password", help = "The password for the OBS websocket")
}
