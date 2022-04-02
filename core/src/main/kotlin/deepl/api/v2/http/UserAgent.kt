package deepl.api.v2.http

object UserAgent {

  @JvmStatic
  fun buildUserAgent(): String {
    val libraryVersion = deepl.api.Metadata.VERSION
    val library = "deepl-jvm/$libraryVersion"
    val repo = "https://github.com/seratch/deepl-jvm"
    val jvm =
        "" + System.getProperty("java.vm.name") + "/" + System.getProperty("java.version") + ""
    val os = "" + System.getProperty("os.name") + "/" + System.getProperty("os.version") + ""
    return "$library; $repo; $jvm; $os"
  }
}
