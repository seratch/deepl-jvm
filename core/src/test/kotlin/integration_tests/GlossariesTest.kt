package integration_tests

import deepl.api.v2.DeepLClient
import deepl.api.v2.model.glossaries.GlossarySourceLanguage
import deepl.api.v2.model.glossaries.GlossaryTargetLanguage
import deepl.api.v2.request.glossaries.GlossaryCreationRequest
import deepl.api.v2.request.glossaries.GlossaryDeletionRequest
import deepl.api.v2.request.glossaries.GlossaryEntriesRequest
import deepl.api.v2.request.glossaries.GlossaryRequest
import kotlin.test.assertNotNull
import org.junit.Test

class GlossariesTest {

  @Test
  fun glossaryLanguagePairs() {
    DeepLClient(token = System.getenv("DEEPL_AUTH_KEY")).use { client ->
      val response = client.listGlossaryLanguagePairs()
      assertNotNull(response.supportedLanguages)
    }
  }

  @Test
  fun operations() {
    DeepLClient(token = System.getenv("DEEPL_AUTH_KEY")).use { client ->
      val newGlossary =
          client.createGlossary(
              GlossaryCreationRequest(
                  name = "something-new-${System.currentTimeMillis()}",
                  sourceLang = GlossarySourceLanguage.English,
                  targetLang = GlossaryTargetLanguage.German,
                  entries =
                      mapOf(
                          "artist" to "Maler",
                          "prize" to "Gewinn",
                      )))
      assert(newGlossary.glossary.ready)
      val glossaryId = newGlossary.glossary.glossaryId

      val glossaries = client.listGlossaries()
      assert(glossaries.glossaries.isNotEmpty())

      val glossary = client.getGlossary(GlossaryRequest(glossaryId = glossaryId))
      assertNotNull(glossary)

      val glossaryEntries =
          client.listGlossaryEntries(GlossaryEntriesRequest(glossaryId = glossaryId))
      assertNotNull(glossaryEntries)

      val deletion =
          client.deleteGlossary(
              GlossaryDeletionRequest(
                  glossaryId = glossaryId,
              ))
      assertNotNull(deletion)
    }
  }
}
