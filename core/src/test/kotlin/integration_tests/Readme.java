package integration_tests;

import deepl.api.v2.DeepLClient;
import deepl.api.v2.model.glossaries.GlossarySourceLanguage;
import deepl.api.v2.model.glossaries.GlossaryTargetLanguage;
import deepl.api.v2.model.translation.TargetLanguage;
import deepl.api.v2.model.translation.document.DocumentStatus;
import deepl.api.v2.request.glossaries.GlossaryCreationRequest;
import deepl.api.v2.request.translation.document.DocumentUploadRequest;
import deepl.api.v2.request.translation.text.TextTranslationRequest;
import deepl.api.v2.response.glossaries.*;
import deepl.api.v2.response.translation.document.DocumentStatusResponse;
import deepl.api.v2.response.translation.document.DocumentUploadResponse;
import deepl.api.v2.response.translation.text.TextTranslationResponse;
import deepl.api.v2.response.usage.UsageResponse;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class Readme {
    public static void main(String[] args) throws Exception {
        try (DeepLClient client = new DeepLClient(System.getenv("DEEPL_AUTH_KEY"))) {
            // -----------------------------
            // Usage data
            UsageResponse usage = client.getUsage();
            System.out.println(usage.getCharacterCount());
            assertThat(usage.getCharacterCount(), is(notNullValue()));

            // -----------------------------
            // Text translation
            TextTranslationResponse translation = client.translate(new TextTranslationRequest(
                    "こんにちは！私は元気です :wave:",
                    TargetLanguage.AmericanEnglish
            ));
            System.out.println(translation.getTranslations().get(0).getText());
            String result = translation.getTranslations().get(0).getText();
            assertThat(result, is("Hello! I am fine :wave:"));

            // -----------------------------
            // Document translation
            DocumentUploadResponse uploadedDocument = client.uploadDocument(new DocumentUploadRequest(
                    new File("core/src/test/resources/test-en.docx"),
                    TargetLanguage.Japanese
            ));
            assertNotNull(uploadedDocument.getDocumentId());
            assertNotNull(uploadedDocument.getDocumentKey());

            while (true) {
                DocumentStatusResponse res = client.getDocumentStatus(
                        uploadedDocument.getDocumentId(),
                        uploadedDocument.getDocumentKey()
                );
                if (res.getStatus() == DocumentStatus.Done.Done || res.getStatus() == DocumentStatus.Error) {
                    break;
                }
                Double remainingSeconds = 0.0D;
                if (res.getSecondsRemaining() != null) {
                    remainingSeconds = res.getSecondsRemaining().doubleValue();
                }
                Double secondsToSleep = Math.max(1.0, Math.min(remainingSeconds, 60.0));
                Thread.sleep(secondsToSleep.longValue());
            }

            byte[] resultFileContentBytes = client.downloadDocument(
                    uploadedDocument.getDocumentId(),
                    uploadedDocument.getDocumentKey()
            );
            File jaFile = new File("core/src/test/resources/test-ja.docx");
            Files.write(jaFile.toPath(), resultFileContentBytes);

            // -----------------------------
            // Glossaries
            Map<String, String> entries = new HashMap<>();
            entries.put("artist", "Maler");
            entries.put("prize", "Gewinn");
            GlossaryCreationResponse newGlossary = client.createGlossary(new GlossaryCreationRequest(
                    "something-new-" + System.currentTimeMillis(),
                    GlossarySourceLanguage.English,
                    GlossaryTargetLanguage.German,
                    entries
            ));
            assert (newGlossary.getGlossary().getReady());
            String glossaryId = newGlossary.getGlossary().getGlossaryId();

            GlossariesResponse glossaries = client.listGlossaries();
            assert (glossaries.getGlossaries().size() > 0);

            GlossaryResponse glossary = client.getGlossary(glossaryId);
            assertNotNull(glossary);

            GlossaryEntriesResponse glossaryEntries = client.listGlossaryEntries(glossaryId);
            assertNotNull(glossaryEntries);

            GlossaryDeletionResponse deletion = client.deleteGlossary(glossaryId);
            assertNotNull(deletion);
        }
    }
}
