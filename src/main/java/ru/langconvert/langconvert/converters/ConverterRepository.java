package ru.langconvert.langconvert.converters;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.langconvert.langconvert.languages.LanguageModel;

@Repository
public interface ConverterRepository extends CrudRepository<ConverterModel, Long>
{
    @Query(value="select * from converter where lang_in_id = :langInId and lang_out_id = :langOutId", nativeQuery=true)
    public Optional<ConverterModel> findByLanguages(@Param("langInId") long languageInId, @Param("langOutId") long languageOutId);

    public default Optional<ConverterModel> findByLanguages(LanguageModel languageIn, LanguageModel languageOut)
    {
        return this.findByLanguages(languageIn.id(), languageOut.id());
    }
}
