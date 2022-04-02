package deepl.api.v2.endpoint

import deepl.api.v2.http.UserAgent

interface Endpoint {
  val token: String

  fun buildRequestHeaders(additionalOnes: Map<String, String> = emptyMap()): Map<String, String> {
    return mapOf(
            "Authorization" to "DeepL-Auth-Key $token", "User-Agent" to UserAgent.buildUserAgent())
        .plus(additionalOnes)
  }

  fun contentTypeForm(): Map<String, String> =
      mapOf("Content-Type" to "application/x-www-form-urlencoded")

  fun contentTypeJson(): Map<String, String> =
      mapOf("Content-Type" to "application/json; charset=utf-8")

  fun contentTypeMultipart(): Map<String, String> =
      mapOf("Content-Type" to "application/x-www-form-urlencoded")
}
