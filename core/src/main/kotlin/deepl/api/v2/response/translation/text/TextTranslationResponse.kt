package deepl.api.v2.response.translation.text

import deepl.api.v2.model.translation.SourceLanguage

open class TextTranslationResponse
constructor(
    val translations: List<Translation>,
) {
  open class Translation
  constructor(
      val detectedSourceLanguage: SourceLanguage,
      val text: String,
  )
}
