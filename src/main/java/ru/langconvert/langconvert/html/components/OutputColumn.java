package ru.langconvert.langconvert.html.components;

import ru.langconvert.langconvert.languages.ILanguage;

public class OutputColumn implements IFileColumn
{
    private final ILanguage[] languages;

    public OutputColumn(ILanguage... languages)
    {
        this.languages = languages;
    }

    @Override
    public String getName() 
    {
        return "В";
    }

    @Override
    public String createSelector() 
    {
        return IFileColumn.createSelectorByLanguages("output-", this.languages);
    }

    @Override
    public String createButtons() 
    {
        return """
            <div class=\"button-table-line grid-two-columns\">
                <input id=\"download-output-btn\" type=\"button\" value=\"Скачать\" disabled>
                <input id=\"copy-output-btn\" type=\"button\" value=\"Копировать\" disabled>
            </div>
        """;
    }

    @Override
    public String createWindow() 
    {
        return """
            <div class=\"code-window\">
                <pre id=\"output-text\" class="highlighted-text"></pre>
            </div>
                """;
    }
    
}
