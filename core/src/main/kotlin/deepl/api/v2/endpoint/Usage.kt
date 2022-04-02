package deepl.api.v2.endpoint

import deepl.api.v2.exception.DeepLAPIError
import deepl.api.v2.http.DeepLHttpClient
import deepl.api.v2.json.DeepLJsonSerializer
import deepl.api.v2.logging.DeepLLogger
import deepl.api.v2.request.usage.UsageRequest
import deepl.api.v2.response.usage.UsageResponse

interface Usage : Endpoint {
  val httpClient: DeepLHttpClient
  val jsonSerializer: DeepLJsonSerializer
  val logger: DeepLLogger
  val baseUrl: String

  fun getUsage(): UsageResponse {
    return getUsage(UsageRequest())
  }

  fun getUsage(req: UsageRequest): UsageResponse {
    val httpResponse =
        this.httpClient.postTextBody(
            logger = this.logger,
            url = "${baseUrl}/usage",
            body = req.toRequestBody(),
            headers = buildRequestHeaders(contentTypeForm()),
        )
    if (httpResponse.status == 200) {
      return jsonSerializer.toUsageResponse(httpResponse.textBody())
    } else {
      throw DeepLAPIError(httpResponse = httpResponse)
    }
  }
}
