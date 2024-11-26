package org.themullers.vocab;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleSheets extends Vocabulary {

    private final Log logger = LogFactory.getLog(GoogleSheets.class);

    Sheets sheets;
    String spreadsheetId;
    String sheetName;

    public GoogleSheets(@Value("${google.cloud.credentials.json}") String credentialsFile,
                        @Value("${google.sheets.spreadsheet.id}") String spreadsheetId,
                        @Value("${google.sheets.sheet.name}") String sheetName) throws IOException, GeneralSecurityException {

        try (var credentialStream = new FileInputStream(credentialsFile)) {

            // create the credentials
            var scope = Collections.singleton(SheetsScopes.SPREADSHEETS_READONLY);
            var credentials = GoogleCredentials.fromStream(credentialStream).createScoped(scope);

            // create the client
            var transport = GoogleNetHttpTransport.newTrustedTransport();
            var jsonFactory = JacksonFactory.getDefaultInstance();
            var credentialsAdapter = new HttpCredentialsAdapter(credentials);
            sheets = new Sheets.Builder(transport, jsonFactory, credentialsAdapter).setApplicationName("vocab").build();

            // save the name and sheet of the spreadsheet that has our vocabulary data
            this.spreadsheetId = spreadsheetId;
            this.sheetName = sheetName;
        }
    }

    @PostConstruct
    public void load() throws IOException {

        // get the data in this entire sheet
        var values = sheets.spreadsheets().values().get(spreadsheetId, sheetName).execute().getValues();
        if (values == null) {
            throw new IOException("empty spreadsheet");
        }

        // load each row, skipping the header row
        values.stream().skip(1).forEach(row -> loadRow(row));

        // organize the vocabulary words into data structures for easy access
        buildSpanishWordList();
        buildEnglishWordList();

    }

    @Override
    protected Object get(Object row, int index) {
        List<Object> list = (List<Object>) row;
        return index < list.size() ? list.get(index) : null;
    }
}
