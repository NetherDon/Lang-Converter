package ru.langconvert.langconvert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.print.DocFlavor.STRING;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import ru.langconvert.langconvert.FileUtils.FileName;

@Controller
@SuppressWarnings("unused")
public class MainController 
{
    public static final Logger LOGGER = LoggerFactory.getLogger("main");

    @GetMapping("/")
    public String initData(ModelMap model)
    {
        model.put("lang_selector_values", Languages.createHTMLSelectorValues());
        model.put("prism_scripts", Languages.createHTMLPrismImport());
        model.put(
            "lang_extensions", 
            String.join(
                ", ", 
                Arrays.stream(Languages.getAll())
                    .map((lang) -> "." + lang.fileExtension)
                    .toArray((size) -> new String[size])
            )
        );
        return "index";
    }

    @PostMapping("/converter")
    public ResponseEntity<?> convertToLanguage(@RequestParam("file") MultipartFile file, @RequestParam("requiredLanguageId") String requiredLanguage)
    {
        ResponseEntity.BodyBuilder errResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);

        Language language1 = Languages.getByFileName(file.getOriginalFilename());
        if (language1 == null)
            return errResponse.body("Unknown language of file");
        
        Language language2 = Languages.getByID(requiredLanguage);
        if (language2 == null)
            return errResponse.body("Unknown required language \"" + requiredLanguage + "\"");
        
        FileName name = FileUtils.splitName(file.getOriginalFilename());

        try
        {
            String fileContent = new String(file.getBytes());

            String resultFileName = name.path + "." + language2.fileExtension;
            
            String resultFileContent = String.format("\"%s\" converted to \"%s\"\n\n", language1.name, language2.name);
            LOGGER.info("File size: " + fileContent.length());
            for (int i = fileContent.length()-1; i >= 0; i--)
            {
                resultFileContent += fileContent.charAt(i);
            }
            
            FileData fileData = new FileData(language2, resultFileName, resultFileContent);
            return ResponseEntity.ok(fileData);
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
            return errResponse.body("Can't read file");
        }
    }

    @GetMapping("/lang")
    public @ResponseBody ResponseEntity<FileLangInfo> getLangDataByExtension(FileExtensionData data)
    {
        LOGGER.info(data.toString());
        Language lang = Languages.getByExtension(data.getExtension());
        if (lang == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        return ResponseEntity.ok(new FileLangInfo(lang));
    }
}
