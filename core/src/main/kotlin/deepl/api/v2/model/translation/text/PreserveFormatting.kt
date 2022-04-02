package deepl.api.v2.model.translation.text

import com.google.gson.annotations.SerializedName

enum class PreserveFormatting constructor(val value: String) {
  @SerializedName("0") Disabled("0"), // default
  @SerializedName("1") Enabled("1")
}
