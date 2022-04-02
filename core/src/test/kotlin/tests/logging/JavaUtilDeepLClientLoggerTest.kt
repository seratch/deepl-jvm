package tests.logging

import deepl.api.v2.logging.JavaUtilDeepLLogger
import kotlin.test.assertTrue
import org.junit.Test

class JavaUtilDeepLClientLoggerTest {

  @Test
  fun test() {
    val logger = JavaUtilDeepLLogger()
    assertTrue(logger.isDebugEnabled())

    logger.debug("foo")
    logger.info("foo")
    logger.warn("foo")
    logger.error("foo")

    logger.debug("foo", RuntimeException("hi"))
    logger.info("foo", RuntimeException("hi"))
    logger.warn("foo", RuntimeException("hi"))
    logger.error("foo", RuntimeException("hi"))
  }
}
