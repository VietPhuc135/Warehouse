# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class com.example.warehousemanagement.other.SplashScreen {
   public *;
   protected *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-verbose
-dontoptimize
-dontshrink
-dontusemixedcaseclassnames
-keepparameternames
-keep class com.example.warehousemanagement.MainActivity
-keep class com.facebook.** { *; }
-keep class com.androidquery.** { *; }
-keep class com.google.** { *; }
-keep class org.acra.** { *; }
-keep class org.apache.** { *; }
-keep class com.mobileapptracker.** { *; }
-keep class com.nostra13.** { *; }
-keep class net.simonvt.** { *; }
-keep class android.support.** { *; }
-keep class com.nnacres.app.model.** { *; }
-keep class com.facebook.** { *; }
-keep class com.astuetz.** { *; }
-keep class twitter4j.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep class com.dg.libs.** { *; }
-keep class android.support.v4.** { *; }
-keep class com.bluetapestudio.templateproject.** { *; }
-keep class com.yourideatoreality.model.** { *; }
-keep interface com.yourideatoreality.model.** { *; }
-keep class com.bluetapestudio.** { *; }
-keep interface com.bluetapestudio.** { *; }
# Suppress warnings if you are NOT using IAP:
-dontwarn com.nnacres.app.**
-dontwarn com.androidquery.**
-dontwarn com.google.**
-dontwarn org.acra.**
-dontwarn org.apache.**
-dontwarn com.mobileapptracker.**
-dontwarn com.nostra13.**
-dontwarn net.simonvt.**
-dontwarn android.support.**
-dontwarn com.facebook.**
-dontwarn twitter4j.**
-dontwarn com.astuetz.**
-dontwarn com.actionbarsherlock.**
-dontwarn com.dg.libs.**
-dontwarn  com.bluetapestudio.templateproject.**

-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class com.google.gson.stream.** { *; }

# The official support library.
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.example.warehousemanagement.other.SplashScreen
#  Library JARs.
#-keep class de.greenrobot.dao.** { *; }
#-keep interface de.greenrobot.dao.** { *; }
## Library projects.
#-keep class com.actionbarsherlock.** { *; }
#-keep interface com.actionbarsherlock.** { *; }
##Keep native
#-keepclasseswithmembernames class * {
#    native <methods>;
#}

-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn com.squareup.okhttp.**