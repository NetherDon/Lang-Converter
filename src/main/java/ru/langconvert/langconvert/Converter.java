package ru.langconvert.langconvert;

import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.KeyAlreadyExistsException;

public final class Converter
{
    public final Pair languages;

    public Converter(String initial, String result)
    {
        this.languages = new Pair(initial, result);
    }

    public final class Pair
    {
        public final String initial;
        public final String result;

        private Pair(String initial, String result)
        {
            this.initial = initial.intern();
            this.result = result.intern();
        }
    }

    public static Creator For(String initial)
    {
        return new Creator(initial, new HashMap<>());
    }

    public static final class Creator
    {
        private final String initial;
        private final Map<String, Converter> converters;

        private Creator(String initial, Map<String, Converter> converters)
        {
            this.initial = initial;
            this.converters = converters;
        }

        public Creator Create(String result)
        {
            if (this.initial == result)
            {
                throw new KeyAlreadyExistsException(
                    String.format(
                        "Trying to create a converter from %s to %s", 
                        this.initial, 
                        this.initial
                    )
                );
            }

            if (this.converters.containsKey(result))
            {
                throw new KeyAlreadyExistsException(
                    String.format(
                        "Converter from %s to %s already created",
                        this.initial,
                        result
                    )
                );
            }

            Map<String, Converter> dict = new HashMap<>(this.converters);
            Converter converter = new Converter(this.initial, result);
            dict.put(result, converter);

            return new Creator(this.initial, dict);
        }

        public Converter[] get()
        {
            return this.converters.values().toArray(new Converter[0]);
        }
    }
}
