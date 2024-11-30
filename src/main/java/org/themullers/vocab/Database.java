package org.themullers.vocab;

import jakarta.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.themullers.vocab.pojo.Gender;
import org.themullers.vocab.pojo.Language;
import org.themullers.vocab.pojo.WordAndGender;

import java.util.List;

@Component
public class Database {

    private final Log logger = LogFactory.getLog(Database.class);

    String pathToDatabaseFile;
    NamedParameterJdbcTemplate jt;

    public Database(@Value("${db.path}") String pathToDatabaseFile) {
        this.pathToDatabaseFile = pathToDatabaseFile;
        logger.debug("database path: " + pathToDatabaseFile);
    }

    @PostConstruct
    public void connect() {

        // create a data source
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:" + pathToDatabaseFile);

        // create a jdbc template object that uses the data source
        jt = new NamedParameterJdbcTemplate(dataSource);

        // after connecting, ensure the tables are created
        createTables();
    }

    public List<WordAndGender> getKnownWords(Language language) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user", user);
        params.addValue("language", language.getId());

        return jt.query(Sql.GET_KNOWN_WORDS, params, (rs,i) -> {
            var wordId = rs.getInt("word_id");
            var gender = Gender.fromId(rs.getInt("gender"));
            return new WordAndGender(wordId, gender);
        });
    }

    public void incrementCorrect(int wordId, Gender gender, Language language) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user", user);
        params.addValue("wordId", wordId);
        params.addValue("gender", gender.getId());
        params.addValue("language", language.getId());
        jt.update(Sql.INCREMENT_CORRECT_COUNT, params);
    }

    public void incrementIncorrect(int wordId, Gender gender, Language language) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user", user);
        params.addValue("wordId", wordId);
        params.addValue("gender", gender.getId());
        params.addValue("language", language.getId());
        jt.update(Sql.INCREMENT_INCORRECT_COUNT, params);
    }

    protected void createTables() {
        logger.info("creating schema if necessary");
        jt.getJdbcOperations().execute(Sql.CREATE_WORD_SCORE_TABLE);
    }

    public interface Sql {

        String CREATE_WORD_SCORE_TABLE = """
            create table if not exists word_score (
                user text,
                word_id int,
                gender int,
                language int,
                correct_count int default 0,
                incorrect_count int default 0,
                primary key (user, word_id, gender, language)  
            )
        """;

        String INCREMENT_CORRECT_COUNT = """
            insert into word_score (user, word_id, gender, language, correct_count)
            values (:user, :wordId, :gender, :language, 1)
            on conflict (user, word_id, gender, language) do
                update
                set correct_count=(word_score.correct_count + 1)
                where word_score.user=:user
                and word_id=:wordId
                and gender=:gender
                and language=:language
        """;

        String INCREMENT_INCORRECT_COUNT = """
            insert into word_score (user, word_id, gender, language, correct_count)
            values (:user, :wordId, :gender, :language, 1)
            on conflict (user, word_id, gender, language) do
                update
                set incorrect_count=(word_score.incorrect_count + 1)
                where word_score.user=:user
                and word_id=:wordId
                and gender=:gender
                and language=:language
        """;

        String GET_KNOWN_WORDS = """
            select word_id, gender from word_score 
            where user=:user 
            and language=:language
            and word_score.correct_count > 0
        """;
    }
}
