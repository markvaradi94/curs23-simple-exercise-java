package ro.fasttrackit.curs23simpleexercise.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "vacations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "location")
    private String location;

    @Column(name = "price")
    private Integer price;

    @Column(name = "duration")
    private Integer duration;

    @Builder
    public Vacation(String location, Integer price, Integer duration) {
        this.location = location;
        this.price = price;
        this.duration = duration;
    }
}
