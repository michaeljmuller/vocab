package org.themullers.vocab;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VocabApplicationTests {

	@Autowired
	Vocabulary vocabulary;

	@Autowired
	Database db;

	@Test
	void contextLoads() {
	}
}
