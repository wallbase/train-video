package com.wallbase.train;

import com.wallbase.train.service.DownloadService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class TrainVideoApplication {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(1000);
        return executor;
    }

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(TrainVideoApplication.class, args);
        DownloadService downloadService = context.getBean(DownloadService.class);

        try {
            //开始执行下载器
            downloadService.download();
        } catch (IOException | URISyntaxException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

    }
}
