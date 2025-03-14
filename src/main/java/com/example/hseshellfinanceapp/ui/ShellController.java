package com.example.hseshellfinanceapp.ui;

import com.example.hseshellfinanceapp.ui.handler.HelpHandler;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Controller;

@Controller
public class ShellController implements ApplicationListener<ContextRefreshedEvent> {

    private final HelpHandler helpHandler;

    @Autowired
    public ShellController(HelpHandler helpHandler) {
        this.helpHandler = helpHandler;
    }

    @Bean
    public PromptProvider promptProvider() {
        return () -> new AttributedString("finance-tracker:> ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        displayWelcomeMessage();
    }

    public void displayWelcomeMessage() {
        String banner = """
                 _____ _                            _____             _           
                |  ___(_)_ __   __ _ _ __   ___ ___|_   _| __ __ _  ___| | _____ _ __ 
                | |_  | | '_ \\ / _` | '_ \\ / __/ _ \\| || '__/ _` |/ __| |/ / _ \\ '__|
                |  _| | | | | | (_| | | | | (_|  __/| || | | (_| | (__|   <  __/ |   
                |_|   |_|_| |_|\\__,_|_| |_|\\___\\___||_||_|  \\__,_|\\___|_|\\_\\___|_|   
                
                ===================================================================
                                HSE Shell Finance Application
                ===================================================================
                
                Type 'commands' to see available commands
                Type 'welcome' to see this message again
                Type 'exit' to quit the application
                
                """;

        System.out.println(banner);
    }
}
