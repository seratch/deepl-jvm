package tests.local_tests

import deepl.api.v2.DeepLClient
import deepl.api.v2.model.translation.SourceLanguage
import deepl.api.v2.model.translation.TargetLanguage
import kotlin.test.assertEquals
import org.junit.Before
import org.junit.Test

class TranslationTextTest {

  var client: DeepLClient? = null

  @Before
  fun setup() {
    this.client =
        DeepLClient(
            token = System.getenv("DEEPL_AUTH_KEY"),
            baseUrl = "http://localhost:3000/v2",
        )
  }

  @Test
  fun translateSingleText() {
    this.client!!.use { client ->
      val response =
          client.translate(
              text = "proton beam",
              targetLang = TargetLanguage.Japanese,
          )
      assertEquals(SourceLanguage.English, response.translations[0].detectedSourceLanguage)
      assertEquals("陽子ビーム", response.translations[0].text)
    }
  }

  @Test
  fun translateMultipleTexts() {
    this.client!!.use { client ->
      val response =
          client.translate(
              text = listOf("proton beam 1", "proton beam 2"),
              targetLang = TargetLanguage.Japanese,
          )
      assertEquals(SourceLanguage.English, response.translations[0].detectedSourceLanguage)
      assertEquals("陽子ビーム", response.translations[0].text)
      assertEquals(SourceLanguage.English, response.translations[1].detectedSourceLanguage)
      assertEquals("陽子ビーム", response.translations[1].text)
    }
  }
}
