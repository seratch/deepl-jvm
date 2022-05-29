package deepl.api.v2.model.glossaries

import com.google.gson.annotations.SerializedName

enum class GlossarySourceLanguage constructor(val value: String) {
  @SerializedName("de") German("de"),
  @SerializedName("en") English("en"),
  @SerializedName("es") Spanish("es"),
  @SerializedName("fr") French("fr"),
  @SerializedName("ja") Japanese("ja");

  override fun toString(): String = value
}
