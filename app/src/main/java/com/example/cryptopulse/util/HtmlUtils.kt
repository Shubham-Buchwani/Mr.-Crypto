package com.example.cryptopulse.util

object HtmlUtils {
    fun stripHtml(html: String?): String {
        if (html.isNullOrBlank()) return ""
        return html
            .replace(Regex("<br\\s*/?>"), "\n")
            .replace(Regex("<p>"), "")
            .replace(Regex("</p>"), "\n\n")
            .replace(Regex("<[^>]*>"), "")
            .replace(Regex("&amp;"), "&")
            .replace(Regex("&lt;"), "<")
            .replace(Regex("&gt;"), ">")
            .replace(Regex("&quot;"), "\"")
            .replace(Regex("&#39;"), "'")
            .replace(Regex("&nbsp;"), " ")
            .replace(Regex("\n{3,}"), "\n\n")
            .trim()
    }
}
