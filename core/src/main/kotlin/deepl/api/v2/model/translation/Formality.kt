package deepl.api.v2.model.translation

import com.google.gson.annotations.SerializedName

enum class Formality constructor(val value: String) {
  @SerializedName("default") Default("default"), // default
  @SerializedName("more") More("more"),
  @SerializedName("less") Less("less")
}
