#!/bin/bash
sed -i 's/kotlin("kapt")/alias(libs.plugins.google.devtools.ksp)/g' app/build.gradle.kts
sed -i 's/"kapt"("com.google.dagger:hilt-android-compiler:2.51.1")/"ksp"("com.google.dagger:hilt-android-compiler:2.51.1")/g' app/build.gradle.kts
