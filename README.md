Transitions Everywhere
============
Backport of [Transitions API from Android 4.4][1]. Compatible with <b>Android 2.2+</b>.

About Transitions API
============
[Video - DevBytes: Android 4.4 Transitions][2]<br>
[Sample project from Google][3] 

Usage
============
Add in build.gradle:
```
dependencies {
    compile "com.github.andkulikov:transitions-everywhere:1.0.0"
}
```
And now you need to use classes from package `android.support.transition.XXX`, as against `android.transition.XXX` from 4.4 Transitions API.

About library
============
Transitions animations backported to <b>Android 3.0</b>.<br>
For Android versions <b>>= 2.2</b> and < <b>3.0</b> scene to scene layout changes works correctly with the same API just without animations.

<b>Note:</b> some of transitions classes was marked as hidden by developers of Android. You can find those classes in package `android.support.transition.hidden`. But i don't recommend to use them because they can work unstable.

[1]: http://developer.android.com/reference/android/transition/package-summary.html
[2]: https://www.youtube.com/watch?v=S3H7nJ4QaD8
[3]: https://developer.android.com/samples/BasicTransition/index.html
