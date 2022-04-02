package deepl.api.v2.response.glossaries

import deepl.api.v2.model.glossaries.GlossarySourceLanguage
import deepl.api.v2.model.glossaries.GlossaryTargetLanguage

open class GlossaryLanguagePairsResponse
constructor(
    val supportedLanguages: List<SupportedLanguagePair>,
) {
  open class SupportedLanguagePair
  constructor(
      val sourceLang: GlossarySourceLanguage,
      val targetLang: GlossaryTargetLanguage,
  )
}
