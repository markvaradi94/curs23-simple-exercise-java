package ro.fasttrackit.curs23simpleexercise.controller;

import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.curs23simpleexercise.domain.Vacation;
import ro.fasttrackit.curs23simpleexercise.service.VacationService;

import java.util.List;

@RestController
@RequestMapping("vacations")
public class VacationController {

    private final VacationService vacationService;

    public VacationController(final VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping
    public List<Vacation> getAll(@RequestParam(required = false) String location,
                                 @RequestParam(required = false) Integer maxPrice) {
        return vacationService.getAllVacations(location, maxPrice);
    }

    @GetMapping("{id}")
    public Vacation getVacationById(@PathVariable Integer id) {
        return vacationService.findById(id);
    }

    @PostMapping
    public Vacation addVacation(@RequestBody Vacation vacation) {
        return vacationService.addVacation(vacation);
    }

    @PutMapping("{id}")
    public Vacation replaceVacation(@PathVariable Integer id, @RequestBody Vacation vacation) {
        return vacationService.replaceVacation(id, vacation);
    }

    @DeleteMapping("{id}")
    public Vacation deleteVacation(@PathVariable Integer id) {
        return vacationService.deleteVacation(id);
    }
}
