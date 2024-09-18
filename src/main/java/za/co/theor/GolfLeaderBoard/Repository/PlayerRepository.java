package za.co.theor.GolfLeaderBoard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.theor.GolfLeaderBoard.Entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByName(String name);

}
