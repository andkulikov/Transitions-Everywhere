Change log for 1.x versions
============

<b>1.8.1</b><br>
Bug fixes for Crossfade and Recolor transitions. Thanks to [evant][8] and [DummyCo][9]

<b>1.8.0</b><br>
Provide @NonNull and @Nullable annotations for all the methods to make the library more Kotlin friendly

<b>1.7.9</b><br>
Fix bug with typo in restoring paused Visibility transition when it is using an overlay

<b>1.7.8</b><br>
Fix for ChangeBounds sometimes not applying the latest values. Thanks to [lukaville][7]

<b>1.7.7</b><br>
Fix version resolving issue when use together with the latest suppport libs

<b>1.7.6</b><br>
Fix for the case when Visibility transition is removing the view from the previous scene. [Framework bug for it][6]

<b>1.7.4, 1.7.5</b><br>
Fixes for color change in ChangeText. Thanks to [droidluv][4] and [passsy][5]

<b>1.7.3</b><br>
Fix for TranslationTransition, fix for Visibility transition cancelation when it is using an overlay 

<b>1.7.1, 1.7.2</b><br>
Npe fix, WindowId backport, update PathParser version

<b>1.7.0</b><br>
Bug fix for rare NPE. Thanks to [TealOcean][3]

<b>1.6.9</b><br>
Bug fix for Scenes when we pass null transition

<b>1.6.8</b><br>
Bug fix for Recolor. Thanks to [twyatt][2]

<b>1.6.7</b><br>
Merge with Android 7.0. Some internal improvements

<b>1.6.5</b><br>
Optimizations for ChangeBounds and Fade

<b>1.6.4</b><br>
Bug fix. Thanks to [raycoarana][1]

<b>1.6.3</b><br>
Hidden transitions are moved in the main package. Proguard rules are removed. Some internal fixes.

<b>1.6.2</b><br>
Fixed issue with incorrect disappearing when set of more than one Visibility transitions animates the same view
<br>Added two "extra" transitions: Scale (for scaled appearing & disappearing) and TranslationTransition (animates changes of translationX and translationY)

<b>1.6.0</b><br>
Merge with final Android Marshmallow SDK<br>
PathMotion aka Curved motion is backported!<br>
Bug fixes and performance optimizations.

<b>1.5.0</b><br>
Merge with Android M Preview 2<br>
Migrate to <b>new library package name</b> and <b>maven artifact id</b>:<br>
Please update imports in your classes from `android.transitions.everywhere.*` to `com.transitionseverywhere`<br>
And gradle dependency from `com.github.andkulikov:transitions-everywhere` to `com.andkulikov:transitionseverywhere`<br>

<b>1.4.0</b><br>
Merge with Android M Preview sources.

<b>1.3.1 - 1.3.2</b><br>
Bug fixes.

<b>1.3.0</b><br>
Merge with changes from Android 5.1.

<b>1.2.0 - 1.2.2</b><br>
Bug fixes.

<b>1.1.0</b><br>
Port of new transitions from <b>Android 5.0 Lollipop</b>

<b>1.0.0</b><br>
First release. This library is a backport of [Android Transitions API][10]. Animations backported to <b>Android 4.0+</b>. API compatible with <b>Android 2.3+</b>

[1]: https://github.com/raycoarana
[2]: https://github.com/twyatt
[3]: https://github.com/TealOcean
[4]: https://github.com/droidluv
[5]: https://github.com/passsy
[6]: https://issuetracker.google.com/issues/65688271
[7]: https://github.com/lukaville
[8]: https://github.com/evant
[9]: https://github.com/DummyCo
[10]: http://developer.android.com/reference/android/transition/package-summary.html
