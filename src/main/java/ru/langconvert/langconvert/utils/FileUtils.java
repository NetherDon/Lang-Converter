package ru.langconvert.langconvert.utils;

public final class FileUtils 
{
    public static FileName splitName(String fileName)
    {
        int dot = fileName.lastIndexOf('.');
        if (dot == -1)
            return new FileName("", "");

        String name = fileName.substring(0, dot);
        String extension = fileName.substring(dot+1);
        return new FileName(name, extension);
    }

    public static final class FileName
    {
        public final String path;
        public final String extension;

        public FileName(String path, String extension)
        {
            this.path = path;
            this.extension = extension;
        }
    }
}
