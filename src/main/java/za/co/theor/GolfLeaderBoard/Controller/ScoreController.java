package za.co.theor.GolfLeaderBoard.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.theor.GolfLeaderBoard.Entity.Player;
import za.co.theor.GolfLeaderBoard.Entity.RoundScore;
import za.co.theor.GolfLeaderBoard.Repository.PlayerRepository;
import za.co.theor.GolfLeaderBoard.Repository.RoundScoreRepository;
import za.co.theor.GolfLeaderBoard.model.CaptureScoreRequest;
import za.co.theor.GolfLeaderBoard.model.PlayerNameAndScoreListType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    @GetMapping("/{name}")
    public Object calculatePlayerHandicap(@PathVariable String name){
//
//        Player player = playerRepository.findByName(name);
//
//        if (player.getName() == null) {
//            return String.valueOf(new ResponseEntity<>("Could not find that name", HttpStatus.NOT_FOUND));
//        }
//
//        List<RoundScore> playerScores = player.getScores();
//        List<Double> sixMonthScores = new ArrayList<>();
//        double avgScore = 0.0;
//        double totalScore = 0.0;
//
//        LocalDateTime sixMonthsAgoDate = LocalDateTime.now().minusMonths(6);
//
//        for (int i = 0; i < playerScores.size(); i++) {
//            var isPlayerScoreValid = playerScores.get(i).getDate().isAfter(sixMonthsAgoDate);
//            var validPlayerScoreValue = playerScores.get(i).getScore();
//
//            if (isPlayerScoreValid) {
//                sixMonthScores.add(validPlayerScoreValue);
//            }
//        }
//
//        for (int i = 0; i < sixMonthScores.size(); i++) {
//           totalScore= totalScore + sixMonthScores.get(i);
//        }
//        avgScore= totalScore/playerScores.size();


        //find all the players in the list
        //generate the avgScore of all the players
        //return a string of each player name and their average score

        var getAllPlayersData = playerRepository.findAll();
        var listOfPlayers = new ArrayList<>();
        var currentPlayer = new PlayerNameAndScoreListType();

        for (int i = 0; i < getAllPlayersData.size(); i++) {

            var totalScoresAvailableForPlayer = getAllPlayersData.get(i).getScores().size();
            var currentPlayerTotalScore = 0.0;
            var currentPlayerName = getAllPlayersData.get(i).getName();

            for (int j = 0; j < totalScoresAvailableForPlayer; j++) {
                currentPlayerTotalScore = currentPlayerTotalScore + getAllPlayersData.get(i).getScores().get(j).getScore();
            }

            var currentPlayerHandicap = currentPlayerTotalScore/ totalScoresAvailableForPlayer;
            currentPlayer.setHandicap(currentPlayerHandicap);
            currentPlayer.setName(currentPlayerName);

            listOfPlayers.add(i, currentPlayer);
        }





        return listOfPlayers;

    }
}



