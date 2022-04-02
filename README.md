# DeepL Kotlin/JavaVM Library

[![Maven Central](https://img.shields.io/maven-central/v/com.github.seratch/deepl-jvm.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.seratch%22%20AND%20a:%22deepl-jvm%22)
[![CI Build](https://github.com/seratch/deepl-jvm/actions/workflows/ci-build.yml/badge.svg)](https://github.com/seratch/deepl-jvm/actions/workflows/ci-build.yml)

Here is a [DeepL API](https://www.deepl.com/docs-api) SDK for any JVM language users :wave: 

This project aims to provide a DeepL API client for any JVM language developers without hurdles.
To realize the goal, its code is written in Kotlin with a nice consideration for Java compatibility.

This SDK works on [Android runtime](https://developer.android.com/) and any distributions based on [OpenJDK](https://openjdk.java.net/).
With regard to programming languages, this project provides out-of-the-box supports for Java (of course!) and [Kotlin](https://kotlinlang.org/).
We don't have nice wrappers for some other JVM languages such as [Scala](https://www.scala-lang.org/), [Groovy](https://groovy-lang.org/), and [Clojure](https://clojure.org/), but your code using this library should work in the languages too.
``
### Getting Started

You can start using this library just by adding `deepl-jvm` dependency to your project.

For Gradle users:

```gradle
ext.DeepLSDKVersion = "0.1.0"
dependencies {
  // This dependency is at least required
  implementation("com.github.seratch:deepl-jvm:${DeepLSDKVersion}")
}
```

For Maven users:

```xml
<properties>
  <deepl-sdk.version>0.1.0</deepl-sdk.version>
</properties>

<dependencies>
  <dependency>
  <groupId>com.github.seratch</groupId>
  <artifactId>deepl-jvm</artifactId>
  <version>${deepl-sdk.version}</version>
  </dependency>
</dependencies>
```

As this library is in Kotlin, using in the same language is the smoothest :) Let's start with the following code, which manipulates DeepL pages :wave:

```kotlin
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
        file = File("source.docx"),
        filename = "source.docx",
        targetLang = TargetLanguage.Japanese,
      )
    assertNotNull(uploadedDocument.documentId)
    assertNotNull(uploadedDocument.documentKey)

    while (true) {
      val res = client.getDocumentStatus(
        documentId = uploadedDocument.documentId,
        documentKey = uploadedDocument.documentKey,
      )
      if (res.status == DocumentStatus.Done || res.status == DocumentStatus.Error) {
        break
      }
      Thread.sleep(500L)
    }

    val resultFileContentBytes = client.downloadDocument(
      documentId = uploadedDocument.documentId,
      documentKey = uploadedDocument.documentKey,
    )
    File("translation-ja.docx").writeBytes(resultFileContentBytes)

    // -----------------------------
    // Glossaries
    val newGlossary = client.createGlossary(
      name = "something-new-${System.currentTimeMillis()}",
      sourceLang = GlossarySourceLanguage.English,
      targetLang = GlossaryTargetLanguage.German,
      entries = mapOf("artist" to "Maler", "prize" to "Gewinn")
    )
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
```

#### Using in Java

Even when you use this SDK in Java and other languages, all the classes/methods should be accessible. If you find issues, please let us know the issue in [this project's issue tracker](https://github.com/seratch/deepl-jvm/issues).

```java
import deepl.api.v2.DeepLClient;
import deepl.api.v2.model.glossaries.GlossarySourceLanguage;
import deepl.api.v2.model.glossaries.GlossaryTargetLanguage;
import deepl.api.v2.model.translation.TargetLanguage;
import deepl.api.v2.model.translation.document.DocumentStatus;
import deepl.api.v2.request.glossaries.GlossaryCreationRequest;
import deepl.api.v2.request.glossaries.GlossaryDeletionRequest;
import deepl.api.v2.request.glossaries.GlossaryEntriesRequest;
import deepl.api.v2.request.glossaries.GlossaryRequest;
import deepl.api.v2.request.translation.document.DocumentDownloadRequest;
import deepl.api.v2.request.translation.document.DocumentStatusRequest;
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
        Thread.sleep(500L);
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
```

### Supported Java Runtimes

* OpenJDK 8 or newer
* All Android runtime versions supported by Kotlin 1.6

### License

The MIT License
