/*
 * Copyright (C) 2015 Andrey Kulikov (andkulikov@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;

/**
 * Dummy class. Permits to extend same hidden class from android framework.
 * Actually in runtime will be used class from android framework and ObjectAnimator
 * optimizations for IntProperty will be applied.
 *
 * Created by Andrey Kulikov on 18.08.15.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public abstract class IntProperty<T> extends Property<T, Integer> {

    public IntProperty(String name) {
        super(Integer.class, name);
    }

    public abstract void setValue(T object, int value);

    @SuppressLint("NewApi")
    @Override
    final public void set(T object, Integer value) {
        setValue(object, value);
    }

}
