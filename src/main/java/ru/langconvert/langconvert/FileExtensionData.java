package ru.langconvert.langconvert;

public class FileExtensionData 
{
    private String extension;
    
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
