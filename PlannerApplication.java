package financeplanner.planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // RECTIFIED: Removed the (exclude = {SecurityAutoConfiguration.class})
public class PlannerApplication {
	public static void main(String[] args) {
		SpringApplication.run(PlannerApplication.class, args);
	}
}