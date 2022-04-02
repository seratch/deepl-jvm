package deepl.api.v2.request.translation.text

import deepl.api.v2.misc.InternalUtility.urlEncode
import deepl.api.v2.model.translation.Formality
import deepl.api.v2.model.translation.SourceLanguage
import deepl.api.v2.model.translation.TargetLanguage
import deepl.api.v2.model.translation.text.OutlineDetection
import deepl.api.v2.model.translation.text.PreserveFormatting
import deepl.api.v2.model.translation.text.SplitSentences
import deepl.api.v2.model.translation.text.TagHandling

open class TextTranslationRequest
private constructor(
    /**
     * Text to be translated. Only UTF8-encoded plain text is supported. The parameter may be
     * specified multiple times and translations are returned in the same order as they are
     * requested. Each of the parameter values may contain multiple sentences. Up to 50 texts can be
     * sent for translation in one request.
     */
    private val singleText: String?,
    private val texts: List<String>?,
    /** The language into which the text should be translated. */
    private val targetLang: TargetLanguage,
    /** Language of the text to be translated. */
    private val sourceLang: SourceLanguage? = null,
    /**
     * Sets whether the translation engine should first split the input into sentences. This is
     * enabled by default. Possible values are:
     * - "0" - no splitting at all, whole input is treated as one sentence
     * - "1" (default) - splits on punctuation and on newlines
     * - "nonewlines" - splits on punctuation only, ignoring newlines For applications that send one
     * sentence per text parameter, it is advisable to set split_sentences=0, in order to prevent
     * the engine from splitting the sentence unintentionally.
     */
    private val splitSentences: SplitSentences? = null,
    /**
     * Sets whether the translation engine should respect the original formatting, even if it would
     * usually correct some aspects. Possible values are:
     * - "0" (default)
     * - "1" The formatting aspects affected by this setting include:
     * - Punctuation at the beginning and end of the sentence
     * - Upper/lower case at the beginning of the sentence
     */
    private val preserveFormatting: PreserveFormatting? = null,
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
    /**
     * Sets which kind of tags should be handled. Options currently available:
     * - "xml"
     * - "html"
     */
    private val tagHandling: TagHandling? = null,
    /** Comma-separated list of XML tags which never split sentences. */
    private val nonSplittingTags: List<String>? = null,
    /** Please see the "Handling XML" section for further details. */
    private val outlineDetection: OutlineDetection? = null,
    /** Comma-separated list of XML tags which always cause splits. */
    private val splittingTags: List<String>? = null,
    /** Comma-separated list of XML tags that indicate text not to be translated. */
    private val ignoreTags: List<String>? = null,
) {

  @JvmOverloads
  constructor(
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
  ) : this(
      singleText = text,
      texts = null,
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
  )

  @JvmOverloads
  constructor(
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
  ) : this(
      singleText = null,
      texts = text,
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
  )

  fun toRequestBody(): String {
    val body = StringBuilder()
    if (singleText != null && singleText.trim().isNotEmpty()) {
      body.append("&text=${urlEncode(singleText)}")
    } else if (texts != null && texts.isNotEmpty()) {
      texts.forEach { body.append("&text=${urlEncode(it)}") }
    }
    body.append("&target_lang=${targetLang}")
    if (sourceLang != null) {
      body.append("&source_lang=${sourceLang}")
    }
    if (splitSentences != null) {
      body.append("&split_sentences=${splitSentences}")
    }
    if (preserveFormatting != null) {
      body.append("&preserve_formatting=${preserveFormatting}")
    }
    if (formality != null) {
      body.append("&formality=${formality}")
    }
    if (glossaryId != null) {
      body.append("&glossary_id=${urlEncode(glossaryId)}")
    }
    if (tagHandling != null) {
      body.append("&tag_handling=${tagHandling}")
    }
    if (nonSplittingTags != null) {
      body.append("&non_splitting_tags=${urlEncode(nonSplittingTags.joinToString(","))}")
    }
    if (outlineDetection != null) {
      body.append("&outline_detection=${outlineDetection}")
    }
    if (splittingTags != null) {
      body.append("&splitting_tags=${urlEncode(splittingTags.joinToString(","))}")
    }
    if (ignoreTags != null) {
      body.append("&ignore_tags=${urlEncode(ignoreTags.joinToString(","))}")
    }
    return body.toString().replaceFirst("^&", "")
  }
}
