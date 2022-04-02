package deepl.api.v2.endpoint

import deepl.api.v2.exception.DeepLAPIError
import deepl.api.v2.http.DeepLHttpClient
import deepl.api.v2.json.DeepLJsonSerializer
import deepl.api.v2.logging.DeepLLogger
import deepl.api.v2.model.languages.LanguageType
import deepl.api.v2.request.languages.LanguagesRequest
import deepl.api.v2.response.languages.LanguagesResponse

interface Languages : Endpoint {
  val httpClient: DeepLHttpClient
  val jsonSerializer: DeepLJsonSerializer
  val logger: DeepLLogger
  val baseUrl: String

  fun listLanguages(): LanguagesResponse {
    return listLanguages(LanguagesRequest())
  }

  fun listLanguages(type: LanguageType): LanguagesResponse {
    return listLanguages(LanguagesRequest(type = type))
  }

  fun listLanguages(req: LanguagesRequest): LanguagesResponse {
    val httpResponse =
        this.httpClient.postTextBody(
            logger = this.logger,
            url = "${baseUrl}/languages",
            body = req.toRequestBody(),
            headers = buildRequestHeaders(contentTypeForm()),
        )
    if (httpResponse.status == 200) {
      return jsonSerializer.toLanguagesResponse("""{"languages": ${httpResponse.textBody()}}""")
    } else {
      throw DeepLAPIError(httpResponse = httpResponse)
    }
  }
}
