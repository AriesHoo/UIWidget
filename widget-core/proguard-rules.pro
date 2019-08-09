# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lihui/work/AndroidStudio/sdk/tools/proguard/proguard-android.txt
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
-keep public class com.aries.ui.helper.alpha**
-keep public class com.aries.ui.helper.navigation**
-keep public class com.aries.ui.helper.status**
-keep public class com.aries.ui.impl.ActivityLifecycleCallbacksImpl
-keep public class com.aries.ui.util**
-keep public class com.aries.ui.view.alpha**
-keep public class com.aries.ui.view.nested**
-keep public class com.aries.ui.view.radius**
-keep public class com.aries.ui.view.title**
-keep public class com.aries.ui.view.DragLayout
-keep public class com.aries.ui.view.ObservableScrollView
-keep public class com.aries.ui.view.ObservableWebView
-keep public class com.aries.ui.widget.action.sheet**
-keep public class com.aries.ui.widget.i**
-keep public class com.aries.ui.widget.progress**
-keep public class com.aries.ui.widget.BasisDialog