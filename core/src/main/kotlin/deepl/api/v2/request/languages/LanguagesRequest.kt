package deepl.api.v2.request.languages

import deepl.api.v2.model.languages.LanguageType

open class LanguagesRequest
@JvmOverloads
constructor(
    /**
     * Sets whether source or target languages should be listed. Possible options are:
     * - "source" (default) - For languages that can be used in the source_lang parameter of
     * /translate requests.
     * - "target" - For languages that can be used in the target_lang parameter of /translate
     * requests.
     */
    val type: LanguageType? = null,
) {
  fun toRequestBody(): String {
    val body = StringBuilder()
    if (type != null) {
      body.append("&type=${type}")
    }
    return body.toString().replaceFirst("^&", "")
  }
}
