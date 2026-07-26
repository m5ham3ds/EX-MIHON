package com.example.worker

import com.example.core.utils.FileUtils
import com.example.engine.generator.CodeGenerator
import com.example.engine.generator.ProjectTreeGenerator
import com.google.gson.Gson
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WorkerEntryPoint {
    fun projectTreeGenerator(): ProjectTreeGenerator
    fun codeGenerator(): CodeGenerator
    fun fileUtils(): FileUtils
    fun gson(): Gson
}
