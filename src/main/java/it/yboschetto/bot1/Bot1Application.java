package it.yboschetto.bot1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
public class Bot1Application {

	public static void main(String[] args) {
		
		ApiContextInitializer.init();
		TelegramBotsApi botsApi=new TelegramBotsApi();
		
		 try {
	            botsApi.registerBot(new SolarBot());
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
		 
		SpringApplication.run(Bot1Application.class, args);
	}

}
