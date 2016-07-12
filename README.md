Transitions Everywhere
============
Backport of [Android Transitions API][1]. Animations backported to <b>Android 4.0+</b>. API compatible with <b>Android 2.2+</b>

About
============
[Article about transitions and library. More info here][2]<br>
[Russian version][3]<br>
[Chinese version][5]<br>
[Article about transitions and this library, Chinese version][6]<br>

Simple example
============
```java
TransitionManager.beginDelayedTransition(transitionsContainer);
text.setVisibility(visible ? View.VISIBLE : View.GONE);      
```
<img src="https://habrastorage.org/files/c51/b1e/b26/c51b1eb26fb941698ad5a1368d06603b.gif"/>
<br>[Article][2] and sample application contain a lot of examples how to use transitions.

Quick start
============
```groovy
dependencies {
    compile "com.andkulikov:transitionseverywhere:1.6.5"
}
```
Use transition classes from package `com.transitionseverywhere.*` instead of `android.transition.*` from android framework Transitions API.<br>

Changelog
============
<b>1.6.5</b><br>
Optimizations for ChangeBounds and Fade

<b>1.6.4</b><br>
Bug fix. Thanks to [raycoarana][4].

<b>1.6.3</b><br>
Hidden transitions is moved in main package. Proguard rules are removed. Some internal fixes.

<b>1.6.2</b><br>
Fixed issue with incorrect disappearing when set of more than one Visibility transitions animates the same view
<br>Added two "extra" transitions: Scale (for scaled appearing & disappearing) and TranslationTransition (animates changes of translationX and translationY)

<b>1.6.0</b><br>
Merge with final Android Marshmallow SDK<br>
PathMotion aka Curved motion is backported!<br>
Bug fixes and performance optimizations.

[1]: http://developer.android.com/reference/android/transition/package-summary.html
[2]: https://medium.com/@andkulikov/animate-all-the-things-transitions-in-android-914af5477d50
[3]: http://habrahabr.ru/post/243363/
[4]: https://github.com/raycoarana
[5]: https://yanlu.me/animate-all-the-things-transitions-in-android/
[6]: http://www.jianshu.com/p/98f2ec280945