package ru.langconvert.langconvert;

public class LangRequest 
{
    private String langId;

    public void setLangId(String langId)
    {
        this.langId = langId;
    }

    public String getLangId()
    {
        return this.langId;
    }

    @Override
    public String toString()
    {
        return "LangRequest[" + this.langId + "]";
    }
    
}
