#!/bin/bash
for file in app/src/main/java/com/example/domain/usecase/AnalyzeWebsiteUseCase.kt \
  app/src/main/java/com/example/domain/usecase/SaveExtensionConfigUseCase.kt \
  app/src/main/java/com/example/domain/repository/ProjectRepository.kt \
  app/src/main/java/com/example/engine/generator/CodeGenerator.kt \
  app/src/main/java/com/example/data/remote/HuggingFaceApiClient.kt \
  app/src/main/java/com/example/data/repository/ProjectRepositoryImpl.kt; do
  
  # Remove all instances of "import com.example.core.utils.Resource" from the file
  sed -i '/import com.example.core.utils.Resource/d' "$file"
  
  # Insert it right after the package declaration
  awk '/^package / { print; print "import com.example.core.utils.Resource"; next }1' "$file" > temp.kt && mv temp.kt "$file"
done
