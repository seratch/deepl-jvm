package deepl.api.v2.logging

import java.util.logging.Level
import java.util.logging.Logger

class JavaUtilDeepLLogger constructor(val logger: Logger) : DeepLLogger {

  constructor() : this(Logger.getLogger(JavaUtilDeepLLogger::class.java.canonicalName)) {
    if (this.logger.level == null) {
      this.logger.level = Level.ALL
    }
  }

  override fun isDebugEnabled(): Boolean = logger.isLoggable(Level.FINE)

  override fun debug(message: String, e: Throwable?) {
    logger.log(Level.FINE, message, e)
  }

  override fun info(message: String, e: Throwable?) {
    logger.log(Level.INFO, message, e)
  }

  override fun warn(message: String, e: Throwable?) {
    logger.log(Level.WARNING, message, e)
  }

  override fun error(message: String, e: Throwable?) {
    logger.log(Level.SEVERE, message, e)
  }
}
