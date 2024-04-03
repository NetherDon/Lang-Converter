package ru.langconvert.langconvert.html;

public class LanguageCompilerResponse 
{
    private String url;
    
    public LanguageCompilerResponse(String url)
    {
        this.url = url;
    }

    public String getUrl() { return this.url; }
}
