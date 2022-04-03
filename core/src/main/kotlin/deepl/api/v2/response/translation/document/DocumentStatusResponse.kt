package deepl.api.v2.response.translation.document

import deepl.api.v2.model.translation.document.DocumentStatus

open class DocumentStatusResponse
constructor(
    val documentId: String,
    val status: DocumentStatus,
    val secondsRemaining: Int?,
    val billedCharacters: Int?,
)
