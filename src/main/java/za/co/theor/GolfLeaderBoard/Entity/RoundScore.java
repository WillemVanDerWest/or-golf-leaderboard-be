package za.co.theor.GolfLeaderBoard.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class RoundScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double score;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;
    private LocalDateTime date;
}
