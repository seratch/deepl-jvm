package integration_tests

import deepl.api.v2.DeepLClient
import deepl.api.v2.model.translation.TargetLanguage
import deepl.api.v2.model.translation.document.DocumentStatus
import deepl.api.v2.request.translation.document.DocumentStatusRequest
import deepl.api.v2.request.translation.document.DocumentUploadRequest
import java.io.File
import kotlin.test.assertNotNull
import org.junit.Test

class TranslationDocumentTest {

  @Test
  fun translateDocument() {
    DeepLClient(token = System.getenv("DEEPL_AUTH_KEY")).use { client ->
      val uploadedDocument =
          client.uploadDocument(
              DocumentUploadRequest(
                  file = File("src/test/resources/test-en.docx"),
                  filename = "test-en.docx",
                  targetLang = TargetLanguage.Japanese,
              ))
      assertNotNull(uploadedDocument.documentId)
      assertNotNull(uploadedDocument.documentKey)

      while (true) {
        val res =
            client.getDocumentStatus(
                DocumentStatusRequest(
                    documentId = uploadedDocument.documentId,
                    documentKey = uploadedDocument.documentKey,
                ))
        if (res.status == DocumentStatus.Done || res.status == DocumentStatus.Error) {
          break
        }
        val remainingSeconds: Double =
            if (res.secondsRemaining != null) res.secondsRemaining!!.toDouble() else 0.0
        val secondsToSleep = 1.0.coerceAtLeast((remainingSeconds / 2.0).coerceAtMost(60.0))
        Thread.sleep(secondsToSleep.toLong())
      }

      val fileData =
          client.downloadDocument(
              documentId = uploadedDocument.documentId,
              documentKey = uploadedDocument.documentKey,
          )
      File("src/test/resources/test-ja.docx").writeBytes(fileData)
    }
  }
}
