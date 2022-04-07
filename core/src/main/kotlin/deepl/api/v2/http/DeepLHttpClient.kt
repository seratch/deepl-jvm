package deepl.api.v2.http

import deepl.api.misc.InternalUtility.urlEncode
import deepl.api.v2.logging.DeepLLogger
import java.io.Closeable

interface DeepLHttpClient : AutoCloseable, Closeable {

  fun get(
      logger: DeepLLogger,
      url: String,
      query: Map<String, String> = emptyMap(),
      headers: Map<String, String>,
  ): DeepLHttpResponse

  fun postTextBody(
      logger: DeepLLogger,
      url: String,
      query: Map<String, String> = emptyMap(),
      body: String,
      headers: Map<String, String>
  ): DeepLHttpResponse

  fun uploadDocument(
      logger: DeepLLogger,
      url: String,
      query: Map<String, String> = emptyMap(),
      file: ByteArray,
      filename: String,
      params: Map<String, String>,
      headers: Map<String, String>
  ): DeepLHttpResponse

  fun delete(
      logger: DeepLLogger,
      url: String,
      query: Map<String, String> = emptyMap(),
      headers: Map<String, String>,
  ): DeepLHttpResponse

  override fun close() {}

  // ------------------------------

  fun buildQueryString(query: Map<String, String>) =
      query
          .map { "${urlEncode(it.key)}=${urlEncode(it.value)}" }
          .joinToString(prefix = "?", separator = "&")

  fun buildFullUrl(url: String, q: String) = url + if (q != "?") q else ""

  fun debugLogStart(
      logger: DeepLLogger,
      method: String,
      fullUrl: String,
      body: String?,
  ) {
    if (logger.isDebugEnabled()) {
      val b = if (body == null || body.isEmpty()) "" else "body   $body\n"
      logger.debug("""Sending a request:
$method $fullUrl
$b
""".trimIndent().trimMargin())
    }
  }

  fun debugLogSuccess(logger: DeepLLogger, startTimeMillis: Long, response: DeepLHttpResponse) {
    if (logger.isDebugEnabled()) {
      val responseTimeMillis = System.currentTimeMillis() - startTimeMillis
      logger.debug(
          """Received a response ($responseTimeMillis millis):
status  ${response.status}
body    ${response.textBody()}
"""
              .trimIndent()
              .trimMargin())
    }
  }

  fun warnLogFailure(logger: DeepLLogger, e: Exception) {
    logger.warn("Failed to disconnect from Notion: ${e.message}", e)
  }
}
