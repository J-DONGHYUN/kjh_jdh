package project.kjhjdh.ibid;

import org.springframework.boot.SpringApplication;

public class TestIbidBackApplication {

	public static void main(String[] args) {
		SpringApplication.from(IbidBackApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
