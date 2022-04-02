package deepl.api.v2.model.translation.text

import com.google.gson.annotations.SerializedName

enum class SplitSentences constructor(val value: String) {
  @SerializedName("0") Disabled("0"),
  @SerializedName("1") BothPunctuationsAndNewlines("1"), // default
  @SerializedName("nonewlines") OnlyPunctuations("nonewlines")
}
