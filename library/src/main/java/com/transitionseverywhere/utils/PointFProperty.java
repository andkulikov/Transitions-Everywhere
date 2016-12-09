/*
 * Copyright (C) 2015 Andrey Kulikov (andkulikov@gmail.com)
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
package com.transitionseverywhere.utils;

import android.annotation.TargetApi;
import android.graphics.PointF;
import android.os.Build;
import android.util.Property;

/**
 * Created by Andrey Kulikov on 15.08.15.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public abstract class PointFProperty<T> extends Property<T, PointF> {

    public PointFProperty() {
        super(PointF.class, null);
        // null instead of name here because it's used only for calling setter
        // and getter via reflection. but we have our own overridden set and get.
    }

    /**
     * Just default realisation. Some of properties can have no getter. Override for real getter
     */
    @Override
    public PointF get(T object) {
        return null;
    }

}
