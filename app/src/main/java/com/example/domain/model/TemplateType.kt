package com.example.domain.model

import com.google.gson.annotations.SerializedName

enum class TemplateType(
    val displayName: String,
    val description: String,
    val assetPath: String,
    val detectionSignatures: List<String>
) {
    MADARA(
        displayName = "Madara",
        description = "القالب الأكثر انتشارًا لمواقع المانغا (WordPress Madara Theme)",
        assetPath = "templates/Madara",
        detectionSignatures = listOf(
            "wp-manga", "madara", "manga-reading-content",
            "wp-manga-chapter", "site-breadcrumb"
        )
    ),
    MANGA_READER(
        displayName = "MangaReader",
        description = "قالب WordPress MangaReader الشهير",
        assetPath = "templates/MangaReader",
        detectionSignatures = listOf(
            "bsx", "mangareader", "manga-info-top",
            "chapter-list", "listupd"
        )
    ),
    WORDPRESS_GENERIC(
        displayName = "WordPress Generic",
        description = "مواقع WordPress بقوالب مخصصة",
        assetPath = "templates/WordPressGeneric",
        detectionSignatures = listOf(
            "wp-content", "wordpress", "woocommerce",
            "wp-json", "xmlrpc.php"
        )
    ),
    CUSTOM_JSON_API(
        displayName = "Custom JSON API",
        description = "مواقع تعتمد بالكامل على REST API",
        assetPath = "templates/CustomJsonApi",
        detectionSignatures = listOf(
            "application/json", "api/v", "/api/manga",
            "Authorization: Bearer", "X-API-Key"
        )
    )
}
