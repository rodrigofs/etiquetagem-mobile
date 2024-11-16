import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    //alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.devtools.ksp)
}

val isReleaseBuild =
    project.gradle.startParameter.taskNames.any { it.lowercase().contains("release") }

// Primeiro, crie uma função para carregar as propriedades do arquivo local.properties
fun getLocalProperty(key: String, project: Project): String? {
    // Localiza o arquivo local.properties na raiz do projeto
    val propertiesFile = File(project.rootDir, "local.properties")

    // Verifica se o arquivo existe
    if (propertiesFile.exists()) {
        // Cria uma instância de Properties e carrega o arquivo
        val properties = Properties().apply {
            load(propertiesFile.inputStream())
        }
        // Retorna o valor associado à chave fornecida
        return properties.getProperty(key)
    }
    return null
}

android {

    namespace = "com.esoft.emobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.esoft.emobile"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "APP_VERSION", "\"$versionName\"")
    }

    ndkVersion = "27.0.12077973"

    if (isReleaseBuild) {
        externalNativeBuild {
            cmake {
                path = file("CMakeLists.txt")
                version = "3.22.1"
            }
        }
    }

    flavorDimensions.add("environment")

    productFlavors {
        create("dev") {
            dimension = "environment"
            buildConfigField("String", "ENVIRONMENT", "\"dev\"")
            buildConfigField("String", "BASE_URL", "\"${getLocalProperty("API_URL_DEV", project) ?: ""}\"")
        }

        create("prod") {
            dimension = "environment"
            buildConfigField("String", "ENVIRONMENT", "\"prod\"")
            buildConfigField("String", "BASE_URL", "\"${getLocalProperty("API_URL_PROD", project) ?: ""}\"")
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

            manifestPlaceholders["ALLOW_HTTP_TRAFFIC"] = false
            manifestPlaceholders["NETWORK_SECURITY_CONFIG"] = "@xml/network_config_http"
        }

        getByName("release") {
            manifestPlaceholders += mapOf()
            manifestPlaceholders["ALLOW_HTTP_TRAFFIC"] = true
            manifestPlaceholders["NETWORK_SECURITY_CONFIG"] = "@xml/network_config_https"

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")

            ndk {
                debugSymbolLevel = "SYMBOL_TABLE" // ou 'SYMBOL_TABLE' ou 'NONE'
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(fileTree("libs") { include("*.jar") })
    implementation(files("libs/Sewoo_Android_1113.jar"))

    /* CORE */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    /* Icons*/
    implementation(libs.material.icons.extended)

    /* Text Recognition and Document Scanner */
    implementation(libs.mlkit.text.recognition)

    /* App Update */
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    /* Timber (logging)*/
    implementation(libs.timber)

    /* Lifecycle ViewModel Compose */
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    /* Hilt */
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    /* Datastore */
    implementation(libs.datastore.preferences)

    /* Memory leak */
    debugImplementation(libs.leakcanary.android)

    /* Retrofit */
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.moshi)

    /* Okhttp */
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)


    /* Moshi */
    ksp(libs.moshi.kotlin.codegen)
    //implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)

    /* Coroutines */
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    /* Permissions */
    implementation(libs.accompanist.permissions)

    /* Barcode Scanning */
    implementation(libs.barcode.scanning)

    /* CameraX */
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)

    /* Navigation */
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.compose.navigation)

    /* Splash Screen */
    implementation(libs.androidx.core.splashscreen)

}