package com.trendingtech.taskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties ={
	"spring.datasource.url=jdbc:h2:mem:testdb",
	"spring.jpa.hibernate.ddl-auto=create-drop"
})
class TaskManagerApplicationTests {

	@Test
	void contextLoads() {
	}

}
