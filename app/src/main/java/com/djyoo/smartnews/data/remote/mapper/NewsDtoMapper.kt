package com.djyoo.smartnews.data.remote.mapper

import com.djyoo.smartnews.data.remote.dto.NaverNewsItemDto
import com.djyoo.smartnews.domain.model.Article
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun NaverNewsItemDto.toDomain(
    keywords: List<String>,
    fetchedAt: Long,
): Article =
    Article(
        id = originalLink.ifBlank { link },
        title = title.stripHtmlTags().decodeHtmlEntities(),
        description = description.stripHtmlTags().decodeHtmlEntities(),
        link = link,
        originalLink = originalLink,
        pubDate = pubDate.toEpochMillisOrNow(),
        keywords = keywords,
        fetchedAt = fetchedAt,
    )

private fun String.stripHtmlTags(): String = replace(Regex("<[^>]*>"), "").trim()

private fun String.decodeHtmlEntities(): String {
    val namedDecoded =
        this
            .replace("&quot;", "\"")
            .replace("&#34;", "\"")
            .replace("&apos;", "'")
            .replace("&#39;", "'")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&nbsp;", " ")

    val decimalDecoded =
        Regex("&#(\\d+);").replace(namedDecoded) { match ->
            val codePoint = match.groupValues[1].toIntOrNull() ?: return@replace match.value
            runCatching { String(Character.toChars(codePoint)) }.getOrDefault(match.value)
        }

    return Regex("&#x([0-9A-Fa-f]+);").replace(decimalDecoded) { match ->
        val codePoint = match.groupValues[1].toIntOrNull(16) ?: return@replace match.value
        runCatching { String(Character.toChars(codePoint)) }.getOrDefault(match.value)
    }
}

private fun String.toEpochMillisOrNow(): Long =
    runCatching {
        ZonedDateTime.parse(this, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant().toEpochMilli()
    }.getOrElse { System.currentTimeMillis() }
