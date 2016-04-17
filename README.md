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
<br>Sample application contain a lot of examples how to use transitions.

Usage
============
Gradle:
```groovy
dependencies {
    compile "com.andkulikov:transitionseverywhere:1.6.3"
}
```
Use transition classes from package `com.transitionseverywhere.*` instead of `android.transition.*` from android framework Transitions API.<br>

Changelog
============
<b>1.6.3</b><br>
Hidden transitions is moved in main package. Proguard rules are removed. Some internal fixes.

<b>1.6.2</b><br>
Fixed issue with incorrect disappearing when set of more than one Visibility transitions animates the same view
<br>Added two "extra" transitions: Scale (for scaled appearing & disappearing) and TranslationTransition (animates changes of translationX and translationY)

<b>1.6.0</b><br>
Merge with final Android Marshmallow SDK<br>
PathMotion aka <b>Curved motion is backported</b>! [What is it and how to use it][6]<br>
Bug fixes and performance optimizations.

Transition names of views
============
Android 5.0 adds new method `setTransitionName()` for `View` class. With this library you should call `TransitionManager.setTransitionName(View v, String transitionName)` method instead to provide backward compatibility.

Transitions via XML
============
If you use XML files to create your transitions you need to put them in the res/anim folder instead of the res/transition folder. You need to use application attributes namespase instead of `android:`. For example:
```xml
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

About library [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-transitions--everywhere-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1050)
============
Transition animations backported to <b>Android 4.0+</b>.<br>
For Android ver. <b>>= 2.2</b> and < <b>4.0</b> scene to scene (layout to layout) changes is executed by the same API but without animations.

Thanks to github users: <b>[pardom][4]</b> and <b>[guerwan][5]</b>  

[1]: http://developer.android.com/reference/android/transition/package-summary.html
[2]: https://www.youtube.com/watch?v=S3H7nJ4QaD8
[3]: http://habrahabr.ru/post/243363/
[4]: https://github.com/pardom/TransitionSupportLibrary
[5]: https://github.com/guerwan/TransitionsBackport
[6]: http://blog.stylingandroid.com/curved-motion-part-1
