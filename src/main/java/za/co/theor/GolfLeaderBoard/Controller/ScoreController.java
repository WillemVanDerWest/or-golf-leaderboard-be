package za.co.theor.GolfLeaderBoard.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.theor.GolfLeaderBoard.Entity.Player;
import za.co.theor.GolfLeaderBoard.Entity.RoundScore;
import za.co.theor.GolfLeaderBoard.Repository.PlayerRepository;
import za.co.theor.GolfLeaderBoard.Repository.RoundScoreRepository;
import za.co.theor.GolfLeaderBoard.model.CaptureScoreRequest;
import za.co.theor.GolfLeaderBoard.Service.GetPlayersData;
import za.co.theor.GolfLeaderBoard.model.PlayerScoreAndDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("api/scoreController")
public class ScoreController {

    private final PlayerRepository playerRepository;

    public ScoreController(PlayerRepository playerRepository, RoundScoreRepository roundScoreRepository) {
        this.playerRepository = playerRepository;
        this.roundScoreRepository = roundScoreRepository;
    }

    private final RoundScoreRepository roundScoreRepository;


    @PostMapping
    public RoundScore captureScore(@RequestBody CaptureScoreRequest request) {

        var player = playerRepository.findByName(request.getName());

        if (player == null) {
            player = new Player();
            player.setName(request.getName());
        }

        var score = new RoundScore();

        score.setPlayer(player);
        score.setDate(request.getDate());
        score.setScore(request.getScore());

        return roundScoreRepository.save(score);
    }


//    @GetMapping("/player/{name}")
//    public double getOneEntry(@PathVariable String name) {
//
//
//        //      for (var score: roundScores)
////        for (int i = 0; i < roundScores.size(); i++) {
////            totalScore = totalScore + roundScores.get(i).getScore();
////        }
//
////        roundScores.forEach(score -> totalScore = totalScore + score.getScore());
//
////        roundScores.stream().reduce();
//
//
//        return avgScore;
//    }

    @GetMapping("/player/{name}")
    public String getByDate(@PathVariable String name) {

        var player = playerRepository.findByName(name);
        var roundScores = player.getScores();
        double totalScore = 0;
        double avgScore = 0;

        //six months ago = current date - 6 months
        //create a new list that only has the data van die vorige ses maande
        //loop deur, transfer alles behalwe enige iets wat 6 maande of ouer is

        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        List<RoundScore> previousSixMonthScores = new ArrayList<>();

        for (int i = 0; i < roundScores.size(); i++) {
            if (roundScores.get(i).getDate().isAfter(sixMonthsAgo)){
                 previousSixMonthScores.add(roundScores.get(i));
            }
        }

        for (var score : previousSixMonthScores) {
            totalScore = totalScore + score.getScore();
        }

        avgScore = totalScore / roundScores.size();

        return name + "'s score: " + avgScore;
    }

    @Autowired
    private GetPlayersData getPlayersData;

    @GetMapping("/recentscore/{name}")
    public PlayerScoreAndDate getMostRecentScore(@PathVariable String name){
        return getPlayersData.getMostRecentScore(name);
    }

    @GetMapping("/handicap/{name}")
    public String getHandicap(@PathVariable String name){
        return name + "'s handicap: " + getPlayersData.getHandicapForPlayer(name);
    }

    @GetMapping("/data")
    public Object getPlayerHandicapAndName(){

        return getPlayersData.getHandicapAndName();
    }

    @GetMapping("/lastscore/{name}")
    public String getLastScore(@PathVariable String name){
        var date = getPlayersData.getMostRecentDate(name);
        return "Latest score is: " + date.getScore() + "and its latest date is: " + date.getDate();
    }
}



