package ru.langconvert.langconvert.html.components;

import ru.langconvert.langconvert.languages.ILanguage;

public interface IFileColumn 
{
    public String getName();
    public String createSelector();
    public String createButtons();
    public String createWindow();

    public default String getLine(int i)
    {
        return switch (i)
        {
            case 0 -> "<div class=\"language-table-line\"><a>" + getName() + "</a>" + createSelector() + "</div>";
            case 1 -> createButtons();
            case 2 -> createWindow();
            default -> "";
        };
    }

    public static String createSelectorByLanguages(String idPrefix, ILanguage... languages)
    {
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("<select id=\"" + idPrefix + "lang-selector\">");
        for (ILanguage language : languages)
        {
            String tag = String.format("<option value = \"%s\">%s</option>", language.name(), language.displayName());
            selectBuilder.append(tag);
        }
        selectBuilder.append("</select>");
        return selectBuilder.toString();
    }
}
