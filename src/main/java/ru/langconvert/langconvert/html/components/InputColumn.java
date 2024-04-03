package ru.langconvert.langconvert.html.components;

import ru.langconvert.langconvert.languages.ILanguage;

public class InputColumn implements IFileColumn
{
    private final ILanguage[] languages;

    public InputColumn(ILanguage... languages)
    {
        this.languages = languages;
    }

    @Override
    public String getName() 
    {
        return "Из";
    }

    @Override
    public String createSelector() 
    {
        return IFileColumn.createSelectorByLanguages("input-", this.languages);
    }

    @Override
    public String createButtons() 
    {
        return """
            <div class=\"button-table-line grid-three-columns\">
                <input id="convert-btn" type="button" value="Конвертировать">
                <input id="load-file-btn" type="button" value="Загрузить">
                <input id="load-file-input" type="file">
                <input id="download-input-btn" type="button" value="Скачать">
                <input id="copy-input-btn" type="button" value="Копировать">
                <input id="open-compiler-input-btn" type="button" value="Проверить">
            </div>
        """;
    }

    @Override
    public String createWindow() 
    {
        return """
            <div class=\"code-window\">
                <textarea id=\"input-textarea\"></textarea>
                <pre id=\"input-highligted-text\" class="highlighted-text" aria-hidden></pre>
                <input id=\"show-message-btn\" type=\"button\" value=\"!\" style="display: none;">
            </div>
                """;
    }
    
}
