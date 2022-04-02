package integration_tests

import deepl.api.v2.DeepLClient
import kotlin.test.assertNotNull
import org.junit.Test

class UsageTest {

  @Test
  fun usage() {
    DeepLClient(token = System.getenv("DEEPL_AUTH_KEY")).use { client ->
      val response = client.getUsage()
      assertNotNull(response)
    }
  }
}
