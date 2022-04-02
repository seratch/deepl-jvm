package deepl.api.v2.response.languages

open class LanguagesResponse
constructor(
    val languages: List<Language>,
) {
  open class Language
  constructor(
      val language: String,
      val name: String,
      val supportsFormality: Boolean,
  )
}
