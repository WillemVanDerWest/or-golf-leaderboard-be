package za.co.theor.GolfLeaderBoard.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlayerScoreAndDate {
    private Double score;
    private LocalDateTime date;
}
