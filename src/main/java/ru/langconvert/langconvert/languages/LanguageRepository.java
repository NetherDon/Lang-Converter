package ru.langconvert.langconvert.languages;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends CrudRepository<LanguageModel, Long>
{
    @Query(value="select file_extension from language where name = :name", nativeQuery=true)
    public Optional<String> findExtensionByName(@Param("name") String name);

    @Query(value="select name from language where file_extension = :ext", nativeQuery=true)
    public Optional<String> findNameByExtension(@Param("ext") String fileExtension);

    @Query(value="select web_compiler_url from language where name = :name", nativeQuery=true)
    public Optional<String> findCompilerByName(@Param("name") String name);

    public Optional<LanguageModel> findByName(String name);
}
