package deepl.api.v2.endpoint

import deepl.api.v2.exception.DeepLAPIError
import deepl.api.v2.http.DeepLHttpClient
import deepl.api.v2.json.DeepLJsonSerializer
import deepl.api.v2.logging.DeepLLogger
import deepl.api.v2.model.translation.Formality
import deepl.api.v2.model.translation.SourceLanguage
import deepl.api.v2.model.translation.TargetLanguage
import deepl.api.v2.model.translation.text.OutlineDetection
import deepl.api.v2.model.translation.text.PreserveFormatting
import deepl.api.v2.model.translation.text.SplitSentences
import deepl.api.v2.model.translation.text.TagHandling
import deepl.api.v2.request.translation.text.TextTranslationRequest
import deepl.api.v2.response.translation.text.TextTranslationResponse

interface TextTranslation : Endpoint {
  val httpClient: DeepLHttpClient
  val jsonSerializer: DeepLJsonSerializer
  val logger: DeepLLogger
  val baseUrl: String

  fun translate(
      text: String,
      targetLang: TargetLanguage,
      sourceLang: SourceLanguage? = null,
      splitSentences: SplitSentences? = null,
      preserveFormatting: PreserveFormatting? = null,
      formality: Formality? = null,
      glossaryId: String? = null,
      tagHandling: TagHandling? = null,
      nonSplittingTags: List<String>? = null,
      outlineDetection: OutlineDetection? = null,
      splittingTags: List<String>? = null,
      ignoreTags: List<String>? = null,
  ): TextTranslationResponse {
    return translate(
        TextTranslationRequest(
            text = text,
            targetLang = targetLang,
            sourceLang = sourceLang,
            splitSentences = splitSentences,
            preserveFormatting = preserveFormatting,
            formality = formality,
            glossaryId = glossaryId,
            tagHandling = tagHandling,
            nonSplittingTags = nonSplittingTags,
            outlineDetection = outlineDetection,
            splittingTags = splittingTags,
            ignoreTags = ignoreTags,
        ))
  }

  fun translate(
      text: List<String>,
      targetLang: TargetLanguage,
      sourceLang: SourceLanguage? = null,
      splitSentences: SplitSentences? = null,
      preserveFormatting: PreserveFormatting? = null,
      formality: Formality? = null,
      glossaryId: String? = null,
      tagHandling: TagHandling? = null,
      nonSplittingTags: List<String>? = null,
      outlineDetection: OutlineDetection? = null,
      splittingTags: List<String>? = null,
      ignoreTags: List<String>? = null,
  ): TextTranslationResponse {
    return translate(
        TextTranslationRequest(
            text = text,
            targetLang = targetLang,
            sourceLang = sourceLang,
            splitSentences = splitSentences,
            preserveFormatting = preserveFormatting,
            formality = formality,
            glossaryId = glossaryId,
            tagHandling = tagHandling,
            nonSplittingTags = nonSplittingTags,
            outlineDetection = outlineDetection,
            splittingTags = splittingTags,
            ignoreTags = ignoreTags,
        ))
  }

  fun translate(req: TextTranslationRequest): TextTranslationResponse {
    val httpResponse =
        this.httpClient.postTextBody(
            logger = this.logger,
            url = "${baseUrl}/translate",
            body = req.toRequestBody(),
            headers = buildRequestHeaders(contentTypeForm()),
        )
    if (httpResponse.status == 200) {
      return jsonSerializer.toTextTranslationResponse(httpResponse.textBody())
    } else {
      throw DeepLAPIError(httpResponse = httpResponse)
    }
  }
}
