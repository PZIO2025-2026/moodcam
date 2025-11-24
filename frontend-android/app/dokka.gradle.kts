import org.jetbrains.dokka.gradle.DokkaTask

tasks.withType<DokkaTask>().configureEach {
    outputDirectory.set(file("$buildDir/dokka"))
    
    dokkaSourceSets {
        configureEach {
            moduleName.set("MoodCam Android")
            
            // Show everything including private/internal
            includeNonPublic.set(true)
            skipDeprecated.set(false)
            skipEmptyPackages.set(false)
            
            // Document undocumented code
            reportUndocumented.set(false)
            
            sourceLink {
                localDirectory.set(file("src/main/java"))
                remoteUrl.set(uri("https://github.com/PZIO2025-2026/moodcam/tree/main/frontend-android/app/src/main/java").toURL())
                remoteLineSuffix.set("#L")
            }
            
            // Suppress warnings
            suppressInheritedMembers.set(false)
        }
    }
}
