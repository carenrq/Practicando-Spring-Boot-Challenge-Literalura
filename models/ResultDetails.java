package com.aluracursos.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultDetails(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<AuthorDetails> autorList,
        @JsonAlias("languages") List<String> language,
        @JsonAlias("download_count") Integer descargas
) {

}
