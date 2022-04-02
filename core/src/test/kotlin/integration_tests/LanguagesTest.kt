package integration_tests

import deepl.api.v2.DeepLClient
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test

class LanguagesTest {

  @Test
  fun languages() {
    DeepLClient(token = System.getenv("DEEPL_AUTH_KEY")).use { client ->
      val response = client.listLanguages()
      assertNotNull(response.languages.size)
      assertTrue { response.languages.size >= 24 }
    }
  }
}
