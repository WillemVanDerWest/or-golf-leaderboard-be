package za.co.theor.GolfLeaderBoard.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.theor.GolfLeaderBoard.Entity.Player;
import za.co.theor.GolfLeaderBoard.Entity.RoundScore;
import za.co.theor.GolfLeaderBoard.Repository.PlayerRepository;
import za.co.theor.GolfLeaderBoard.Repository.RoundScoreRepository;
import za.co.theor.GolfLeaderBoard.Service.FrontEndModelService;
import za.co.theor.GolfLeaderBoard.model.CaptureScoreRequest;
import za.co.theor.GolfLeaderBoard.Service.GetPlayersData;
import za.co.theor.GolfLeaderBoard.model.FrontEndModel;

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
        roundScoreRepository.save(score);
        System.out.println("Added a score for " + score.getPlayer().getName());
        return score;
    }

    @Autowired
    private GetPlayersData getPlayersData;

    @GetMapping("/recentscore/{name}")
    public Double getMostRecentScore(@PathVariable String name){
        var recentScore = getPlayersData.getMostRecentDateForPlayer(name).getScore();
        return recentScore;
    }

    @GetMapping("/handicap/{name}")
    public String getHandicap(@PathVariable String name){
        return name + "'s handicap: " + getPlayersData.getHandicapForPlayer(name);
    }

    @GetMapping("/data")
    public Object getPlayerHandicapAndNameAndDate(){

        return getPlayersData.getHandicapAndName();
    }

    @GetMapping("/lastscore/{name}")
    public String getLastScore(@PathVariable String name){
        var date = getPlayersData.getMostRecentDateForPlayer(name);
        return "Latest score is: " + date.getScore() + "and its latest date is: " + date.getDate();
    }

    @Autowired
    private FrontEndModelService frontEndModelService;

    @GetMapping("/{name}/info")
    public FrontEndModel getPlayerFrontEndStats(@PathVariable String name){
        return frontEndModelService.assignFrontEndModel(name);
    }

    @GetMapping("/ranks")
    public List<FrontEndModel> getRanks(){
        var ranks = frontEndModelService.buildListOfAllPlayersData();
        return ranks;
    }
}