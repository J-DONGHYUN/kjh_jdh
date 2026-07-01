package project.kjhjdh.ibid;

import org.springframework.boot.SpringApplication;

public class TestIbidApplication {

	public static void main(String[] args) {
		SpringApplication.from(IbidApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
