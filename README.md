Transitions Everywhere
============
Backport of [Transitions API from Android 4.4][1]. Compatible with <b>Android 2.2+</b>.

About Transitions API
============
[Video - DevBytes: Android 4.4 Transitions][2]<br>
[Sample project from Google][3] 

Cooming soon
============
Port of new transitions from Android 5.0 Lolipop

Simple example
============
<img src="http://www.doubleencore.com/wp-content/uploads/2013/11/transitionSample.gif"/>

Usage
============
Gradle:
```
dependencies {
    compile "com.github.andkulikov:transitions-everywhere:1.0.0"
}
```
Use transition classes from package `android.support.transition.*` instead of `android.transition.*` from 4.4 Transitions API.

About library
============
Transition animations backported to <b>Android 3.0</b>.<br>
For Android ver. <b>>= 2.2</b> and < <b>3.0</b> scene to scene (layout to layout) changes is executed by the same API  but without animations.

<b>Note:</b> some of transitions classes was marked as hidden by developers of Android. You can find it in package  `android.support.transition.hidden`. But i don't recommend to use them because they can work unstable.

Thanks to github users: <b>[pardom][4]</b> and <b>[guerwan][5]</b>  

[1]: http://developer.android.com/reference/android/transition/package-summary.html
[2]: https://www.youtube.com/watch?v=S3H7nJ4QaD8
[3]: https://developer.android.com/samples/BasicTransition/index.html
[4]: https://github.com/pardom/TransitionSupportLibrary
[5]: https://github.com/guerwan/TransitionsBackport

<br>
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-transitions--everywhere-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1050)
