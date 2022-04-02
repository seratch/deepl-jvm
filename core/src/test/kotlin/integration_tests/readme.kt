package integration_tests

import deepl.api.v2.DeepLClient
import deepl.api.v2.model.glossaries.GlossarySourceLanguage
import deepl.api.v2.model.glossaries.GlossaryTargetLanguage
import deepl.api.v2.model.translation.TargetLanguage
import deepl.api.v2.model.translation.document.DocumentStatus
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

fun main() {
  val client = DeepLClient(token = System.getenv("DEEPL_AUTH_KEY"))
  client.use { client ->
    // -----------------------------
    // Usage data
    val usage = client.getUsage()
    println(usage.characterCount)
    assertNotNull(usage.characterCount)

    // -----------------------------
    // Text translation
    val translation =
        client.translate(
            text = "こんにちは！私は元気です :wave:",
            targetLang = TargetLanguage.AmericanEnglish,
        )
    println(translation.translations[0].text)
    assertEquals("Hello! I am fine :wave:", translation.translations[0].text)

    // -----------------------------
    // Document translation
    val uploadedDocument =
        client.uploadDocument(
            file = File("core/src/test/resources/test-en.docx"),
            filename = "test-en.docx",
            targetLang = TargetLanguage.Japanese,
        )
    assertNotNull(uploadedDocument.documentId)
    assertNotNull(uploadedDocument.documentKey)

    while (true) {
      val res =
          client.getDocumentStatus(
              documentId = uploadedDocument.documentId,
              documentKey = uploadedDocument.documentKey,
          )
      if (res.status == DocumentStatus.Done || res.status == DocumentStatus.Error) {
        break
      }
      Thread.sleep(500L)
    }

    val resultFileContentBytes =
        client.downloadDocument(
            documentId = uploadedDocument.documentId,
            documentKey = uploadedDocument.documentKey,
        )
    File("core/src/test/resources/test-ja.docx").writeBytes(resultFileContentBytes)

    // -----------------------------
    // Glossaries
    val newGlossary =
        client.createGlossary(
            name = "something-new-${System.currentTimeMillis()}",
            sourceLang = GlossarySourceLanguage.English,
            targetLang = GlossaryTargetLanguage.German,
            entries =
                mapOf(
                    "artist" to "Maler",
                    "prize" to "Gewinn",
                ))
    assert(newGlossary.glossary.ready)
    val glossaryId = newGlossary.glossary.glossaryId

    val glossaries = client.listGlossaries()
    assert(glossaries.glossaries.isNotEmpty())

    val glossary = client.getGlossary(glossaryId)
    assertNotNull(glossary)

    val glossaryEntries = client.listGlossaryEntries(glossaryId)
    assertNotNull(glossaryEntries)

    val deletion = client.deleteGlossary(glossaryId)
    assertNotNull(deletion)
  }
}
