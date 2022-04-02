package deepl.api.v2.model.glossaries

import com.google.gson.annotations.SerializedName

enum class EntriesFormat constructor(val value: String) {
  @SerializedName("tsv") TabSeparatedValues("tsv");

  override fun toString(): String = value
}
