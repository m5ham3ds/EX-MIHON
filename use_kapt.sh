#!/bin/bash
sed -i 's/alias(libs.plugins.google.devtools.ksp)/alias(libs.plugins.google.devtools.ksp)\n  id("kotlin-kapt")/g' app/build.gradle.kts
sed -i 's/"ksp"("com.google.dagger:hilt-android-compiler/"kapt"("com.google.dagger:hilt-android-compiler/g' app/build.gradle.kts
sed -i 's/"ksp"("androidx.hilt:hilt-compiler/"kapt"("androidx.hilt:hilt-compiler/g' app/build.gradle.kts
