package ru.langconvert.langconvert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ConverterSet
{
    private final Map<String, List<Converter>> converters = new HashMap<>();

    public ConverterSet(Converter... converters)
    {
        for (Converter converter : converters)
        {
            List<Converter> list = this.converters.getOrDefault(converter.languages.initial, null);
            if (list == null)
            {
                list = new ArrayList<>();
                this.converters.put(converter.languages.initial, list);
            }

            list.add(converter);
        }
    }

    public static ConverterSet create(Converter.Creator... creators)
    {
        return new ConverterSet(
            Arrays.stream(creators)
                .<Converter>mapMulti((creator, consumer) ->
                {
                    for (Converter conv : creator.get())
                    {
                        consumer.accept(conv);
                    }
                })
                .toArray((size) -> new Converter[size])
        );
    }

    public Converter[] getConverters(String language)
    {
        List<Converter> convs = this.converters.getOrDefault(language, null);
        if (convs == null)
            return new Converter[0];

        return convs.toArray(new Converter[0]);
    }

    public boolean contains(String initial)
    {
        return this.converters.containsKey(initial);
    }

    public boolean contains(String initial, String result)
    {
        for (Converter converter : getConverters(initial))
        {
            if (converter.languages.result == result)
            {
                return true;
            }
        }

        return false;
    }
}
