package deepl.api.v2.misc

import java.net.URLEncoder

object InternalUtility {
  fun urlEncode(value: String): String = URLEncoder.encode(value, "UTF-8")
}
