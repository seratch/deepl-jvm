package deepl.api.v2.exception

import deepl.api.v2.http.DeepLHttpResponse

class DeepLAPIError
@JvmOverloads
constructor(val httpResponse: DeepLHttpResponse, override val cause: Throwable? = null) :
    RuntimeException(buildMessage(httpResponse), cause) {

  companion object {
    private fun buildMessage(httpResponse: DeepLHttpResponse) =
        "Got an error from DeepL (status: ${httpResponse.status}, body: ${httpResponse.textBody()})"
  }
}
