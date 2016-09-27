# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-ignorewarnings
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-dontoptimize
-dontobfuscate
-dontpreverify
-keepattributes *Annotation*

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.FragmentActivity
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keep class android.webkit.** {*;}
-keep class cn.sharesdk.** {*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-keep class com.sina.**{*;}
-keep class org.json.** {*;}
#-keep class com.google.gson.** {*;}
-keep class sun.misc.** { *; }

#极光推送
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}

-keep class tv.kuainiu.modle.event.**{*;}
-keep class tv.kuainiu.modle.** {*;}
-keep class tv.kuainiu.ui.liveold.modle.** {*;}

# Retain generated class which implement ViewBinder.
#-keep public class * implements butterknife.internal.ViewBinder { public <init>(); }
#
## Prevent obfuscation of types which use ButterKnife annotations since the simple name
## is used to reflectively look up the generated ViewBinder.
#-dontwarn butterknife.internal.**
#-keep class **$$ViewInjector { *; }
#-keepnames class * { @butterknife.InjectView *;}
#-keepnames class * { @butterknife.* *; }

#Butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
@butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
@butterknife.* <methods>;
}
#
#-dontwarn  com.easefun.polyvsdk.**
#-keep class com.easefun.polyvsdk.**{*;}					# 保持哪些类不被混淆
-keep class tv.danmaku.ijk.media.**{*;}					# 保持哪些类不被混淆
-keep public class tv.danmaku.ijk.media.player.IjkMediaPlayer {*;} # 保持这个类的类变量不被混淆
-keep @interface tv.danmaku.ijk.media.**{*;}			# 保持哪些类不被混淆
-keep class com.nostra13.universalimageloader.**{*;}	# 保持哪些类不被混淆
-keep class org.apache.http.**{*;}						# 保持哪些类不被混淆
-keep class org.apache.commons.**{*;}					# 保持哪些类不被混淆
#-keep public class com.tencent.bugly.**{*;}				# 保持哪些类不被混淆


-dontwarn org.joda.time.**
-keep class org.joda.time.** { *;}

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**

#share sdk
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn **.R$*
-keep class m.framework.**{*;}
#share sdk

#event bus--------------------------------
#参考：http://greenrobot.org/eventbus/documentation/proguard/
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#event bus--------------------------------------------

#友盟
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
#友盟

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers class * extends android.support.v7.app.AppCompatActivity {
   public void *(android.view.View);
}
-keepclassmembers class * {

   @android.webkit.JavascriptInterface <methods>;

}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}

-keepclassmembers class **.R$* {
    public static <fields>;
}
-dontwarn com.bumptech.glide.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-dontwarn android.support.**

#保留源文件和行号的信息
-keepattributes SourceFile,LineNumberTable

