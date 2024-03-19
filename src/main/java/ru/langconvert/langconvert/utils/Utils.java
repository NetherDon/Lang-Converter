package ru.langconvert.langconvert.utils;

public final class Utils 
{
    public static <T> T throwIfNull(T value)
    {
        return throwIfNull(value, null);
    }

    public static <T> T throwIfNull(T value, String message)
    {
        if (value == null)
        {
            if (message == null)
                throw new NullPointerException();
            else
                throw new NullPointerException(message);
        }
        
        return value;
    }
}
