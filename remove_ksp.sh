#!/bin/bash
sed -i 's/alias(libs.plugins.google.devtools.ksp)/id("kotlin-kapt")/g' app/build.gradle.kts
sed -i 's/"ksp"("com.google.dagger:hilt-android-compiler:2.51.1")/"kapt"("com.google.dagger:hilt-android-compiler:2.51.1")/g' app/build.gradle.kts
sed -i '/libs.androidx.room.compiler/d' app/build.gradle.kts
echo "android.builtInKotlin=false" >> gradle.properties
echo "android.newDsl=false" >> gradle.properties
