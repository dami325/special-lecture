package io.dami.speciallecture;

import org.springframework.boot.SpringApplication;

public class TestSepcialLectureApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpecialLectureApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
