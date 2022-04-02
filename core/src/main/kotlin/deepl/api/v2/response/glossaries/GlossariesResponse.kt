package deepl.api.v2.response.glossaries

import deepl.api.v2.model.glossaries.Glossary

open class GlossariesResponse
constructor(
    val glossaries: List<Glossary>,
)
