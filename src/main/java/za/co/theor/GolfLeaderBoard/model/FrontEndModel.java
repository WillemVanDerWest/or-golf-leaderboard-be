package za.co.theor.GolfLeaderBoard.model;

import lombok.Data;

@Data
public class FrontEndModel {

    private String playerName;
    private Double handicap;
    private Double lastScorePersonalStroke;
    private Double lastCourseStroke;
    private Number rank;

}
