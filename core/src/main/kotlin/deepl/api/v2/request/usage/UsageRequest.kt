package deepl.api.v2.request.usage

open class UsageRequest constructor() {
  fun toRequestBody(): String {
    val body = StringBuilder()
    return body.toString().replaceFirst("^&", "")
  }
}
