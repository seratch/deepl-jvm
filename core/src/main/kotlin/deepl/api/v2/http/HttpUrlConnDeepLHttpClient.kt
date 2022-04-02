package deepl.api.v2.http

import deepl.api.v2.logging.DeepLLogger
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

// TODO: proxy support
class HttpUrlConnDeepLHttpClient
@JvmOverloads
constructor(
    private val connectTimeoutMillis: Int = 3_000,
    private val readTimeoutMillis: Int = 30_000,
) : DeepLHttpClient {

  override fun get(
      logger: DeepLLogger,
      url: String,
      query: Map<String, String>,
      headers: Map<String, String>
  ): DeepLHttpResponse {
    val startTimeMillis = System.currentTimeMillis()
    val q = buildQueryString(query)
    val fullUrl = buildFullUrl(url, q)
    val conn = buildConnectionObject(fullUrl, headers)
    try {
      conn.requestMethod = "GET"
      debugLogStart(logger, conn.requestMethod, fullUrl, null)
      connect(conn).use { input ->
        val response =
            DeepLHttpResponse(
                status = conn.responseCode,
                binaryBody = readBody(input),
                headers = conn.headerFields)
        debugLogSuccess(logger, startTimeMillis, response)
        return response
      }
    } finally {
      disconnect(conn, logger)
    }
  }

  override fun postTextBody(
      logger: DeepLLogger,
      url: String,
      query: Map<String, String>,
      body: String,
      headers: Map<String, String>
  ): DeepLHttpResponse {
    val startTimeMillis = System.currentTimeMillis()
    val q = buildQueryString(query)
    val fullUrl = buildFullUrl(url, q)
    val conn = buildConnectionObject(fullUrl, headers)
    try {
      conn.requestMethod = "POST"
      debugLogStart(logger, conn.requestMethod, fullUrl, body)
      setRequestBody(conn, body)
      connect(conn).use { input ->
        val response =
            DeepLHttpResponse(
                status = conn.responseCode,
                binaryBody = readBody(input),
                headers = conn.headerFields)
        debugLogSuccess(logger, startTimeMillis, response)
        return response
      }
    } finally {
      disconnect(conn, logger)
    }
  }

  private val crlf = "\r\n"

  override fun uploadDocument(
      logger: DeepLLogger,
      url: String,
      query: Map<String, String>,
      file: ByteArray,
      filename: String,
      params: Map<String, String>,
      headers: Map<String, String>
  ): DeepLHttpResponse {
    val startTimeMillis = System.currentTimeMillis()
    val q = buildQueryString(query)
    val fullUrl = buildFullUrl(url, q)
    val conn = buildConnectionObject(fullUrl, headers)
    try {
      conn.requestMethod = "POST"
      val printableBody =
          params.map { "${it.key}: ${it.value}" }.joinToString(", ") + ", and the uploaded document"
      debugLogStart(logger, conn.requestMethod, fullUrl, printableBody)
      conn.doOutput = true
      val boundary = "Boundary_" + System.currentTimeMillis()
      conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=${boundary}")
      conn.outputStream?.use { out ->
        params.forEach {
          val item =
              """--${boundary}${crlf}Content-Disposition: form-data; """ +
                  """name="${it.key}"$crlf$crlf""" +
                  it.value +
                  crlf
          out.write(item.encodeToByteArray())
        }
        val fileHeader =
            """--${boundary}${crlf}Content-Disposition: form-data; """ +
                """name="file"; filename="$filename"$crlf$crlf"""
        logger.debug(fileHeader)
        out.write(fileHeader.encodeToByteArray())
        out.write(file)
        val footer = """${crlf}--${boundary}--${crlf}"""
        out.write(footer.encodeToByteArray())
      }

      connect(conn).use { input ->
        val response =
            DeepLHttpResponse(
                status = conn.responseCode,
                binaryBody = readBody(input),
                headers = conn.headerFields)
        debugLogSuccess(logger, startTimeMillis, response)
        return response
      }
    } finally {
      disconnect(conn, logger)
    }
  }

  override fun delete(
      logger: DeepLLogger,
      url: String,
      query: Map<String, String>,
      headers: Map<String, String>
  ): DeepLHttpResponse {
    val startTimeMillis = System.currentTimeMillis()
    val q = buildQueryString(query)
    val fullUrl = buildFullUrl(url, q)
    val conn = buildConnectionObject(fullUrl, headers)
    try {
      conn.requestMethod = "DELETE"
      debugLogStart(logger, conn.requestMethod, fullUrl, null)
      connect(conn).use { input ->
        val response =
            DeepLHttpResponse(
                status = conn.responseCode,
                binaryBody = readBody(input),
                headers = conn.headerFields)
        debugLogSuccess(logger, startTimeMillis, response)
        return response
      }
    } finally {
      disconnect(conn, logger)
    }
  }

  // -----------------------------------------------

  private fun buildConnectionObject(
      fullUrl: String,
      headers: Map<String, String>
  ): HttpURLConnection {
    val conn = URL(fullUrl).openConnection() as HttpURLConnection
    conn.setRequestProperty("Connection", "close")
    conn.connectTimeout = connectTimeoutMillis
    conn.readTimeout = readTimeoutMillis
    headers.forEach { (name, value) -> conn.setRequestProperty(name, value) }
    return conn
  }

  private fun setRequestBody(conn: HttpURLConnection, body: String) {
    conn.doOutput = true
    conn.outputStream.use { out -> out.write(body.toByteArray(Charsets.UTF_8)) }
  }

  private fun connect(conn: HttpURLConnection): InputStream? =
      try {
        conn.connect()
        conn.inputStream
      } catch (e: IOException) {
        conn.errorStream // can be null
      }

  private fun readBody(input: InputStream?): ByteArray {
    return input?.readBytes() ?: byteArrayOf()
  }

  private fun disconnect(conn: HttpURLConnection, logger: DeepLLogger) {
    try {
      conn.disconnect()
    } catch (e: Exception) {
      warnLogFailure(logger, e)
    }
  }
}
