package deepl.api.v2.response.usage

open class UsageResponse
constructor(
    val characterCount: Int?,
    val characterLimit: Int?,
    val documentCount: Int?,
    val documentLimit: Int?,
    val teamDocumentCount: Int?,
    val teamDocumentLimit: Int?,
)
