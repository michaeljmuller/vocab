package org.themullers.vocab;

import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class VocabApplication {

	protected VocabularyFile vocabularyFile;

	public VocabApplication(VocabularyFile vocabularyFile) {
		this.vocabularyFile = vocabularyFile;
	}

	public static void main(String[] args) {
		SpringApplication.run(VocabApplication.class, args);
	}

}
