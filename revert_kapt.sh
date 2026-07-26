#!/bin/bash
sed -i '/id("kotlin-kapt")/d' app/build.gradle.kts
sed -i 's/"kapt"("com.google.dagger:hilt-android-compiler/"ksp"("com.google.dagger:hilt-android-compiler/g' app/build.gradle.kts
sed -i 's/"kapt"("androidx.hilt:hilt-compiler/"ksp"("androidx.hilt:hilt-compiler/g' app/build.gradle.kts
