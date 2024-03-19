package ru.langconvert.langconvert.html;

public class FileExtensionResponse 
{
    private String extension;

    public FileExtensionResponse(String extension)
    {
        this.extension = extension;
    }
    
    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    public String getExtension()
    {
        return this.extension;
    }

    @Override
    public String toString()
    {
        return "FileExtension[" + this.extension + "]";
    }
}
