package za.co.theor.GolfLeaderBoard.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import za.co.theor.GolfLeaderBoard.Entity.Player;
import za.co.theor.GolfLeaderBoard.Entity.RoundScore;
import za.co.theor.GolfLeaderBoard.Repository.PlayerRepository;
import za.co.theor.GolfLeaderBoard.Repository.RoundScoreRepository;
import za.co.theor.GolfLeaderBoard.model.CaptureScoreRequest;

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
    public RoundScore captureScore(@RequestBody CaptureScoreRequest request){

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

}


