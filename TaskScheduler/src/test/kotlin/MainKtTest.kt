import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import taskScheduler.returnOne

class MainKtTest {

    @Test
    fun testMain() {
        assertTrue(true);
    }

    @Test
    fun testReturnOne() {
        assertEquals(1, returnOne());
    }
}