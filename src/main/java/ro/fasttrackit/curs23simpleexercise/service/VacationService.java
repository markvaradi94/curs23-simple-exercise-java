package ro.fasttrackit.curs23simpleexercise.service;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ro.fasttrackit.curs23simpleexercise.domain.Vacation;
import ro.fasttrackit.curs23simpleexercise.exceptions.ResourceNotFoundException;
import ro.fasttrackit.curs23simpleexercise.repository.VacationRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;

@Service
public class VacationService {

    private final VacationRepository vacationRepository;
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public VacationService(final VacationRepository vacationRepository, EntityManager entityManager) {
        this.vacationRepository = vacationRepository;
        this.entityManager = entityManager;
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public List<Vacation> getAllVacations(String location, Integer maxPrice) {
        if (location == null && maxPrice == null) {
            return Collections.unmodifiableList(vacationRepository.findAll());
        }
        return entityManager.createQuery(buildCriteria(location, maxPrice)).getResultList();
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
        vacationToReplace.setPrice(vacation.getPrice());
        vacationToReplace.setDuration(vacation.getDuration());
        return vacationRepository.save(vacationToReplace);
    }

    private Vacation getOrThrow(Integer id) {
        return vacationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find vacation with id " + id));
    }

    private CriteriaQuery<Vacation> buildCriteria(String location, Integer maxPrice) {
        CriteriaQuery<Vacation> criteriaQuery = criteriaBuilder.createQuery(Vacation.class);
        Root<Vacation> vacationRoot = criteriaQuery.from(Vacation.class);
        Predicate predicateForLocation = criteriaBuilder.equal(vacationRoot.get("location"), configLocation(location));
        Predicate predicateForPrice = criteriaBuilder.lessThanOrEqualTo(vacationRoot.get("price"), maxPrice);
        Predicate finalPredicate = criteriaBuilder.or(predicateForLocation, predicateForPrice);
        return criteriaQuery.where(finalPredicate);
    }

    private String configLocation(String location) {
        if (location != null) {
            return Character.toUpperCase(location.charAt(0)) +
                    location.substring(1).toLowerCase();
        } else return Strings.EMPTY;
    }

}
