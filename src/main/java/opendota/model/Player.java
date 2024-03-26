package opendota.model;

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

    @ManyToMany
    private Set<Match> matches;

}
