package ru.langconvert.langconvert.languages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "language")
public class LanguageModel implements ILanguage
{
    @Id
    private long id;
    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;
    @Column(name = "display_name", length = 255, nullable = false)
    private String displayName;
    @Column(name = "file_extension", length = 63, nullable = false)
    private String fileExtension;
    @Column(name = "web_compiler_url", length = 255, nullable = true)
    private String compilerUrl;

    @JsonProperty("id")
    public long id() { return this.id; }
    @JsonProperty("name")
    public String name() { return this.name; }
    @JsonProperty("displayName")
    public String displayName() { return this.displayName; }
    @JsonProperty("extension")
    public String fileExtension() { return this.fileExtension; }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("compiler")
    public String compilerUrl() { return this.compilerUrl; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setFileExtension(String ext) { this.fileExtension = ext; }
    public void setCompiler(String url) { this.compilerUrl = url; }
}
