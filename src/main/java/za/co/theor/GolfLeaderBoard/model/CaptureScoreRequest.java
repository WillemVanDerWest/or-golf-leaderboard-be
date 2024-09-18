package za.co.theor.GolfLeaderBoard.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CaptureScoreRequest {
    private String name;
    private Double score;
    private LocalDateTime date;

}
