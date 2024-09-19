package za.co.theor.GolfLeaderBoard.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @ToString.Exclude
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<RoundScore> scores;
}
