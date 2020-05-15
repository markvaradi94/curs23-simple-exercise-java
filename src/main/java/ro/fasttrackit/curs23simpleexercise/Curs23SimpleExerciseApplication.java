package ro.fasttrackit.curs23simpleexercise;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ro.fasttrackit.curs23simpleexercise.menu.MainMenu;
import ro.fasttrackit.curs23simpleexercise.domain.Vacation;
import ro.fasttrackit.curs23simpleexercise.repository.VacationRepository;
import ro.fasttrackit.curs23simpleexercise.service.VacationService;

@SpringBootApplication
public class Curs23SimpleExerciseApplication {

    public static void main(String[] args) {
        SpringApplication.run(Curs23SimpleExerciseApplication.class, args);
    }

    @Bean
    CommandLineRunner atStartup(VacationRepository vacationRepository) {
        return args -> {
            vacationRepository.save(Vacation.builder().location("Dubai").price(3100).duration(10).build());
            vacationRepository.save(Vacation.builder().location("Paris").price(5400).duration(14).build());
            vacationRepository.save(Vacation.builder().location("London").price(2300).duration(4).build());
            vacationRepository.save(Vacation.builder().location("Moscow").price(7900).duration(6).build());
            vacationRepository.save(Vacation.builder().location("Paris").price(3000).duration(5).build());
            vacationRepository.save(Vacation.builder().location("Dubai").price(1800).duration(5).build());
            vacationRepository.save(Vacation.builder().location("Tokyo").price(6800).duration(8).build());
            vacationRepository.save(Vacation.builder().location("Istanbul").price(2000).duration(7).build());
            vacationRepository.save(Vacation.builder().location("London").price(2400).duration(5).build());
            vacationRepository.save(Vacation.builder().location("Paris").price(3900).duration(8).build());
        };
    }

    @Bean
    CommandLineRunner menu(VacationService vacationService) {
        return args -> {
            MainMenu menu = new MainMenu(vacationService);
            menu.run();
        };
    }

}
