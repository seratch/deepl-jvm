package deepl.api.v2.json

import deepl.api.v2.model.common.DeepLError
import deepl.api.v2.response.glossaries.GlossariesResponse
import deepl.api.v2.response.glossaries.GlossaryCreationResponse
import deepl.api.v2.response.glossaries.GlossaryLanguagePairsResponse
import deepl.api.v2.response.glossaries.GlossaryResponse
import deepl.api.v2.response.languages.LanguagesResponse
import deepl.api.v2.response.translation.document.DocumentStatusResponse
import deepl.api.v2.response.translation.document.DocumentUploadResponse
import deepl.api.v2.response.translation.text.TextTranslationResponse
import deepl.api.v2.response.usage.UsageResponse

interface DeepLJsonSerializer {
  fun toTextTranslationResponse(body: String): TextTranslationResponse
  fun toUsageResponse(body: String): UsageResponse
  fun toLanguagesResponse(body: String): LanguagesResponse
  fun toUploadDocumentResponse(body: String): DocumentUploadResponse
  fun toGetDocumentStatusResponse(body: String): DocumentStatusResponse
  fun toGlossaryLanguagePairResponse(body: String): GlossaryLanguagePairsResponse
  fun toListGlossariesResponse(body: String): GlossariesResponse
  fun toGetGlossaryResponse(body: String): GlossaryResponse
  fun toCreateGlossaryResponse(body: String): GlossaryCreationResponse
  fun toError(body: String): DeepLError
}
