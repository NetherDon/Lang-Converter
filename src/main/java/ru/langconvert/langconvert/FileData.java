package ru.langconvert.langconvert;

public class FileData 
{
    private final String langId;
    private final String name;
    private final String editorUrl;
    private final String fileContent;

    public FileData(Language lang, String name, String fileContent)
    {
        this.langId = lang.id;
        this.name = name;
        this.editorUrl = lang.editorUrl;
        this.fileContent = fileContent;
    }

    public String getLang()
    {
        return this.langId;
    }

    public String getName()
    {
        return this.name;
    }

    public String getContent()
    {
        return this.fileContent;
    }

    public String getEditorUrl()
    {
        return this.editorUrl;
    }
}
