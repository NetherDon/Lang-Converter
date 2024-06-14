package ru.langconvert.langconvert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.langconvert.langconvert.converters.ConversionResult;
import ru.langconvert.langconvert.converters.ConverterModel;
import ru.langconvert.langconvert.converters.ConverterRepository;
import ru.langconvert.langconvert.converters.Message;
import ru.langconvert.langconvert.html.FileExtensionResponse;
import ru.langconvert.langconvert.html.LanguageCompilerResponse;
import ru.langconvert.langconvert.html.LanguageNameResponse;
import ru.langconvert.langconvert.html.components.MainPageBuilder;
import ru.langconvert.langconvert.languages.LanguageModel;
import ru.langconvert.langconvert.languages.LanguageRepository;

@Controller
public class MainController 
{
    public static final Logger LOGGER = LoggerFactory.getLogger("main");
    
    @Autowired
    private LanguageRepository langRepo;
    @Autowired
    private ConverterRepository convRepo;

    private <A, ID> List<A> allToList(CrudRepository<A, ID> repo)
    {
        List<A> models = new ArrayList<>();

        var iter = repo.findAll().iterator();
        while (iter.hasNext())
        {
            models.add(iter.next());
        }

        return models;
    }

    @GetMapping("/")
    public String mainPage(ModelMap model)
    {
        String table = MainPageBuilder.createTable(this.allToList(this.langRepo).toArray(new LanguageModel[0]));
        model.put("files_table", table);
        return "index";
    }

    @GetMapping("/languages")
    public ResponseEntity<Collection<LanguageModel>> getLanguages()
    {
        return ResponseEntity.ok(this.allToList(this.langRepo));
    }

    @GetMapping("/languages/extension-by-name")
    public ResponseEntity<FileExtensionResponse> getExtensionByLanguageName(@RequestParam("lang") String languageName)
    {
        return this.langRepo.findExtensionByName(languageName)
            .map(FileExtensionResponse::new)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/languages/name-by-extension")
    public ResponseEntity<LanguageNameResponse> getLanguageNameByExtension(@RequestParam("ext") String fileExtension)
    {
        return this.langRepo.findNameByExtension(fileExtension)
            .map(LanguageNameResponse::new)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/languages/compiler-by-name")
    public ResponseEntity<LanguageCompilerResponse> getCompilerUrlByLanguageName(@RequestParam("lang") String languageName)
    {
        return this.langRepo.findCompilerByName(languageName)
            .map(LanguageCompilerResponse::new)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/converters")
    public ResponseEntity<Collection<ConverterModel>> getConverters()
    {
        return ResponseEntity.ok(this.allToList(this.convRepo));
    }

    @GetMapping("/conversion")
    public ResponseEntity<ConversionResult> convert(String text, @RequestParam("input") String inputLanguageName, @RequestParam("output") String outputLanguageName)
    {
        if (inputLanguageName.equals(outputLanguageName))
        {
            ConversionResult result = new ConversionResult();
            result.setType(ConversionResult.Type.SAME_LANGUAGE);
            result.setText(text);
            return ResponseEntity.ok(result);
        }

        Optional<LanguageModel> langIn = this.langRepo.findByName(inputLanguageName);
        if (!langIn.isPresent())
        {
            ConversionResult result = new ConversionResult();
            result.setType(ConversionResult.Type.CONVERTER_NOT_FOUND);
            result.errors().add(new Message("Unknown language name \"" + inputLanguageName + "\""));
            return ResponseEntity.badRequest().body(result);
        }

        Optional<LanguageModel> langOut = this.langRepo.findByName(outputLanguageName);
        if (!langOut.isPresent())
        {
            ConversionResult result = new ConversionResult();
            result.setType(ConversionResult.Type.CONVERTER_NOT_FOUND);
            result.errors().add(new Message("Unknown language name \"" + outputLanguageName + "\""));
            return ResponseEntity.badRequest().body(result);
        }

        Optional<ConverterModel> converter = this.convRepo.findByLanguages(langIn.get(), langOut.get());
        if (!converter.isPresent())
        {
            ConversionResult result = new ConversionResult();
            result.setType(ConversionResult.Type.CONVERTER_NOT_FOUND);
            result.setErrors(
                List.of(
                    new Message(
                        String.format(
                            "Перевод кода с %s на %s не поддерживается или временно недоступен", 
                            langIn.get().displayName(), 
                            langOut.get().displayName()
                        )
                    )
                )
            );
            return ResponseEntity.ok(result);
        }

        try 
        {
            ConversionResult result = converter.get().convert(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
            return ResponseEntity.ok(result);
        } 
        catch (
            ClassNotFoundException | NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassCastException | IOException e
        ) 
        {
            e.printStackTrace();
        }
        
        return ResponseEntity.internalServerError().build();
    }
}
