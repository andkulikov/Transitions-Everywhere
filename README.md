Transitions Everywhere
============
Set of extra Transitions on top of [AndroidX Transitions Library][1].

About
============
[Article about transitions and library][2]<br>
Originally this library was a full backport of Android Platform's Transitions API.
Then all the bug fixes from the library were ported into AndroidX Transitions (previously Support library).
Now this lib contains some transitions which are not a part of the official set:
1) Internal Transitions that was marked as @hide in framework: Recolor, Rotate, ChangeText and Crossfade.
2) Two extra transitions: Scale and Translation.
New minSdk version is 14 (Android 4.0 ICS).

Quick start
============
```groovy
dependencies {
    implementation "com.andkulikov:transitionseverywhere:2.0.0-alpha01"
}
```
This version based on <b>androidx.transition:transition:1.1.0-alpha01</b>.

Migration from 1.x guide
============
1) Migrate to <b>AndroidX</b>! Support libraries are not updating anymore, to get all the bug fixes you have to use AndroidX transitions.
2) Replace imports from <b>com.transitionseverywhere.*</b> to <b>androidx.transition.*</b> for all the classes which are a part of the AndroidX lib.
3) If you were using <b>TransitionListenerAdapter</b> class, now please use <b>Transition.TransitionListenerAdapter</b> now.
4) Instead of <b>TransitionManager.setTransitionName()</b> use <b>ViewCompat.setTransitionName()</b>.
5) If you were inflating transitions via xml move your files from <b>anim</b> folder to <b>transition</b> and use <b>android:</b> namespace instead of <b>app:</b>.
6) Some setters in AndroidX transitions are not following the builder pattern, please rewrite this usages with introducing a helper variable if you encounter the issue.
7) Instead of <b>TranslationTransition()/b> use <b>Translation/b>.

[Changelog for version 1.x][4]
============

Articles about the version 1.x
============
[Article about transitions and library][2]<br>
[Russian version][3]<br>
Chinese: [version 1][5], [version 2][6]<br>

[1]: https://developer.android.com/reference/androidx/transition/package-summary
[2]: https://medium.com/@andkulikov/animate-all-the-things-transitions-in-android-914af5477d50
[3]: http://habrahabr.ru/post/243363/
[4]: https://github.com/andkulikov/Transitions-Everywhere/blob/master/library(1.x)/CHANGELOG.md
[5]: https://yanlu.me/animate-all-the-things-transitions-in-android/
[6]: http://www.jianshu.com/p/98f2ec280945
[7]: https://medium.com/@andkulikov/support-library-for-transitions-overview-and-comparison-c41be713cf8c