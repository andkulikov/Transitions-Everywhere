Transitions Everywhere
============
Set of extra Transitions on top of [AndroidX Transitions Library][1].

About
============
[Article about transitions and library][2]<br>
Originally this library was a full backport of Android Platform's Transitions API.<br>
Then all the bug fixes from the library were ported into AndroidX Transitions (previously Support library).<br>
Now this lib has minSdk version <b>14</b> (Android 4.0 ICS) and consist of some transitions which are not a part of the official set:
1) Internal Transitions that was marked as @hide in the platform: <b>Recolor</b>, <b>Rotate</b>, <b>ChangeText</b> and <b>Crossfade</b>.
2) Two extra transitions: <b>Scale</b> and <b>Translation</b>.<br><br>

Quick start
============

This version should be used if you are specifying 29 (Q) as a `targetSdkVersion`:

```groovy
dependencies {
    implementation "com.andkulikov:transitionseverywhere:2.1.0"
}
```
Otherwise, if you specify 29 as `targetSdkVersion` some of the transitions will not work properly. Instead of the reflection calls this version uses the new public methods added in API Level 29. It is based on <b>androidx.transition:transition:1.2.0</b>.

Previous version if you are not yet on 29 (Q) SDK:

```groovy
dependencies {
    implementation "com.andkulikov:transitionseverywhere:2.0.0"
}
```
This version is based on <b>androidx.transition:transition:1.1.0</b>.

Migration from 1.x guide
============
1) Migrate to <b>AndroidX</b>! Support libraries are not updating anymore, to get all the bug fixes you have to use AndroidX transitions.
2) Replace imports from <b>com.transitionseverywhere.</b> to <b>androidx.transition.</b> for all the classes which are a part of the AndroidX lib.
3) If you were using <b>Transition.TransitionListenerAdapter</b> class use <b>TransitionListenerAdapter</b> now.
4) Instead of <b>TransitionManager.setTransitionName()</b> use <b>ViewCompat.setTransitionName()</b>.
5) If you were inflating transitions via xml move your files from <b>anim</b> folder to <b>transition</b> and use <b>android:</b> namespace instead of <b>app:</b>
6) Some setters in AndroidX transitions are not following the builder pattern, please rewrite this usages with introducing a helper variable if you encounter the issue.
7) Instead of <b>TranslationTransition</b> use <b>Translation</b>.

Articles about the version 1.x
============
[Article about transitions and library][2]<br>
[Russian version][3]<br>
Chinese: [version 1][5], [version 2][6]<br>

[Changelog for version 1.x][4]
============

[1]: https://developer.android.com/reference/androidx/transition/package-summary
[2]: https://medium.com/@andkulikov/animate-all-the-things-transitions-in-android-914af5477d50
[3]: http://habrahabr.ru/post/243363/
[4]: https://github.com/andkulikov/Transitions-Everywhere/blob/master/library(1.x)/CHANGELOG.md
[5]: https://yanlu.me/animate-all-the-things-transitions-in-android/
[6]: http://www.jianshu.com/p/98f2ec280945
[7]: https://medium.com/@andkulikov/support-library-for-transitions-overview-and-comparison-c41be713cf8c
