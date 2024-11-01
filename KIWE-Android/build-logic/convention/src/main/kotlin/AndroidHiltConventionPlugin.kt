import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import utils.libs

internal class
AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply(libs.findPlugin("hilt").get().get().pluginId)
                apply(libs.findPlugin("ksp").get().get().pluginId)
            }

            dependencies {
                add("ksp", libs.findLibrary("hilt.compiler").get())
                add("ksp", libs.findLibrary("androidx.hilt.compiler").get())
                add("kspAndroidTest", libs.findLibrary("hilt.compiler").get())
                add("implementation", libs.findLibrary("hilt.android").get())
                add("androidTestImplementation", libs.findLibrary("hilt.android.testing").get())
            }
        }
    }
}