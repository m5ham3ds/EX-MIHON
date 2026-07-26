package com.example.domain.model

enum class BuildStep(val displayName: String, val weight: Int) {
    VALIDATING_CONFIG("التحقق من الإعدادات", 5),
    LOADING_TEMPLATE("تحميل القالب", 10),
    CUSTOMIZING_TEMPLATE("تخصيص القالب", 20),
    GENERATING_PROJECT_TREE("إنشاء هيكل المشروع", 15),
    GENERATING_SOURCE_CODE("كتابة ملفات الكود", 20),
    RUNNING_GRADLE_BUILD("تشغيل Gradle Build", 25),
    SIGNING_APK("توقيع الـ APK", 3),
    COPYING_OUTPUT("نسخ الملفات النهائية", 2)
}
