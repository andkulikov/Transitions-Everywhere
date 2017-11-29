Transitions Everywhere
============
Backport of [Android Transitions API][1]. Animations backported to <b>Android 4.0+</b>. API compatible with <b>Android 2.3+</b>

About
============
[Article about transitions and library. More info here][2]<br>
[Russian version][3]<br>
Chinese: [version 1][5], [version 2][6]<br>

[Article about Support Library for Transitions. Overview and comparison with Transitions-Everywhere][7]

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
    compile "com.andkulikov:transitionseverywhere:1.7.8"
}
```
Use transition classes from package `com.transitionseverywhere.*` instead of `android.transition.*` from android framework Transitions API.<br>

Changelog
============

<b>1.7.8</b><br>
Fix for ChangeBounds sometimes not applying the latest values. Thanks to [lukaville][13]

<b>1.7.7</b><br>
Fix version resolving issue when use together with the latest suppport libs

<b>1.7.6</b><br>
Fix for the case when Visibility transition is removing the view from the previous scene. [Framework bug for it][12]

<b>1.7.4, 1.7.5</b><br>
Fixes for color change in ChangeText. Thanks to [droidluv][10] and [passsy][11]

<b>1.7.3</b><br>
Fix for TranslationTransition, fix for Visibility transition cancelation when it is using an overlay 

<b>1.7.1, 1.7.2</b><br>
Npe fix, WindowId backport, update PathParser version

<b>1.7.0</b><br>
Bug fix for rare NPE. Thanks to [TealOcean][9]

<b>1.6.9</b><br>
Bug fix for Scenes when we pass null transition

<b>1.6.8</b><br>
Bug fix for Recolor. Thanks to [twyatt][8]

<b>1.6.7</b><br>
Merge with Android 7.0. Some internal improvements

<b>1.6.5</b><br>
Optimizations for ChangeBounds and Fade

<b>1.6.4</b><br>
Bug fix. Thanks to [raycoarana][4]

<b>1.6.3</b><br>
Hidden transitions are moved in the main package. Proguard rules are removed. Some internal fixes.

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
[7]: https://medium.com/@andkulikov/support-library-for-transitions-overview-and-comparison-c41be713cf8c
[8]: https://github.com/twyatt
[9]: https://github.com/TealOcean
[10]: https://github.com/droidluv
[11]: https://github.com/passsy
[12]: https://issuetracker.google.com/issues/65688271
[13]: https://github.com/lukaville
