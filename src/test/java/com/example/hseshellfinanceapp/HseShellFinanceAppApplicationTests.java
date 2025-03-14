package com.example.hseshellfinanceapp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.hseshellfinanceapp.facade.BankAccountFacade;
import com.example.hseshellfinanceapp.facade.CategoryFacade;
import com.example.hseshellfinanceapp.facade.OperationFacade;
import com.example.hseshellfinanceapp.repository.BankAccountRepository;
import com.example.hseshellfinanceapp.repository.CategoryRepository;
import com.example.hseshellfinanceapp.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class HseShellFinanceAppApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void mainMethodStartsApplication() throws InterruptedException {
        InputStream originalIn = System.in;

        try {
            ByteArrayInputStream testIn = new ByteArrayInputStream("exit\n".getBytes());

            System.setIn(testIn);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    HseShellFinanceAppApplication.main(new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            executor.shutdown();
            boolean completed = executor.awaitTermination(10, TimeUnit.SECONDS);

            assertTrue(completed, "Application should exit after receiving 'exit' command");
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void repositoriesAreAvailable() {
        assertNotNull(applicationContext.getBean(BankAccountRepository.class),
                "BankAccountRepository should be available");
        assertNotNull(applicationContext.getBean(CategoryRepository.class),
                "CategoryRepository should be available");
        assertNotNull(applicationContext.getBean(OperationRepository.class),
                "OperationRepository should be available");
    }

    @Test
    void facadesAreAvailable() {
        assertNotNull(applicationContext.getBean(BankAccountFacade.class),
                "BankAccountFacade should be available");
        assertNotNull(applicationContext.getBean(CategoryFacade.class),
                "CategoryFacade should be available");
        assertNotNull(applicationContext.getBean(OperationFacade.class),
                "OperationFacade should be available");
    }

    @Test
    void beanNamesAreCorrect() {
        assertTrue(applicationContext.containsBean("bankAccountRepository"),
                "Bean name 'bankAccountRepository' should exist");
        assertTrue(applicationContext.containsBean("categoryRepository"),
                "Bean name 'categoryRepository' should exist");
        assertTrue(applicationContext.containsBean("operationRepository"),
                "Bean name 'operationRepository' should exist");
        assertTrue(applicationContext.containsBean("bankAccountFacade"),
                "Bean name 'bankAccountFacade' should exist");
        assertTrue(applicationContext.containsBean("categoryFacade"),
                "Bean name 'categoryFacade' should exist");
        assertTrue(applicationContext.containsBean("operationFacade"),
                "Bean name 'operationFacade' should exist");
    }
}
