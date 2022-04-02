package deepl.api.v2.model.languages

import com.google.gson.annotations.SerializedName

enum class LanguageType constructor(val value: String) {
  @SerializedName("source") Source("source"), // default
  @SerializedName("target") Target("target")
}
