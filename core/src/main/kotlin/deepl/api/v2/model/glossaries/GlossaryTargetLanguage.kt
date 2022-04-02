package deepl.api.v2.model.glossaries

import com.google.gson.annotations.SerializedName

enum class GlossaryTargetLanguage constructor(val value: String) {
  @SerializedName("de") German("de"),
  @SerializedName("en") English("en"),
  @SerializedName("es") Spanish("es"),
  @SerializedName("fr") French("fr");

  override fun toString(): String = value
}
