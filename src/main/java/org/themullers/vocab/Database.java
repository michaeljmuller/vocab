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

    public void incrementIncorrect(String word, int language) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user", user);
        params.addValue("word", word);
        params.addValue("language", language);
        jt.update(Sql.INCREMENT_INCORRECT, params);
    }

    public void incrementCorrect(String word, int language) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user", user);
        params.addValue("word", word);
        params.addValue("language", language);
        jt.update(Sql.INCREMENT_CORRECT, params);
    }

    public List<String> getKnownWords(int language) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user", user);
        params.addValue("language", language);
        return jt.queryForList(Sql.GET_KNOWN_WORDS, params, String.class);
    }

    protected void createTables() {
        logger.info("creating schema if necessary");
        jt.getJdbcOperations().execute(Sql.CREATE_CORRECT_COUNT_TABLE);
    }

    public interface Sql {
        String CREATE_CORRECT_COUNT_TABLE = """
            create table if not exists correct_count (
                user text, 
                word text, 
                language int, 
                correct_count int default 0, 
                incorrect_count int default 0, 
                primary key (user, word, language)
            )  
        """;

        String INCREMENT_CORRECT = """
            insert into correct_count (user, word, language, correct_count)
            values (:user, :word, :language, 1)
            on conflict(user, word, language) do 
                update  
                set correct_count=(correct_count.correct_count + 1) 
                where correct_count.user=:user
                and correct_count.word=:word
                and correct_count.language=:language
        """;

        String INCREMENT_INCORRECT = """
            insert into correct_count (user, word, language, incorrect_count)
            values (:user, :word, :language, 1)
            on conflict(user, word, language) do 
                update  
                set incorrect_count=(correct_count.incorrect_count + 1) 
                where correct_count.user=:user
                and correct_count.word=:word
                and correct_count.language=:language
        """;

        String GET_KNOWN_WORDS = """
            select word from correct_count 
            where user=:user 
            and language=:language
            and correct_count.correct_count > 0
        """;
    }
}
