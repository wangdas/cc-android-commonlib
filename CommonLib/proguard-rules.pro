# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html


## normal

# 四个过程
-dontshrink                                # 不进行压缩操作，默认情况下，除了-keep配置（下详）的类及其直接或间接引用到的类，都会被移除。
-dontoptimize                              # 不对class进行优化，默认优化。由于优化会进行类合并、内联等多种优化，-applymapping可能无法完全应用，需使用热修复的应用，建议使用此配置关闭优化。
#-dontobfuscate                             # 不对代码名称进行混淆，这样类名称就不会混淆了，默认是混淆，sdk最好混淆
#-dontpreverify                             # 不对 class 进行预校验，默认校验，校验 StackMap /StackMapTable 属性。android不需要进行此步骤，可以加快混淆速度

-optimizationpasses 5                      # 指定压缩级别
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*   # 混淆时采用的算法(google推荐，一般不改变)

-useuniqueclassmembernames                 # 把混淆类中的方法名也混淆了
-dontskipnonpubliclibraryclasses           # 不路过引用库中的非public类
-dontskipnonpubliclibraryclassmembers      # 不跳过非公共的库的类成员
#-allowaccessmodification                   # 优化时允许访问并修改有修饰符的类和类的成员，此项会将混淆文件所在路径全部混淆。
#-ignorewarnings                            # 指定输出所有警告信息，但继续进行混淆。同上一选项，慎用。
#-dontwarn                                  # 指定一组类，不警告这些类中找不到引用或其它重要的问题。这个选项是很危险的，比如，找不到引用的错误可能导致代码不能正常work。

-keepattributes Exceptions                 # 不混淆异常
-keepattributes InnerClasses               # 不混淆内部类
-keepattributes Signature                  # 不混淆泛型
-keepattributes EnclosingMethod            # 不混淆反射
-keepattributes *Annotation*               # 不混淆注解
#-keepattributes Deprecated                 # 不混淆废弃类
#-keepattributes Synthetic                  #
#-keepattributes LocalVariable*Table        #
-keepattributes SourceFile,LineNumberTable # 抛出异常时保留代码和行号
#-keepattributes *JavascriptInterface*      # 不混淆JS调用java的方法
#-renamesourcefileattribute SourceFile       # 将文件来源重命名为“SourceFile”字符串

## normal


## 业务混淆

# 枚举类 混淆规则
-keepclassmembers enum  * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
  }

# ApplicationData 混淆规则
-keep class com.bokecc.common.application.ApplicationData {
    public *;
}
-keep class com.bokecc.common.application.BusinessType {
    public *;
}

# 工具类混淆规则
-keep class com.bokecc.common.utils.** {*;}

# CCLogManager 混淆规则
-keep class com.bokecc.common.log.CCLogManager {
    public *;
    protected *;
}

-keep class com.bokecc.common.log.CCClassLogManager {
    public *;
    protected *;
}
-keep class com.bokecc.common.log.CCLiveLogManager {
    public *;
    protected *;
}
-keep class com.bokecc.common.log.CCVodLogManager {
    public *;
    protected *;
}

-keep class com.bokecc.common.log.CCLogRequestCallback {
    public *;
    protected *;
}

# CCCrashManager 混淆规则
-keep class com.bokecc.common.crash.CCCrashManager {
    public *;
    protected *;
}

# BaseRequest 混淆规则
-keep class com.bokecc.common.http.BaseRequest {
    public *;
    protected *;
}
-keep class com.bokecc.common.http.listener.RequestListener {
    public *;
    protected *;
}
-keep class com.bokecc.common.http.listener.DownloadListener {
    public *;
    protected *;
}
# CCBaseSocket 混淆规则
-keep class com.bokecc.common.socket.CCBaseSocket {
    public *;
    protected *;
}
-keep class com.bokecc.common.socket.Listener {
    public *;
    protected *;
}
-keep class com.bokecc.common.socket.emitter.Emitter {
    public *;
    protected *;
}
-keep class com.bokecc.common.socket.CCSocketCallback {
    public *;
    protected *;
}

# activity和dialog
-keep class com.bokecc.common.base.CCBaseActivity {
    public *;
    protected *;
}
-keep class com.bokecc.common.dialog.** {
    public *;
    protected *;
}

# xlog混淆规则
-keep class com.bokecc.xlog.** {
  public protected private *;
}

# socketIO-0.8.3 混淆规则
-keep class com.bokecc.socket.** {*;}
-keep class com.bokecc.json.** {*;}
# okhttp 混淆规则
-keep class com.bokecc.okhttp.** {*;}
-keep class com.bokecc.okio.** {*;}


# native方法 混淆规则
-keepclasseswithmembers class * {
    native <methods>;
 }

# 流相关基础对象和基类不混淆
-keep class com.bokecc.common.stream.** {*;}
-keep class com.bokecc.stream.** {*;}


## 业务混淆

