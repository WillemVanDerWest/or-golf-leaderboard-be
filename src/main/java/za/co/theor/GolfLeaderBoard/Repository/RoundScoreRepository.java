package za.co.theor.GolfLeaderBoard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.theor.GolfLeaderBoard.Entity.RoundScore;

@Repository
public interface RoundScoreRepository extends JpaRepository<RoundScore, Long> {
}
