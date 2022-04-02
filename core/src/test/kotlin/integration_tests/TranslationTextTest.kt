package integration_tests

import deepl.api.v2.DeepLClient
import deepl.api.v2.model.translation.SourceLanguage
import deepl.api.v2.model.translation.TargetLanguage
import kotlin.test.assertEquals
import org.junit.Test

class TranslationTextTest {

  @Test
  fun translateSingleText() {
    DeepLClient(token = System.getenv("DEEPL_AUTH_KEY")).use { client ->
      val response =
          client.translate(
              text = "こんにちは！お元気ですか？",
              targetLang = TargetLanguage.AmericanEnglish,
          )
      assertEquals(SourceLanguage.Japanese, response.translations[0].detectedSourceLanguage)
      assertEquals("Hello! How are you?", response.translations[0].text)
    }
  }

  @Test
  fun translateMultipleTexts() {
    DeepLClient(token = System.getenv("DEEPL_AUTH_KEY")).use { client ->
      val response =
          client.translate(
              text = listOf("すみません、今ちょっと忙しいかもです。", "資料を添付いたしますので、ご確認ください。"),
              targetLang = TargetLanguage.AmericanEnglish,
          )
      assertEquals(SourceLanguage.Japanese, response.translations[0].detectedSourceLanguage)
      assertEquals("Sorry, I may be a little busy right now.", response.translations[0].text)
      assertEquals(SourceLanguage.Japanese, response.translations[1].detectedSourceLanguage)
      assertEquals("Please find attached the documents.", response.translations[1].text)
    }
  }
}
