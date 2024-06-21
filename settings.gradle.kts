pluginManagement {
   repositories {
      google {
         content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("com\\.google.*")
            includeGroupByRegex("androidx.*")
         }
      }
      mavenCentral()
      gradlePluginPortal()
      @Suppress("DEPRECATION")
      jcenter()
   }
}
dependencyResolutionManagement {
   repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
   repositories {
      google()
      mavenCentral()
      @Suppress("DEPRECATION")
      jcenter()
   }
}

rootProject.name = "SampleIntegrateEkycNfc"
include(":app")
include(":ekyc")
include(":nfc")
