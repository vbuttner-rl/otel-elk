package com.example.app;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@RestController
public class AppApplication {
    Logger logger = LoggerFactory.getLogger(AppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @GetMapping("/")
    public String root(@RequestParam(value = "name", defaultValue = "World") String name, @RequestHeader HttpHeaders headers) {
        logger.error(headers.toString());
        logger.error(String.format("Hello %s!!", name));
        logger.debug("Debugging log");
        logger.info("Info log");
        logger.warn("Hey, This is a warning!");
        logger.error("Oops! We have an Error. OK");
        return String.format("Hello %s!!", name);
    }

    @GetMapping("/io_task")
    public String io_task(@RequestParam(value = "name", defaultValue = "World") String name) throws InterruptedException {
        Thread.sleep(1000);
        logger.info("io_task");
        return "io_task";
    }

    @GetMapping("/cpu_task")
    public String cpu_task(@RequestParam(value = "name", defaultValue = "World") String name) {
        for (int i = 0; i < 100; i++) {
            int tmp = i * i * i;
        }
        logger.info("cpu_task");
        return "cpu_task";
    }

    @GetMapping("/random_sleep")
    public String random_sleep(@RequestParam(value = "name", defaultValue = "World") String name) throws InterruptedException {
        Thread.sleep((int) (Math.random() / 5 * 10000));
        logger.info("random_sleep");
        return "random_sleep";
    }

    @GetMapping("/random_status")
    public String random_status(@RequestParam(value = "name", defaultValue = "World") String name, HttpServletResponse response) throws InterruptedException {
        List<Integer> givenList = Arrays.asList(200, 200, 300, 400, 500);
        Random rand = new Random();
        int randomElement = givenList.get(rand.nextInt(givenList.size()));
        response.setStatus(randomElement);
        logger.info("random_status");
        return "random_status";
    }

    @GetMapping("/chain")
    public String chain(@RequestParam(value = "name", defaultValue = "World") String name) throws InterruptedException, IOException {
        String TARGET_ONE_SVC = System.getenv().getOrDefault("TARGET_ONE_SVC", "localhost:8080");
        String TARGET_TWO_SVC = System.getenv().getOrDefault("TARGET_TWO_SVC", "localhost:8080");
        logger.debug("chain is starting");
        Request.Get("http://localhost:8080/")
                .execute().returnContent();
        Request.Get(String.format("http://%s/io_task", TARGET_ONE_SVC))
                .execute().returnContent();
        Request.Get(String.format("http://%s/cpu_task", TARGET_TWO_SVC))
                .execute().returnContent();
        logger.debug("chain is finished");
        return "chain";
    }

    @GetMapping("/error_test")
    public String error_test(@RequestParam(value = "name", defaultValue = "World") String name) throws Exception {
        throw new Exception("Error test");
    }

}
