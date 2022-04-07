package deepl.api.v2.endpoint

import deepl.api.misc.InternalUtility.urlEncode
import deepl.api.v2.exception.DeepLException
import deepl.api.v2.http.DeepLHttpClient
import deepl.api.v2.json.DeepLJsonSerializer
import deepl.api.v2.logging.DeepLLogger
import deepl.api.v2.model.glossaries.EntriesFormat
import deepl.api.v2.model.glossaries.GlossarySourceLanguage
import deepl.api.v2.model.glossaries.GlossaryTargetLanguage
import deepl.api.v2.request.glossaries.*
import deepl.api.v2.response.glossaries.*

interface Glossaries : Endpoint {
  val httpClient: DeepLHttpClient
  val jsonSerializer: DeepLJsonSerializer
  val logger: DeepLLogger
  val baseUrl: String

  fun listGlossaryLanguagePairs(): GlossaryLanguagePairsResponse =
      listGlossaryLanguagePairs(GlossaryLanguagePairsRequest())

  fun listGlossaryLanguagePairs(req: GlossaryLanguagePairsRequest): GlossaryLanguagePairsResponse {
    val httpResponse =
        this.httpClient.get(
            logger = this.logger,
            url = "${baseUrl}/glossary-language-pairs",
            headers = buildRequestHeaders(contentTypeForm()),
        )
    if (httpResponse.status == 200) {
      return jsonSerializer.toGlossaryLanguagePairResponse(httpResponse.textBody())
    } else {
      throw DeepLException(httpResponse = httpResponse, jsonSerializer = jsonSerializer)
    }
  }

  fun listGlossaries(): GlossariesResponse = listGlossaries(GlossariesRequest())

  fun listGlossaries(req: GlossariesRequest): GlossariesResponse {
    val httpResponse =
        this.httpClient.get(
            logger = this.logger,
            url = "${baseUrl}/glossaries",
            headers = buildRequestHeaders(contentTypeForm()),
        )
    if (httpResponse.status == 200) {
      return jsonSerializer.toListGlossariesResponse(httpResponse.textBody())
    } else {
      throw DeepLException(httpResponse = httpResponse, jsonSerializer = jsonSerializer)
    }
  }

  fun getGlossary(glossaryId: String): GlossaryResponse {
    return getGlossary(GlossaryRequest(glossaryId = glossaryId))
  }

  fun getGlossary(req: GlossaryRequest): GlossaryResponse {
    val httpResponse =
        this.httpClient.get(
            logger = this.logger,
            url = "${baseUrl}/glossaries/${urlEncode(req.glossaryId)}",
            headers = buildRequestHeaders(contentTypeForm()),
        )
    if (httpResponse.status == 200) {
      return jsonSerializer.toGetGlossaryResponse("""{"glossary": ${httpResponse.textBody()}}""")
    } else {
      throw DeepLException(httpResponse = httpResponse, jsonSerializer = jsonSerializer)
    }
  }

  fun listGlossaryEntries(glossaryId: String): GlossaryEntriesResponse {
    return listGlossaryEntries(GlossaryEntriesRequest(glossaryId = glossaryId))
  }

  fun listGlossaryEntries(req: GlossaryEntriesRequest): GlossaryEntriesResponse {
    val httpResponse =
        this.httpClient.get(
            logger = this.logger,
            url = "${baseUrl}/glossaries/${urlEncode(req.glossaryId)}/entries",
            headers = buildRequestHeaders(contentTypeForm()),
        )
    if (httpResponse.status == 200) {
      return GlossaryEntriesResponse(
          entries =
              httpResponse.textBody().lines().associate {
                val elements = it.split("\t")
                elements[0] to elements[1]
              })
    } else {
      throw DeepLException(httpResponse = httpResponse, jsonSerializer = jsonSerializer)
    }
  }

  fun createGlossary(
      name: String,
      sourceLang: GlossarySourceLanguage,
      targetLang: GlossaryTargetLanguage,
      entries: Map<String, String>,
      entriesFormat: EntriesFormat = EntriesFormat.TabSeparatedValues
  ): GlossaryCreationResponse {
    return createGlossary(
        GlossaryCreationRequest(
            name = name,
            sourceLang = sourceLang,
            targetLang = targetLang,
            entries = entries,
            entriesFormat = entriesFormat,
        ))
  }

  fun createGlossary(req: GlossaryCreationRequest): GlossaryCreationResponse {
    val httpResponse =
        this.httpClient.postTextBody(
            logger = this.logger,
            url = "${baseUrl}/glossaries",
            body = req.toRequestBody(),
            headers = buildRequestHeaders(contentTypeForm()),
        )
    if (httpResponse.status == 201) {
      return jsonSerializer.toCreateGlossaryResponse("""{"glossary": ${httpResponse.textBody()}}""")
    } else {
      throw DeepLException(httpResponse = httpResponse, jsonSerializer = jsonSerializer)
    }
  }

  fun deleteGlossary(glossaryId: String): GlossaryDeletionResponse {
    return deleteGlossary(GlossaryDeletionRequest(glossaryId = glossaryId))
  }

  fun deleteGlossary(req: GlossaryDeletionRequest): GlossaryDeletionResponse {
    val httpResponse =
        this.httpClient.delete(
            logger = this.logger,
            url = "${baseUrl}/glossaries/${urlEncode(req.glossaryId)}",
            headers = buildRequestHeaders(),
        )
    if (httpResponse.status == 204) {
      return GlossaryDeletionResponse()
    } else {
      throw DeepLException(httpResponse = httpResponse, jsonSerializer = jsonSerializer)
    }
  }
}
