package ro.fasttrackit.curs23simpleexercise.domain;

import de.vandermeer.asciitable.AsciiTable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ro.fasttrackit.curs23simpleexercise.exceptions.ResourceNotFoundException;
import ro.fasttrackit.curs23simpleexercise.service.VacationService;

import java.util.List;
import java.util.Scanner;

@Component
public class MainMenu {

    private final VacationService vacationService;
    private final RestTemplate rest = new RestTemplate();
    private final Scanner scanner = new Scanner(System.in);
    private final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/vacations/");

    public MainMenu(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    public void run() {
        int input = 0;
        do {
            printMainMenu();
            input = getInput();
            executeInput(input);
        } while (input != 7);
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("===========================================================================");
        System.out.println("(1) List all vacations");
        System.out.println("(2) Get vacations for a specific location");
        System.out.println("(3) Get vacations with maximum price of");
        System.out.println("(4) Add a new vacation");
        System.out.println("(5) Delete a vacation by id");
        System.out.println("(6) Update an existing vacation");
        System.out.println("(7) Exit");
        System.out.println("===========================================================================");
    }

    private int getInput() {
        System.out.print("Select your option: ");
        return scanner.nextInt();
    }

    private void executeInput(int input) {
        switch (input) {
            case 1:
                handleListAllVacations();
                break;
            case 2:
                handleVacationsFromLocation();
                break;
            case 3:
                handleVacationsUnderAmount();
                break;
            case 4:
                handleAddVacation();
                break;
            case 5:
                handleDeleteVacation();
                break;
            case 6:
                handleUpdateVacation();
                break;
            case 7:
                handleExit();
                break;
            default:
                System.out.println("Invalid option selected");
        }
    }

    private void handleListAllVacations() {
        printOptionResult(builder);
    }

    private void handleVacationsFromLocation() {
        System.out.print("Enter location: ");
        String location = scanner.next();

        UriComponentsBuilder requestBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/vacations")
                .queryParam("location", location)
                .query("Select * from vacations where location = " + location);

        printOptionResult(requestBuilder);
    }

    private void handleVacationsUnderAmount() {
        System.out.print("Enter maximum price: ");
        Integer maxPrice = scanner.nextInt();

        UriComponentsBuilder requestBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/vacations")
                .queryParam("maxPrice", maxPrice)
                .query("Select * from vacations where price <= " + maxPrice);

        printOptionResult(requestBuilder);
    }

    private void handleAddVacation() {
        Vacation vacationToAdd = newVacationDetails();
        scanner.nextLine();
        rest.postForObject(builder.toUriString(), vacationToAdd, Vacation.class);
    }

    private void handleDeleteVacation() {
        System.out.print("Enter ID of vacation to be deleted: ");
        Integer id = scanner.nextInt();
        try {
            Vacation vacationToDelete = vacationService.findById(id);
            rest.delete(builder.toUriString() + vacationToDelete.getId());
        } catch (ResourceNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void handleUpdateVacation() {
        System.out.print("Enter ID of vacation to be updated: ");
        Integer id = scanner.nextInt();
        scanner.nextLine();
        try {
            Vacation vacationToUpdate = vacationService.findById(id);
            Vacation newVacation = newVacationDetails();
            scanner.nextLine();
            rest.put(builder.toUriString() + vacationToUpdate.getId(), newVacation);
        } catch (ResourceNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void handleExit() {
        System.out.println("Exiting command line vacation service");
    }

    private List<Vacation> getListOfVacations(UriComponentsBuilder builder) {
        return rest.exchange(builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(null),
                new ParameterizedTypeReference<List<Vacation>>() {
                })
                .getBody();
    }

    private Vacation newVacationDetails() {
        System.out.print("Enter location: ");
        String location = scanner.next();
        System.out.print("Enter price: ");
        Integer price = scanner.nextInt();
        System.out.print("Enter duration: ");
        Integer duration = scanner.nextInt();
        return new Vacation(location, price, duration);
    }

    private void renderAsciiTable(List<Vacation> vacations) {
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow("ID", "Location", "Price", "Duration");
        asciiTable.addRule();
        vacations.forEach(vacation -> asciiTable.addRow(vacation.getId(), vacation.getLocation(),
                vacation.getPrice(), vacation.getDuration()));
        asciiTable.addRule();
        System.out.println(asciiTable.render());
    }

    private void printOptionResult(UriComponentsBuilder requestBuilder) {
        List<Vacation> vacations = getListOfVacations(requestBuilder);
        renderAsciiTable(vacations);
    }

}
