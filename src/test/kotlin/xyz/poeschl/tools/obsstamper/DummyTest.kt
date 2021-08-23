package xyz.poeschl.tools.obsstamper

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class DummyTest {
    @Test
    internal fun imACat() {
        //WHEN
        val text = "I'm a"

        //THEN
        val result = "$text cat"

        //VERIFY
        assertThat(result).isEqualTo("I'm a cat")
    }
}
