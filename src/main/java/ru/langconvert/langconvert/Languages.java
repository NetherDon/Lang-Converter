package ru.langconvert.langconvert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

import ru.langconvert.langconvert.FileUtils.FileName;

public class Languages 
{
    /*
    private static final Map<String, Language> ALL = new LinkedHashMap<>();

    public static final Language CSHARP = add(
        new Language.Builder("csharp")
                    .name("C#")
                    .extension("cs")
                    .editorUrl("https://www.jdoodle.com/compile-c-sharp-online/")
    );

    public static final Language CPP = add(
        new Language.Builder("cpp")
                    .name("C++")
                    .prismDependencies("c")
                    .editorUrl("https://www.jdoodle.com/online-compiler-c++17/")
    );
    
    public static final Language JAVA = add(
        new Language.Builder("java")
                    .name("Java")
                    .editorUrl("https://www.jdoodle.com/online-java-compiler/")
    );
    
    public static final Language JAVASCRIPT = add(
        new Language.Builder("javascript")
                    .name("JavaScript")
                    .extension("js")
                    .editorUrl("https://www.jdoodle.com/execute-spidermonkey-online/")
    );
    
    public static final Language PASCAL = add(
        new Language.Builder("pascal")
                    .name("Pascal")
                    .extension("pas")
                    .editorUrl("https://www.jdoodle.com/execute-pascal-online/")
    );

    public static final ConverterSet CONVERTERS = ConverterSet.create(
        Converter.For(CSHARP).Create(CPP).Create(JAVASCRIPT),
        Converter.For(CPP).Create(PASCAL).Create(JAVA).Create(JAVASCRIPT)
    );

    private static Language add(Language.Builder builder)
    {
        return add(builder.build());
    }

    private static Language add(Language language)
    {
        ALL.put(language.id, language);
        return language;
    }
    */

    public static final ConverterSet CONVERTERS = ConverterSet.create(
        Converter.For("csharp").Create("cpp").Create("javascript"),
        Converter.For("cpp").Create("pascal").Create("java").Create("javascript")
    );

    public static Language[] getAll()
    {
        List<Language> result = new ArrayList<>();
        
        try
        {
            File[] langFiles = new ClassPathResource("data/langs/").getFile().listFiles();
            for (File file : langFiles)
            {
                if (file.isDirectory())
                    continue;

                Language lang = Language.readFromXML(file);
                if (lang != null)
                    result.add(lang);
            }
        }
        catch (IOException e)
        {
            MainController.LOGGER.error("Failed to open language folder");
        }

        return result.toArray(new Language[0]);
    }

    public static Language getByID(String id)
    {
        String filename = id + ".xml";
        
        try
        {
            return Language.readFromXML(new ClassPathResource("data/langs/" + filename).getFile());
        }
        catch (IOException e)
        {
            MainController.LOGGER.error("Failed to open language folder");
        }

        return null;
    }

    public static Language getByExtension(String extension)
    {
        return Arrays.stream(getAll())
                    .filter((lang) -> lang.fileExtension.equals(extension))
                    .findFirst()
                    .orElse(null);
    }

    public static String createHTMLSelectorValues()
    {
        return Arrays.stream(getAll())
                    .map((lang) -> lang.toHTMLSelectorValue())
                    .collect(Collectors.joining("\n"));
    }

    public static String createHTMLPrismImport()
    {
        return Arrays.stream(getAll())
                    .<String>mapMulti((lang, consumer) -> 
                    {
                        for (String prismLang : lang.prismLangs)
                        {
                            consumer.accept(prismLang);
                        }
                    })
                    .distinct()
                    .map((prismLang) -> getPrismLangScript(prismLang))
                    .collect(Collectors.joining("\n"));
                    
    }

    private static String getPrismLangScript(String prismLang)
    {
        return String.format("<script src=\"https://unpkg.com/prismjs@1.29.0/components/prism-%s.js\"></script>", prismLang);
    }

    public static Language getByFileName(String filename)
    {
        FileName fn = FileUtils.splitName(filename);
        if (fn == null)
            return null;

        return getByExtension(fn.extension);
    }
}
