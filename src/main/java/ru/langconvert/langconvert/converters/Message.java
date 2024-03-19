package ru.langconvert.langconvert.converters;

public record Message(String text, int row, int column) 
{
    public Message(String text)
    {
        this(text, -1, -1);
    }

    public String getText() { return this.text; }
    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }
}