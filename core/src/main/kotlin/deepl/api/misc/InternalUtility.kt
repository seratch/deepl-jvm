package deepl.api.misc

import java.net.URLEncoder

object InternalUtility {
  fun urlEncode(value: String): String = URLEncoder.encode(value, "UTF-8")
}
