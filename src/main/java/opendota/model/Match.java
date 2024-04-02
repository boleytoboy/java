package opendota.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long matchId;
    private Integer duration;

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REMOVE
    })
    @JoinTable(
            name = "match_player",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Player> players;

}
