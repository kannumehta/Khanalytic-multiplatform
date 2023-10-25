package com.khanalytic.kmm.importing.intgerations

data class Cookie(val key: String, val value: String, val domain: String) {
    companion object {
        fun String.toCookies(domain: String) =
            this.split("; ")
                .map { c: String ->
                    c.split("=").let { pair: List<String> -> Cookie(pair[0], pair[1], domain) }
                }
    }
}