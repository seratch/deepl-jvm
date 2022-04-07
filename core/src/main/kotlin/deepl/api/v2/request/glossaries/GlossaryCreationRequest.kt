package deepl.api.v2.request.glossaries

import deepl.api.misc.InternalUtility.urlEncode
import deepl.api.v2.model.glossaries.EntriesFormat
import deepl.api.v2.model.glossaries.GlossarySourceLanguage
import deepl.api.v2.model.glossaries.GlossaryTargetLanguage

open class GlossaryCreationRequest
@JvmOverloads
constructor(
    private val name: String,
    private val sourceLang: GlossarySourceLanguage,
    private val targetLang: GlossaryTargetLanguage,
    private val entries: Map<String, String>,
    private val entriesFormat: EntriesFormat = EntriesFormat.TabSeparatedValues
) {
  fun toRequestBody(): String {
    val body = StringBuilder()
    body.append("&name=${urlEncode(name)}")
    body.append("&source_lang=${sourceLang.value}")
    body.append("&target_lang=${targetLang.value}")
    val entriesTSV = entries.map { "${it.key}\t${it.value}" }.joinToString("\n")
    body.append("&entries=${urlEncode(entriesTSV)}")
    body.append("&entries_format=${entriesFormat.value}")
    return body.toString().replaceFirst("^&", "")
  }
}
