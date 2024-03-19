package ru.langconvert.langconvert.converters;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class ConversionResult
{
    private String text = "";
    private Type type = Type.ERROR;
    private List<Message> errors = new ArrayList<>();
    private List<Message> warnings = new ArrayList<>();

    public void setText(String text) { this.text = text; }
    public void setErrors(List<Message> messages) { this.errors = messages; }
    public void setWarnings(List<Message> messages) { this.warnings = messages; }
    public void setType(Type type) { this.type = type; }

    @JsonProperty("text")
    public String text() { return this.text; }
    @JsonProperty("type")
    public Type type() { return this.type; }
    @JsonProperty("errors")
    public List<Message> errors() { return this.errors; }
    @JsonProperty("warnings")
    public List<Message> warnings() { return this.warnings; }

    public static enum Type
    {
        SUCCESS("success"),
        SAME_LANGUAGE("same-language"),
        CONVERTER_NOT_FOUND("converter-not-found"),
        ERROR("error");

        private String name;

        private Type(String name)
        {
            this.name = name;
        }

        @JsonValue
        public String getName()
        {
            return this.name;
        }
    }
}