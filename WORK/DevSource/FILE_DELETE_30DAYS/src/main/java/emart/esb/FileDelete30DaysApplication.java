package emart.esb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ImportResource(value = { "classpath:bean.xml", "classpath:INTERFACE.xml" })
public class FileDelete30DaysApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(FileDelete30DaysApplication.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run();
	}

}
