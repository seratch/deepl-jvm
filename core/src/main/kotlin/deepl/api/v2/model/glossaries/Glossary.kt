package deepl.api.v2.model.glossaries

open class Glossary
constructor(
    val glossaryId: String,
    val name: String,
    val ready: Boolean,
    val sourceLang: GlossarySourceLanguage,
    val targetLang: GlossaryTargetLanguage,
    val creationTime: String,
    val entryCount: Int,
)
