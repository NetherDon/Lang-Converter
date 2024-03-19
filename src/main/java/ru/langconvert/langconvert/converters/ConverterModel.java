package ru.langconvert.langconvert.converters;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ru.langconvert.langconvert.languages.LanguageModel;

@Entity
@Table(name="converter")
public class ConverterModel 
{
    public static final Path JARS_PATH = new File("converters").toPath();

    @Id
    private long id;
    @Column(name="name")
    private String name;
    @ManyToOne
    @JoinColumn(name="lang_in_id")
    private LanguageModel inputLanguage;
    @ManyToOne
    @JoinColumn(name="lang_out_id")
    private LanguageModel outputLanguage;
    @Column(name="file_name")
    private String fileName;
    @Column(name="package")
    private String packageName;

    @JsonProperty("id")
    public long id() { return this.id; }
    @JsonProperty("name")
    public String name() { return this.name; }
    @JsonProperty("fileName")
    public String fileName() { return this.fileName; }
    @JsonProperty("package")
    public String packageName() { return this.packageName; }
    public LanguageModel inputLanguage() { return this.inputLanguage; }
    public LanguageModel outputLanguage() { return this.outputLanguage; }

    @JsonProperty("languages.in")
    private long inputLanguageId() { return this.inputLanguage.id(); }
    @JsonProperty("languages.out")
    private long outputLanguageId() { return this.outputLanguage.id(); }

    public Path jarPath() { return JARS_PATH.resolve(this.fileName + ".jar"); }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public void setInputLanguage(LanguageModel language) { this.inputLanguage = language; }
    public void setOutputLanguage(LanguageModel language) { this.outputLanguage = language; }

    public ConversionResult convert(InputStream inputStream) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
    {
        URL[] url = new URL[] { jarPath().toFile().toURI().toURL() };
        System.out.println("Try found converter " + url[0]);
        URLClassLoader classLoader = new URLClassLoader(url);
        Class<?> parseMainClass = Class.forName(this.packageName + ".Main", true, classLoader);
        
        if (parseMainClass == null)
        {
            ConversionResult result = new ConversionResult();
            result.errors().add(new Message("Conversion Error 1"));
            return result;
        }

        System.out.println("Main class: " + parseMainClass);
        Method method = parseMainClass.getMethod("parse", InputStream.class);

        String jsonStr = (String)method.invoke(null, inputStream);
        classLoader.close();

        ObjectMapper mapper = new ObjectMapper();
        ConversionResult result = mapper.readValue(jsonStr, ConversionResult.class);
        if (result.errors().size() == 0)
        {
            result.setType(ConversionResult.Type.SUCCESS);
        }
        else
        {
            result.setText("");
            result.setType(ConversionResult.Type.ERROR);
        }
        
        return result;
    }
}
