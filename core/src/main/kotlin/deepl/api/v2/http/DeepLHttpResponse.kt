package deepl.api.v2.http

import java.io.ByteArrayInputStream

open class DeepLHttpResponse
constructor(
    val status: Int,
    val binaryBody: ByteArray,
    val headers: Map<String, List<String>>,
) {
  private var _textBody: String = ""

  fun textBody(): String {
    synchronized(_textBody) {
      if (_textBody.isEmpty()) {
        _textBody = loadTextBody()
      }
    }
    return _textBody
  }

  private val textContentTypes =
      listOf(
          "application/json",
          "text/tab-separated-values",
      )

  private fun loadTextBody(): String {
    val contentType =
        if (headers["Content-Type"]?.isNotEmpty() == true) headers["Content-Type"]?.get(0) else ""
    if (contentType != null && textContentTypes.any { contentType.startsWith(it) }) {
      try {
        ByteArrayInputStream(binaryBody).use { ins ->
          return ins.bufferedReader(Charsets.UTF_8).readText()
        }
      } catch (e: Exception) {
        return " "
      }
    }
    return " "
  }
}
