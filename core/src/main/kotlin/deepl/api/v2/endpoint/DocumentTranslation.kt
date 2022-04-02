package deepl.api.v2.endpoint

import deepl.api.v2.exception.DeepLAPIError
import deepl.api.v2.http.DeepLHttpClient
import deepl.api.v2.json.DeepLJsonSerializer
import deepl.api.v2.logging.DeepLLogger
import deepl.api.v2.misc.InternalUtility.urlEncode
import deepl.api.v2.model.translation.Formality
import deepl.api.v2.model.translation.SourceLanguage
import deepl.api.v2.model.translation.TargetLanguage
import deepl.api.v2.request.translation.document.DocumentDownloadRequest
import deepl.api.v2.request.translation.document.DocumentStatusRequest
import deepl.api.v2.request.translation.document.DocumentUploadRequest
import deepl.api.v2.response.translation.document.DocumentStatusResponse
import deepl.api.v2.response.translation.document.DocumentUploadResponse
import java.io.File

interface DocumentTranslation : Endpoint {
  val httpClient: DeepLHttpClient
  val jsonSerializer: DeepLJsonSerializer
  val logger: DeepLLogger
  val baseUrl: String

  fun uploadDocument(
      file: ByteArray,
      filename: String,
      targetLang: TargetLanguage,
      sourceLang: SourceLanguage? = null,
      formality: Formality? = null,
      glossaryId: String? = null,
  ): DocumentUploadResponse {
    return uploadDocument(
        DocumentUploadRequest(
            file = file,
            filename = filename,
            targetLang = targetLang,
            sourceLang = sourceLang,
            formality = formality,
            glossaryId = glossaryId,
        ))
  }

  fun uploadDocument(
      file: File,
      filename: String,
      targetLang: TargetLanguage,
      sourceLang: SourceLanguage? = null,
      formality: Formality? = null,
      glossaryId: String? = null,
  ): DocumentUploadResponse {
    return uploadDocument(
        DocumentUploadRequest(
            file = file,
            filename = filename,
            targetLang = targetLang,
            sourceLang = sourceLang,
            formality = formality,
            glossaryId = glossaryId,
        ))
  }

  fun uploadDocument(req: DocumentUploadRequest): DocumentUploadResponse {
    val httpResponse =
        this.httpClient.uploadDocument(
            logger = this.logger,
            url = "${baseUrl}/document",
            file = req.file,
            filename = req.filename,
            params = req.params(),
            headers = buildRequestHeaders(),
        )
    if (httpResponse.status == 200) {
      return jsonSerializer.toUploadDocumentResponse(httpResponse.textBody())
    } else {
      throw DeepLAPIError(httpResponse = httpResponse)
    }
  }

  fun getDocumentStatus(
      documentId: String,
      documentKey: String,
  ): DocumentStatusResponse {
    return getDocumentStatus(
        DocumentStatusRequest(
            documentId = documentId,
            documentKey = documentKey,
        ))
  }

  fun getDocumentStatus(req: DocumentStatusRequest): DocumentStatusResponse {
    val httpResponse =
        this.httpClient.postTextBody(
            logger = this.logger,
            url = "${baseUrl}/document/${urlEncode(req.documentId)}",
            body = req.toRequestBody(),
            headers = buildRequestHeaders(contentTypeForm()),
        )
    if (httpResponse.status == 200) {
      return jsonSerializer.toGetDocumentStatusResponse(httpResponse.textBody())
    } else {
      throw DeepLAPIError(httpResponse = httpResponse)
    }
  }

  fun downloadDocument(
      documentId: String,
      documentKey: String,
  ): ByteArray {
    return downloadDocument(
        DocumentDownloadRequest(
            documentId = documentId,
            documentKey = documentKey,
        ))
  }

  fun downloadDocument(req: DocumentDownloadRequest): ByteArray {
    val httpResponse =
        this.httpClient.postTextBody(
            logger = this.logger,
            url = "${baseUrl}/document/${urlEncode(req.documentId)}/result",
            body = req.toRequestBody(),
            headers = buildRequestHeaders(contentTypeForm()),
        )
    if (httpResponse.status == 200) {
      return httpResponse.binaryBody
    } else {
      throw DeepLAPIError(httpResponse = httpResponse)
    }
  }
}
