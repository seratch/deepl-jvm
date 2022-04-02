package deepl.api.v2.model.translation.text

import com.google.gson.annotations.SerializedName

enum class OutlineDetection constructor(val value: String) {
  @SerializedName("0") Disabled("0"),
  @SerializedName("1") Enabled("1")
}
