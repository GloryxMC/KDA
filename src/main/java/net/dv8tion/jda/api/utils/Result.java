/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion.jda.api.utils;

import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a computation or task result.
 * <br>This result may be a {@link #getFailure() failure} or {@link #get() success}.
 *
 * <p>This is a <b>value type</b> and does not implement {@link #equals(Object)} or {@link #hashCode()}!
 *
 * @param <T>
 *        The success type
 *
 * @since  4.2.1
 */
public class Result<T>
{
    private final T value;
    private final Throwable error;

    private Result(T value, Throwable error)
    {
        this.value = value;
        this.error = error;
    }

    /**
     * Creates a successful result.
     *
     * @param  value
     *         The success value
     * @param  <E>
     *         The success type
     *
     * @return Result
     */
    @NotNull
    @CheckReturnValue
    public static <E> Result<E> success(@Nullable E value)
    {
        return new Result<>(value, null);
    }

    /**
     * Creates a failure result.
     *
     * @param  error
     *         The failure throwable
     * @param  <E>
     *         The success type
     *
     * @throws IllegalArgumentException
     *         If the provided error is null
     *
     * @return Result
     */
    @NotNull
    @CheckReturnValue
    public static <E> Result<E> failure(@NotNull Throwable error)
    {
        Checks.notNull(error, "Error");
        return new Result<>(null, error);
    }

    /**
     * Creates a result instance from the provided supplier.
     * <br>If the supplier throws an exception, a failure result is returned.
     *
     * @param  supplier
     *         The supplier
     * @param  <E>
     *         The success type
     *
     * @throws IllegalArgumentException
     *         If the supplier is null
     *
     * @return Result instance with the supplied value or exception failure
     */
    @NotNull
    @CheckReturnValue
    public static <E> Result<E> defer(@NotNull Supplier<? extends E> supplier)
    {
        Checks.notNull(supplier, "Supplier");
        try
        {
            return Result.success(supplier.get());
        }
        catch (Exception ex)
        {
            return Result.failure(ex);
        }
    }

    /**
     * True if this result is a failure.
     * <br>Use {@link #getFailure()} or {@link #expect(Predicate)} to handle failures.
     *
     * @return True, if this is a failure result
     */
    public boolean isFailure()
    {
        return error != null;
    }

    /**
     * True if this result is a success.
     * <br>Use {@link #get()} or {@link #map(Function)} to handle success values.
     *
     * @return True, if this is a successful result
     */
    public boolean isSuccess()
    {
        return error == null;
    }

    /**
     * Passive error handler.
     * <br>This will apply the provided callback if {@link #isFailure()} is true
     * and return the same result for further chaining.
     *
     * @param  callback
     *         The passive callback
     *
     * @throws IllegalArgumentException
     *         If the callback is null
     *
     * @return The same result instance
     */
    @NotNull
    public Result<T> onFailure(@NotNull Consumer<? super Throwable> callback)
    {
        Checks.notNull(callback, "Callback");
        if (isFailure())
            callback.accept(error);
        return this;
    }

    /**
     * Passive success handler.
     * <br>This will apply the provided callback if {@link #isSuccess()} is true
     * and return the same result for further chaining.
     *
     * @param  callback
     *         The passive callback
     *
     * @throws IllegalArgumentException
     *         If the callback is null
     *
     * @return The same result instance
     */
    @NotNull
    public Result<T> onSuccess(@NotNull Consumer<? super T> callback)
    {
        Checks.notNull(callback, "Callback");
        if (isSuccess())
            callback.accept(value);
        return this;
    }

    /**
     * Composite function to convert a result value to another value.
     * <br>This will only apply the function is {@link #isSuccess()} is true.
     *
     * @param  function
     *         The conversion function
     * @param  <U>
     *         The result type
     *
     * @throws IllegalArgumentException
     *         If the provided function is null
     *
     * @return The mapped result
     *
     * @see    #flatMap(Function)
     */
    @NotNull
    @CheckReturnValue
    @SuppressWarnings("unchecked")
    public <U> Result<U> map(@NotNull Function<? super T, ? extends U> function)
    {
        Checks.notNull(function, "Function");
        if (isSuccess())
            return Result.defer(() -> function.apply(value));
        return (Result<U>) this;
    }

    /**
     * Composite function to convert a result value to another result.
     * <br>This will only apply the function is {@link #isSuccess()} is true.
     *
     * @param  function
     *         The conversion function
     * @param  <U>
     *         The result type
     *
     * @throws IllegalArgumentException
     *         If the provided function is null
     *
     * @return The mapped result
     */
    @NotNull
    @CheckReturnValue
    @SuppressWarnings("unchecked")
    public <U> Result<U> flatMap(@NotNull Function<? super T, ? extends Result<U>> function)
    {
        Checks.notNull(function, "Function");
        try
        {
            if (isSuccess())
                return function.apply(value);
        }
        catch (Exception ex)
        {
            return Result.failure(ex);
        }
        return (Result<U>) this;
    }

    /**
     * Unwraps the success value of this result.
     * <br>This only works if {@link #isSuccess()} is true and throws otherwise.
     *
     * @throws IllegalStateException
     *         If the result is not successful
     *
     * @return The result value
     */
    public T get()
    {
        if (isFailure())
            throw new IllegalStateException(error);
        return value;
    }

    /**
     * Unwraps the error for this result.
     * <br>This will be {@code null} if {@link #isFailure()} is false.
     *
     * @return The error or null
     */
    @Nullable
    public Throwable getFailure()
    {
        return error;
    }

    /**
     * Throws the wrapped exception if the provided predicate returns true.
     * <br>This will never provide a null error to the predicate.
     * A successful result will never throw.
     *
     * @param  predicate
     *         The test predicate
     *
     * @throws IllegalArgumentException
     *         If the provided predicate is null
     * @throws IllegalStateException
     *         If the predicate returns true, the {@link Throwable#getCause() cause} will be the wrapped exception
     *
     * @return The same result instance
     */
    @NotNull
    public Result<T> expect(@NotNull Predicate<? super Throwable> predicate)
    {
        Checks.notNull(predicate, "Predicate");
        if (isFailure() && predicate.test(error))
            throw new IllegalStateException(error);
        return this;
    }

    @Override
    public String toString()
    {
        return isSuccess() ? "Result(success=" + value + ")" : "Result(error=" + error + ")";
    }
}
