package deepl.api.v2.request.translation.document

import deepl.api.v2.model.translation.Formality
import deepl.api.v2.model.translation.SourceLanguage
import deepl.api.v2.model.translation.TargetLanguage
import java.io.File

open class DocumentUploadRequest
@JvmOverloads
constructor(
    /**
     * The document file to be translated. The file name should be included in this part's content
     * disposition. As an alternative, the filename parameter can be used. The following file types
     * and extensions are supported:
     * - "docx" - Microsoft Word Document
     * - "pptx" - Microsoft PowerPoint Document
     * - "pdf" - Portable Document Format
     * - "htm / html" - HTML Document
     * - "txt" - Plain Text Document Please note that in order to translate PDF documents you need
     * to give one-time consent to using the Adobe API via the account interface.
     */
    val file: ByteArray,
    /**
     * The name of the uploaded file. Can be used as an alternative to including the file name in
     * the file part's content disposition.
     */
    val filename: String,
    /** The language into which the text should be translated. */
    private val targetLang: TargetLanguage,
    /** Language of the text to be translated. */
    private val sourceLang: SourceLanguage? = null,
    /**
     * Sets whether the translated text should lean towards formal or informal language. Possible
     * options are:
     * - "default" (default)
     * - "more" - for a more formal language
     * - "less" - for a more informal language
     */
    private val formality: Formality? = null,
    /**
     * Specify the glossary to use for the translation. Important: This requires the source_lang
     * parameter to be set and the language pair of the glossary has to match the language pair of
     * the request.
     */
    private val glossaryId: String? = null,
) {

  @JvmOverloads
  constructor(
      file: File,
      targetLang: TargetLanguage,
      sourceLang: SourceLanguage? = null,
      filename: String = "document.${file.name}",
      formality: Formality? = null,
      glossaryId: String? = null,
  ) : this(
      file = file.readBytes(),
      targetLang = targetLang,
      sourceLang = sourceLang,
      filename = filename,
      formality = formality,
      glossaryId = glossaryId,
  )

  fun params(): Map<String, String> {
    val params = mutableMapOf("target_lang" to targetLang.value)
    if (sourceLang != null) {
      params["source_lang"] = sourceLang.value
    }
    if (formality != null) {
      params["formality"] = formality.value
    }
    if (glossaryId != null) {
      params["glossary_id"] = glossaryId
    }
    return params
  }
}
