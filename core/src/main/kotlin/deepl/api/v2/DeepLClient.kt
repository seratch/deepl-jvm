package deepl.api.v2

import deepl.api.v2.endpoint.*
import deepl.api.v2.http.DeepLHttpClient
import deepl.api.v2.http.HttpUrlConnDeepLHttpClient
import deepl.api.v2.json.DeepLJsonSerializer
import deepl.api.v2.json.GsonDeepLJsonSerializer
import deepl.api.v2.logging.DeepLLogger
import deepl.api.v2.logging.StdoutDeepLLogger
import java.io.Closeable

class DeepLClient
@JvmOverloads
constructor(
    override var token: String,
    override var httpClient: DeepLHttpClient = defaultHttpClient,
    override var logger: DeepLLogger = defaultLogger,
    override var jsonSerializer: DeepLJsonSerializer = defaultJsonSerializer,
    override var baseUrl: String = getApiBaseUrl(token),
) : AutoCloseable, Closeable, TextTranslation, DocumentTranslation, Usage, Glossaries, Languages {

  companion object {
    @JvmStatic val freeApiBaseUrl: String = "https://api-free.deepl.com/v2"
    @JvmStatic val apiBaseUrl: String = "https://api.deepl.com/v2"
    @JvmStatic val defaultHttpClient: DeepLHttpClient = HttpUrlConnDeepLHttpClient()
    @JvmStatic val defaultLogger: DeepLLogger = StdoutDeepLLogger()
    @JvmStatic val defaultJsonSerializer: DeepLJsonSerializer = GsonDeepLJsonSerializer()

    fun getApiBaseUrl(token: String): String {
      return if (token.endsWith(":fx")) freeApiBaseUrl else apiBaseUrl
    }
  }

  override fun close() {
    httpClient.close()
  }
}
