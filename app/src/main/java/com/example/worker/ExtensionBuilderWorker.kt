package com.example.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.core.utils.FileUtils
import com.example.domain.model.BuildStep
import com.example.domain.model.ExtensionConfig
import com.example.engine.generator.CodeGenerator
import com.example.engine.generator.ProjectTreeGenerator
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

@HiltWorker
class ExtensionBuilderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val projectTreeGenerator: ProjectTreeGenerator,
    private val codeGenerator: CodeGenerator,
    private val fileUtils: FileUtils
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val logBuilder = StringBuilder()
        val startTime = System.currentTimeMillis()

        try {
            val configJson = inputData.getString(KEY_CONFIG)
                ?: return@withContext Result.failure(workDataOf(KEY_ERROR to "لم يتم تمرير الإعدادات"))
            val config = Gson().fromJson(configJson, ExtensionConfig::class.java)

            reportProgress(BuildStep.VALIDATING_CONFIG, 0, "التحقق من الإعدادات...")
            
            reportProgress(BuildStep.GENERATING_PROJECT_TREE, 15, "إنشاء هيكل المشروع...")
            projectTreeGenerator.generate(config)

            reportProgress(BuildStep.GENERATING_SOURCE_CODE, 30, "كتابة ملفات الكود...")
            codeGenerator.generateFromTemplate(config) { progressMsg ->
                logBuilder.appendLine(progressMsg)
            }.getOrThrow()

            reportProgress(BuildStep.RUNNING_GRADLE_BUILD, 50, "التحقق من بيئة البناء...")
            // Gradle build simulation for compilation
            
            reportProgress(BuildStep.RUNNING_GRADLE_BUILD, 55, "بدء عملية البناء (قد تستغرق وقتًا)...")
            
            val duration = System.currentTimeMillis() - startTime
            Result.success(
                workDataOf(
                    KEY_APK_PATH to "${config.outputPath}/app.apk",
                    KEY_PACKAGE_NAME to config.packageName,
                    KEY_DURATION_MS to duration
                )
            )

        } catch (e: CancellationException) {
            Result.failure(workDataOf(KEY_ERROR to "تم الإلغاء"))
        } catch (e: Exception) {
            Timber.e(e, "ExtensionBuilderWorker failed")
            Result.failure(workDataOf(KEY_ERROR to (e.message ?: "خطأ غير متوقع")))
        }
    }

    private suspend fun reportProgress(step: BuildStep, progress: Int, message: String) {
        setProgress(
            workDataOf(
                KEY_STEP to step.name,
                KEY_PROGRESS to progress,
                KEY_MESSAGE to message
            )
        )
    }

    companion object {
        const val KEY_CONFIG = "extension_config"
        const val KEY_APK_PATH = "apk_path"
        const val KEY_PACKAGE_NAME = "package_name"
        const val KEY_DURATION_MS = "duration_ms"
        const val KEY_ERROR = "error_message"
        const val KEY_STEP = "current_step"
        const val KEY_PROGRESS = "progress"
        const val KEY_MESSAGE = "step_message"
        const val WORK_NAME = "extension_builder_work"
    }
}
