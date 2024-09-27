package za.co.theor.GolfLeaderBoard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.theor.GolfLeaderBoard.Entity.Player;
import za.co.theor.GolfLeaderBoard.Entity.RoundScore;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoundScoreRepository extends JpaRepository<RoundScore, Long> {

    RoundScore findByDate(LocalDateTime date);

}
