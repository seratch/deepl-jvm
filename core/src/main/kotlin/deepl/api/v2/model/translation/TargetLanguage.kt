package deepl.api.v2.model.translation

import com.google.gson.annotations.SerializedName

enum class TargetLanguage constructor(val value: String) {
  @SerializedName("BG") Bulgarian("BG"),
  @SerializedName("CS") Czech("CS"),
  @SerializedName("DA") Danish("DA"),
  @SerializedName("DE") German("DE"),
  @SerializedName("EL") Greek("EL"),
  @SerializedName("EN-GB") BritishEnglish("EN-GB"),
  @SerializedName("EN-US") AmericanEnglish("EN-US"),
  @SerializedName("ES") Spanish("ES"),
  @SerializedName("ET") Estonian("ET"),
  @SerializedName("FI") Finnish("FI"),
  @SerializedName("FR") French("FR"),
  @SerializedName("HU") Hungarian("HU"),
  @SerializedName("IT") Italian("IT"),
  @SerializedName("JA") Japanese("JA"),
  @SerializedName("LT") Lithuanian("LT"),
  @SerializedName("LV") Latvian("LV"),
  @SerializedName("NL") Dutch("NL"),
  @SerializedName("PL") Polish("PL"),
  @SerializedName("PT-PT") Portuguese("PT-PT"),
  @SerializedName("PT-BR") BrazilianPortuguese("PT-BR"),
  @SerializedName("RO") Romanian("RO"),
  @SerializedName("RU") Russian("RU"),
  @SerializedName("SK") Slovak("SK"),
  @SerializedName("SL") Slovenian("SL"),
  @SerializedName("SV") Swedish("SV"),
  @SerializedName("ZH") Chinese("ZH");

  override fun toString(): String = value
}
