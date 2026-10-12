package com.javaacademy.spendly.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.function.Function;

/** A small, explicit version of {@code Transformations.map}, built on {@link MediatorLiveData}. */
public final class LiveDataTransformations {

    private LiveDataTransformations() { }

    public static <X, Y> LiveData<Y> map(LiveData<X> source, Function<X, Y> mapper) {
        MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, value -> result.setValue(value == null ? null : mapper.apply(value)));
        return result;
    }
}
