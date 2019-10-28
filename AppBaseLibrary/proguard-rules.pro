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

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable { *; }
# 保持 Serializable 不被混淆
-keep class * implements java.io.Serializable { *; }
#enum
-keepclassmembers enum * { *;}
#webview
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

#---start 混淆OkGo------------------------------------
#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
-keep class okio.**{*;}
#---end 混淆OkGo------------------------------------


#---start 混淆immersionbar------------------------------------
 -keep class com.gyf.immersionbar.* {*;}
 -dontwarn com.gyf.immersionbar.**
#---end 混淆immersionbar------------------------------------

-keep public class * extends com.google.android.material.bottomnavigation.BottomNavigationView { *; }
-keep public class * extends com.google.android.material.bottomnavigation.BottomNavigationMenuView { *; }
-keep public class * extends com.google.android.material.bottomnavigation.BottomNavigationPresenter { *; }
-keep public class * extends com.google.android.material.bottomnavigation.BottomNavigationItemView { *; }

#---start 混淆glide------------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#---end 混淆glide------------------------------------


#---start 混淆banner------------------------------------
-keep class com.youth.banner.** {
    *;
 }
#---end 混淆banner------------------------------------


#---start 混淆databinding------------------------------------
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
#---end 混淆databinding------------------------------------
