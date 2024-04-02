package opendota.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import jakarta.persistence.*;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;

    private String personalName;
    @JsonIgnore
    @ManyToMany(mappedBy = "players")
    private Set<Match> matches;


    public void addMatch(Match match){
        matches.add(match);
    }
}
