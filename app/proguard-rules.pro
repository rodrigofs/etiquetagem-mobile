# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Manter anotações
-keepattributes *Annotation*

# Não ofuscar classes anotadas com @Keep
-keep @androidx.annotation.Keep class *

# Não ofuscar classes do Hilt
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.**

# Não ofuscar classes do Retrofit e Moshi
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

# Não ofuscar classes do Room
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# Manter classes anotadas com @FormField
-keep @com.esoft.emobile.ui.components.form.FormField class * { *; }

# Manter métodos de classes criadas dinamicamente (evitar problemas com reflexão)
-keep class kotlin.reflect.jvm.internal.KClassImpl { *; }

# Timber
-assumenosideeffects class timber.log.Timber$LogTree {
    public *;
}

# Regras específicas para Jetpack Compose
# Manter todas as classes do Jetpack Compose
-keep class androidx.compose.** { *; }

# Manter lambdas e seus métodos de composição no Compose
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable <methods>;
}

# Manter o runtime do Compose intacto
-keep class androidx.compose.runtime.** { *; }

# Manter classes relacionadas a animações no Compose
-keep class androidx.compose.animation.** { *; }

# Manter classes relacionadas ao layout no Compose
-keep class androidx.compose.ui.layout.** { *; }

# Regras para evitar problemas na validação e exibição de erros
# Manter classes de validação
-keep class com.esoft.emobile.ui.components.form.validators.** { *; }

# Manter classes de estado de campo (FieldState)
-keep class com.esoft.emobile.ui.components.form.FieldState { *; }

# Manter classes de formulários (Form)
-keep class com.esoft.emobile.ui.components.form.Form { *; }

# Manter todos os campos de variáveis (evita remoção de campos usados por reflexão)
-keepclassmembers class * {
    <fields>;
}

# Manter todas as classes e métodos em classes que implementam interfaces do Compose
-keep class ** implements androidx.compose.runtime.Composer { *; }

# Manter classes de logs, caso use o Timber ou outra ferramenta de logging
-keep class timber.log.Timber { *; }
