package xyz.poeschl.tools.obsstamper

import mu.KotlinLogging

fun main() {
    ObsStamper().start()
}

class ObsStamper {

    companion object {
        val LOGGER = KotlinLogging.logger { }
    }

    fun start() {
        LOGGER.info { "Started ObsStamper" }
    }
}
