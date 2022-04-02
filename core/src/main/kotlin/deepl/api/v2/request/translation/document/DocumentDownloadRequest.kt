package deepl.api.v2.request.translation.document

import deepl.api.v2.misc.InternalUtility.urlEncode

open class DocumentDownloadRequest
constructor(
    val documentId: String,
    private val documentKey: String,
) {

  fun toRequestBody(): String {
    return "document_key=${urlEncode(documentKey)}"
  }
}
