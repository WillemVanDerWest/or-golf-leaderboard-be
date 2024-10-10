package za.co.theor.GolfLeaderBoard.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.theor.GolfLeaderBoard.Repository.PlayerRepository;
import za.co.theor.GolfLeaderBoard.Repository.RoundScoreRepository;
import za.co.theor.GolfLeaderBoard.model.FrontEndModel;

import java.util.List;

@Service
public class FrontEndModelService {

    @Autowired
    private GetPlayersData getPlayersData;


    private final PlayerRepository playerRepository;

    public FrontEndModelService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public FrontEndModel assignFrontEndModel(String name){
            FrontEndModel player01 = new FrontEndModel();

            player01.setPlayerName(name);
            player01.setRank(getPlayersData.getPlayerRank(name));
            player01.setLastCourseStroke(getPlayersData.getMostRecentDateForPlayer(name).getCourseScore());
            player01.setLastScorePersonalStroke(getPlayersData.getMostRecentDateForPlayer(name).getScore());
            player01.setHandicap(getPlayersData.getHandicapForPlayer(name));
        return player01;
    }

    public List<FrontEndModel> buildListOfAllPlayersData(){
        var names = playerRepository.findAll();
        List<FrontEndModel> listOfAllPlayersData = new java.util.ArrayList<>(List.of());
        for (int i = 0; i < names.size(); i++) {
           listOfAllPlayersData.add(assignFrontEndModel(names.get(i).getName()));
        }

        return listOfAllPlayersData;
    }
}
