package deepl.api.v2.json

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import deepl.api.Metadata.isLibraryMaintainerMode
import deepl.api.v2.json.gson.GsonUnknownFieldDetection
import deepl.api.v2.response.glossaries.GlossariesResponse
import deepl.api.v2.response.glossaries.GlossaryCreationResponse
import deepl.api.v2.response.glossaries.GlossaryLanguagePairsResponse
import deepl.api.v2.response.glossaries.GlossaryResponse
import deepl.api.v2.response.languages.LanguagesResponse
import deepl.api.v2.response.translation.document.DocumentStatusResponse
import deepl.api.v2.response.translation.document.DocumentUploadResponse
import deepl.api.v2.response.translation.text.TextTranslationResponse
import deepl.api.v2.response.usage.UsageResponse

class GsonDeepLJsonSerializer : DeepLJsonSerializer {
  private val gson: Gson

  constructor(unknownPropertyDetection: Boolean = isLibraryMaintainerMode()) {
    val builder = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)

    gson =
        if (unknownPropertyDetection) {
          builder.registerTypeAdapterFactory(GsonUnknownFieldDetection()).create()
        } else {
          builder.create()
        }
  }

  override fun toTextTranslationResponse(body: String): TextTranslationResponse =
      gson.fromJson(body, TextTranslationResponse::class.java)

  override fun toUsageResponse(body: String): UsageResponse =
      gson.fromJson(body, UsageResponse::class.java)

  override fun toLanguagesResponse(body: String): LanguagesResponse =
      gson.fromJson(body, LanguagesResponse::class.java)

  override fun toUploadDocumentResponse(body: String): DocumentUploadResponse =
      gson.fromJson(body, DocumentUploadResponse::class.java)

  override fun toGetDocumentStatusResponse(body: String): DocumentStatusResponse =
      gson.fromJson(body, DocumentStatusResponse::class.java)

  override fun toGlossaryLanguagePairResponse(body: String): GlossaryLanguagePairsResponse =
      gson.fromJson(body, GlossaryLanguagePairsResponse::class.java)

  override fun toListGlossariesResponse(body: String): GlossariesResponse =
      gson.fromJson(body, GlossariesResponse::class.java)

  override fun toGetGlossaryResponse(body: String): GlossaryResponse =
      gson.fromJson(body, GlossaryResponse::class.java)

  override fun toCreateGlossaryResponse(body: String): GlossaryCreationResponse =
      gson.fromJson(body, GlossaryCreationResponse::class.java)
}
