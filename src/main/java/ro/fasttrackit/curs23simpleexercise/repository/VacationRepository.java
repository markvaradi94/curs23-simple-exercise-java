package ro.fasttrackit.curs23simpleexercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fasttrackit.curs23simpleexercise.domain.Vacation;

import java.util.List;

public interface VacationRepository extends JpaRepository<Vacation, Integer> {

    List<Vacation> findVacationsByLocationIgnoreCase(String location);

    List<Vacation> findVacationsByPrice(Integer maxPrice);
}
