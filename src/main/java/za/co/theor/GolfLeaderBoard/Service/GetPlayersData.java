package za.co.theor.GolfLeaderBoard.Service;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import za.co.theor.GolfLeaderBoard.Entity.RoundScore;
import za.co.theor.GolfLeaderBoard.Repository.PlayerRepository;
import za.co.theor.GolfLeaderBoard.Repository.RoundScoreRepository;
import za.co.theor.GolfLeaderBoard.model.PlayerNameAndScoreListType;
import za.co.theor.GolfLeaderBoard.model.PlayerScoreAndDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetPlayersData {

    private final RoundScoreRepository roundScoreRepository;

    private final PlayerRepository playerRepository;

    public GetPlayersData(RoundScoreRepository roundScoreRepository, PlayerRepository playerRepository) {
        this.roundScoreRepository = roundScoreRepository;
        this.playerRepository = playerRepository;
    }

    public String getAllPlayersData(){
        var nameAndHandicap = getHandicapAndName();
        return nameAndHandicap.get(0).getName() + " ," + nameAndHandicap.get(1).getName();
    }

    public  List<PlayerNameAndScoreListType> getHandicapAndName(){
        //handicap is calculated by doing the sixmonth thing, adding all that sixmonth scores up and divide it by the total

        var getPlayersData = playerRepository.findAll();
        List<PlayerNameAndScoreListType> players = new ArrayList<>();
        for (int i = 0; i < getPlayersData.size(); i++) {
            var getPlayerScores = getPlayersData.get(i).getScores();
            var sixMonthsAgoDate = LocalDateTime.now().minusMonths(6);

            List<Double> validScores = new ArrayList<>();
            for (int j = 0; j < getPlayerScores.size(); j++) {
                // loop through all the scores, check if they are older than six months and add into a new list with only the scores
                var scoreDate = getPlayerScores.get(j).getDate();

                var score = getPlayerScores.get(j).getScore();
                if (scoreDate.isAfter(sixMonthsAgoDate)) {
                    validScores.add(score);
                }
            }
            var totalScore = 0.0;
            for (int j = 0; j < validScores.size(); j++) {
                totalScore = totalScore + validScores.get(j);
            }
            //add to a list
            PlayerNameAndScoreListType currentPlayer = new PlayerNameAndScoreListType();
            currentPlayer.setHandicap(totalScore / validScores.size());
            currentPlayer.setName(getPlayersData.get(i).getName());

            players.add(currentPlayer);
        }

        return players;
    }

    public Double getHandicapForPlayer(String name){
        var currentPlayerData = playerRepository.findByName(name);
        var sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        var scores = currentPlayerData.getScores();
        var sumOfScores = 0.0;
        var handiCap = 0.0;
        List<Double> validScores = new ArrayList<>();

        for (int i = 0; i < currentPlayerData.getScores().size(); i++) {
            if (scores.get(i).getDate().isAfter(sixMonthsAgo)) {
                validScores.add(scores.get(i).getScore());
            }
        }

        for (int i = 0; i < validScores.size(); i++) {
            sumOfScores += validScores.get(i);
        }

        handiCap = sumOfScores/ validScores.size();

        return handiCap;
    }

    public PlayerScoreAndDate getMostRecentScore(String name){
        var scoreAndDate = getPlayerScores(name);
        return scoreAndDate.getLast();
    }

    public List<PlayerScoreAndDate> getPlayerScores(String name){
        var currentPlayerData = playerRepository.findByName(name);
        var sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        var scores = currentPlayerData.getScores();
        List<PlayerScoreAndDate> validScores = new ArrayList<>();

        for (int i = 0; i < currentPlayerData.getScores().size(); i++) {
            if (scores.get(i).getDate().isAfter(sixMonthsAgo)) {
                PlayerScoreAndDate validScore = new PlayerScoreAndDate();
                validScore.setDate(scores.get(i).getDate());
                validScore.setScore(scores.get(i).getScore());
                validScores.add(validScore);
            }
        }
        return validScores;
    }

    public RoundScore getMostRecentDate(String name){

        var playerData = playerRepository.findByName(name);
        var date01 = playerData.getScores().get(1).getDate();
        var date02 = playerData.getScores().get(2).getDate();

        List<RoundScore> listOfScores = playerData.getScores();

        LocalDateTime maxDate = listOfScores.stream().map(RoundScore::getDate).max(LocalDateTime::compareTo).get();

        var latestData = roundScoreRepository.findByDate(maxDate);

        return latestData;
    }



//    public String getPlayerRanks(){
//
//    }


}
