package deepl.api.v2.model.translation.text

import com.google.gson.annotations.SerializedName

enum class TagHandling constructor(val value: String) {
  @SerializedName("xml") XML("xml"),
  @SerializedName("html") HTML("html")
}
