package ro.fasttrackit.curs23simpleexercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fasttrackit.curs23simpleexercise.domain.Vacation;

public interface VacationRepository extends JpaRepository<Vacation, Integer> {
}
