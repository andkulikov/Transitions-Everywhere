/*
 * Copyright (C) 2014 Andrey Kulikov (andkulikov@gmail.com)
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
import android.graphics.Matrix;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

/**
 * Created by Andrey Kulikov on 20.10.14.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ViewUtilsLollipop extends ViewUtils.ViewUtilsKitKat {

    @Nullable
    private static final Class CLASS_GhostView = ReflectionUtils.getClass("android.view.GhostView");
    @Nullable
    private static final Method METHOD_addGhost = ReflectionUtils.getMethod(CLASS_GhostView,
            "addGhost", View.class, ViewGroup.class, Matrix.class);
    @Nullable
    private static final Method METHOD_removeGhost = ReflectionUtils.getMethod(CLASS_GhostView,
            "removeGhost", View.class);
    @Nullable
    private static final Method METHOD_transformMatrixToGlobal =
            ReflectionUtils.getMethod(View.class, "transformMatrixToGlobal", Matrix.class);
    @Nullable
    private static final Method METHOD_transformMatrixToLocal =
            ReflectionUtils.getMethod(View.class, "transformMatrixToLocal", Matrix.class);
    @Nullable
    private static final Method METHOD_setAnimationMatrix =
            ReflectionUtils.getMethod(View.class, "setAnimationMatrix", Matrix.class);

    @Override
    public void transformMatrixToGlobal(@NonNull View view, @NonNull Matrix matrix) {
        ReflectionUtils.invoke(view, null, METHOD_transformMatrixToGlobal, matrix);
    }

    @Override
    public void transformMatrixToLocal(@NonNull View view, @NonNull Matrix matrix) {
        ReflectionUtils.invoke(view, null, METHOD_transformMatrixToLocal, matrix);
    }

    @Override
    public void setAnimationMatrix(@NonNull View view, @Nullable Matrix matrix) {
        ReflectionUtils.invoke(view, null, METHOD_setAnimationMatrix, matrix);
    }

    @Override
    @Nullable
    public View addGhostView(@NonNull View view, @NonNull ViewGroup viewGroup, @Nullable Matrix matrix) {
        return (View) ReflectionUtils.invoke(null, null, METHOD_addGhost, view, viewGroup, matrix);
    }

    @Override
    public void removeGhostView(@NonNull View view) {
        ReflectionUtils.invoke(view, null, METHOD_removeGhost, view);
    }

    @Override
    public void setTransitionName(@NonNull View v, @Nullable String name) {
        v.setTransitionName(name);
    }

    @Override
    @Nullable
    public String getTransitionName(@NonNull View v) {
        return v.getTransitionName();
    }

    @Override
    public float getTranslationZ(@NonNull View view) {
        return view.getTranslationZ();
    }

    @Override
    public void setTranslationZ(@NonNull View view, float z) {
        view.setTranslationZ(z);
    }
}

