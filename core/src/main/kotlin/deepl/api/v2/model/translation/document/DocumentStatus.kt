package deepl.api.v2.model.translation.document

import com.google.gson.annotations.SerializedName

enum class DocumentStatus constructor(val value: String) {
  @SerializedName("queued") Queued("queued"), // default
  @SerializedName("translating") Translating("translating"),
  @SerializedName("done") Done("done"),
  @SerializedName("error") Error("error")
}
