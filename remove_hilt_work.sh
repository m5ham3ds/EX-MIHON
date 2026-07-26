#!/bin/bash
sed -i '/androidx.hilt:hilt-work/d' app/build.gradle.kts
sed -i '/androidx.hilt:hilt-compiler/d' app/build.gradle.kts
