package ru.langconvert.langconvert;

import java.util.Arrays;

public class FileLangInfo 
{
    private final String id;
    private final String name;
    private final String[] existingConverters;

    public FileLangInfo(Language language)
    {
        this.id = language.id;
        this.name = language.name;
        this.existingConverters = Arrays.stream(
                    Languages.CONVERTERS
                            .getConverters(language.id)
                    )
                    .map((lang) -> lang.languages.result)
                    .toArray((size) -> new String[size]);

        MainController.LOGGER.info(Arrays.toString(this.existingConverters));
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String[] getExistingConverters()
    {
        return this.existingConverters;
    }
}
