package com.squidlard.sutils.Utilities;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public class GuavaCompat
{
    public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> enumClass, String value)
    {
        Preconditions.checkNotNull(enumClass);
        Preconditions.checkNotNull(value);
        try
        {
            return Optional.of(Enum.valueOf(enumClass, value));
        }
        catch (IllegalArgumentException iae) {}
        return Optional.absent();
    }

    public static <T> T firstNonNull(@Nullable T first, @Nullable T second)
    {
        return first != null ? first : Preconditions.checkNotNull(second);
    }
}
