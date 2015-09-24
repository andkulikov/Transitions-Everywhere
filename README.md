Transitions Everywhere
============
Backport of [Android Transitions API][1]. Animations backported to <b>Android 4.0+</b>. API compatible with <b>Android 2.2+</b>

About Transitions API
============
[Video - DevBytes: Android 4.4 Transitions][2]<br>
[Article about transitions and library (in Russian language)][3]

Simple example
============
<img src="http://habrastorage.org/getpro/habr/post_images/e93/37c/0da/e9337c0dacc355523adddf1545b57e5a.gif"/>

Usage
============
Gradle:
```
dependencies {
    compile "com.andkulikov:transitionseverywhere:1.6.0"
}
```
Use transition classes from package `com.transitionseverywhere.*` instead of `android.transition.*` from android framework Transitions API.<br>

Changelog
============
<b>1.6.0</b><br>
Merge with final Android Marshmallow SDK<br>
PathMotion aka <b>Curved motion is backported</b>! [What is it and how to use it][6]<br>
Bug fixes and performance optimizations. Animations now works on Android 4.0+ (instead of 3.1+ in previous version)

<b>1.5.0</b><br>
Merge with Android M Preview 2<br>
Migrate to <b>new library package name</b> and <b>maven artifact id</b>:<br>
Please update imports in your classes<br>
from `android.transitions.everywhere.*` to `com.transitionseverywhere`<br>
And gradle dependency<br>
from `com.github.andkulikov:transitions-everywhere` to `com.andkulikov:transitionseverywhere`<br>

Transitions via XML
============
If you use XML files to create your transitions you need to put them in the res/anim folder instead of the res/transition folder. You need to use application attributes namespase instead of `android:`. For example:
```
<transitionSet xmlns:app="http://schemas.android.com/apk/res-auto"
               app:duration="400">
    <changeBounds/>
    <fade app:fadingMode="fade_in">
        <targets>
            <target app:targetId="@id/transition_title"/>
        </targets>
    </fade>
</transitionSet>
```

Transition names of views
============
Android 5.0 adds new method `setTransitionName()` for `View` class. With this library you should call `TransitionManager.setTransitionName(View v, String transitionName)` method instead to provide backward compatibility.

About library
============
Transition animations backported to <b>Android 4.0+</b>.<br>
For Android ver. <b>>= 2.2</b> and < <b>4.0</b> scene to scene (layout to layout) changes is executed by the same API  but without animations.

<b>Note:</b> some of transitions classes was marked as hidden by developers of Android. You can find it in package  `com.transitionseverywhere.hidden`.

Thanks to github users: <b>[pardom][4]</b> and <b>[guerwan][5]</b>  

ProGuard configs
============
```
-keep class com.transitionseverywhere.** { *; }
-keep class com.transitionseverywhere.**.** { *; }
```

[1]: http://developer.android.com/reference/android/transition/package-summary.html
[2]: https://www.youtube.com/watch?v=S3H7nJ4QaD8
[3]: http://habrahabr.ru/post/243363/
[4]: https://github.com/pardom/TransitionSupportLibrary
[5]: https://github.com/guerwan/TransitionsBackport
[6]: http://blog.stylingandroid.com/curved-motion-part-1

<br>
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-transitions--everywhere-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1050)
