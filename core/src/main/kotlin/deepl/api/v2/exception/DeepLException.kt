package deepl.api.v2.exception

import deepl.api.v2.http.DeepLHttpResponse
import deepl.api.v2.json.DeepLJsonSerializer
import deepl.api.v2.model.common.DeepLError

class DeepLException
@JvmOverloads
constructor(
    val httpResponse: DeepLHttpResponse,
    private val jsonSerializer: DeepLJsonSerializer? = null,
    val error: DeepLError? = toError(jsonSerializer, httpResponse.textBody()),
    override val cause: Throwable? = null
) : RuntimeException(buildMessage(httpResponse), cause) {

  companion object {
    private fun buildMessage(httpResponse: DeepLHttpResponse) =
        "Got an error from DeepL (status: ${httpResponse.status}, body: ${httpResponse.textBody()})"

    private fun toError(jsonSerializer: DeepLJsonSerializer?, body: String): DeepLError? {
      if (jsonSerializer != null) {
        if (body != null && body.isNotEmpty() && body.startsWith("{")) {
          return jsonSerializer.toError(body)
        }
      }
      return null
    }
  }
}
