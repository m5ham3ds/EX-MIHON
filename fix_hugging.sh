#!/bin/bash
sed -i 's/runCatching {/try {/g' app/src/main/java/com/example/data/remote/HuggingFaceApiClient.kt
sed -i 's/200 -> AnalysisResponse("Madara", emptyMap(), 0.9f, "OK") \/\/ Simplified/200 -> Resource.Success(AnalysisResponse("Madara", emptyMap(), 0.9f, "OK"))/g' app/src/main/java/com/example/data/remote/HuggingFaceApiClient.kt
sed -i '/        }/a \        } catch (e: Exception) {\n            Resource.Error(e.message ?: "Error", e)\n        }' app/src/main/java/com/example/data/remote/HuggingFaceApiClient.kt
