package ru.langconvert.langconvert.html.components;

import ru.langconvert.langconvert.languages.ILanguage;

public final class MainPageBuilder 
{
    public static String createTable(ILanguage... languages)
    {
        InputColumn inputColumn = new InputColumn(languages);
        OutputColumn outputColumn = new OutputColumn(languages);

        StringBuilder tableBuilder = new StringBuilder();
        //tableBuilder.append("<table id=\"files-table\">");
        tableBuilder.append("<div id=\"files-grid\">");
        for (int i = 0; i < 3; i++)
        {
            String line = createTableLine(i, inputColumn, outputColumn);
            tableBuilder.append(line);
        }
        tableBuilder.append("</div>");
        //tableBuilder.append("</table>");

        return tableBuilder.toString();
    }

    private static String createTableLine(int i, IFileColumn... columns)
    {
        StringBuilder lineBuilder = new StringBuilder();
        //lineBuilder.append("<tr>");
        for (IFileColumn column : columns)
        {
            //lineBuilder.append("<td>");
            lineBuilder.append(column.getLine(i));
            //lineBuilder.append("</td>");
        }
        //lineBuilder.append("</tr>");
        return lineBuilder.toString();
    }
}
