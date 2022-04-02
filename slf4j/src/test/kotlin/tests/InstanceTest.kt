package tests

import deepl.api.v2.logging.Slf4jDeepLLogger
import kotlin.test.assertNotNull
import org.junit.Test

class InstanceTest {
  @Test
  fun instantiation() {
    val logger = Slf4jDeepLLogger()
    assertNotNull(logger)
  }
}
