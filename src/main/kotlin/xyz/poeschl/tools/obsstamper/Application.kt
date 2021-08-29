package xyz.poeschl.tools.obsstamper

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import mu.KotlinLogging
import net.twasi.obsremotejava.OBSRemoteController
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.*
import kotlin.system.exitProcess
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaDuration

@ExperimentalPathApi
@ExperimentalTime
fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::Args).run {
        ObsStamper(host, Path(folder), password).start()
    }
}

@ExperimentalPathApi
@ExperimentalTime
class ObsStamper(host: String, parentFolder: Path, websocketPassword: String?) {

    private val obsController = OBSRemoteController(host, false, websocketPassword, false)
    private val timestampFileFolder = parentFolder.createDirectories()

    companion object {
        private val LOGGER = KotlinLogging.logger { }
        private const val TIMECODE_PATTERN = "HH:mm:ss"
        private const val FILE_TIMESTAMP_PATTERN = "yyyy-MM-dd HH-mm-ss"
    }

    fun start() {
        LOGGER.info { "Started OBSStamper" }
        obsController.connect()

        obsController.registerConnectionFailedCallback { msg -> LOGGER.error { "Connection failed: $msg" } }
        obsController.registerOnError { msg, throwable -> LOGGER.error(throwable) { "OBS Error: $msg" } }
        obsController.registerConnectCallback {
            obsController.getStreamingStatus { response ->
                if (!response.isRecording) {
                    LOGGER.info { "OBS is not recording" }
                } else {
                    val timecode = response.recTimecode
                    LOGGER.info { "Current recording timecode: $timecode" }
                    printTimecode("record", timecode)
                }

                if (!response.isStreaming) {
                    LOGGER.info { "OBS is not streaming" }
                } else {
                    val timecode = response.streamTimecode
                    LOGGER.info { "Current streaming timecode: $timecode" }
                    printTimecode("stream", timecode)
                }
                stop()
            }
        }
        obsController.await()
    }

    private fun stop() {
        obsController.disconnect()
        exitProcess(0)
    }

    private fun printTimecode(filesuffix: String, timecode: String) {
        val recordDuration = Duration.milliseconds(SimpleDateFormat(TIMECODE_PATTERN).parse(timecode).time)
        val startDate = LocalDateTime.now().minus(recordDuration.toJavaDuration())

        printToFile("${startDate.format(DateTimeFormatter.ofPattern(FILE_TIMESTAMP_PATTERN))}-timestamps-$filesuffix.txt", timecode)
    }

    private fun printToFile(filename: String, timecode: String) {
        val timecodeFile = Path(timestampFileFolder.pathString, filename)
        if (timecodeFile.notExists()) {
            timecodeFile.createFile()
        }
        timecodeFile.appendLines(timecode.lines())
    }
}

class Args(parser: ArgParser) {
    val host by parser.storing(help = "The host where OBS is running. Defaults to 'ws://localhost:4444'.").default("ws://localhost:4444")
    val folder by parser.storing("-f", help = "The folder where the textfiles are generated. Defaults to the current directory").default(".")
    val password by parser.storing("-p", help = "The password for the OBS websocket").default("")
}
