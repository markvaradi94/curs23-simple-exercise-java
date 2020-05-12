package ro.fasttrackit.curs23simpleexercise.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ro.fasttrackit.curs23simpleexercise.domain.Vacation;
import ro.fasttrackit.curs23simpleexercise.exceptions.ResourceNotFoundException;
import ro.fasttrackit.curs23simpleexercise.repository.VacationRepository;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.*;

@Service
public class VacationService {

    private final VacationRepository vacationRepository;

    public VacationService(final VacationRepository vacationRepository) {
        this.vacationRepository = vacationRepository;
    }

    public List<Vacation> getAllVacations(String location, Integer maxPrice) {
        if (location != null && maxPrice == null) {
            return Collections.unmodifiableList(vacationRepository.findVacationsByLocationIgnoreCase(location));
        } else if (location == null && maxPrice != null) {
            return Collections.unmodifiableList(vacationRepository.findVacationsByPrice(maxPrice));
        } else {
            return Collections.unmodifiableList(vacationRepository.findAll());
        }
    }

    public Vacation findById(Integer id) {
        return getOrThrow(id);
    }

    public Vacation addVacation(Vacation vacation) {
        return vacationRepository.save(vacation);
    }

    public Vacation deleteVacation(Integer id) {
        Vacation vacationToDelete = getOrThrow(id);
        vacationRepository.deleteById(id);
        return vacationToDelete;
    }

    public Vacation replaceVacation(Integer id, Vacation vacation) {
        Vacation vacationToReplace = getOrThrow(id);
        vacationToReplace.setLocation(vacation.getLocation());
        vacationToReplace.setDuration(vacation.getDuration());
        vacationToReplace.setPrice(vacation.getPrice());
        return vacationRepository.save(vacationToReplace);
    }

    private Vacation getOrThrow(Integer id) {
        return vacationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find vacation with id " + id));
    }
}
