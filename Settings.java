package financeplanner.planner;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settings") // Explicitly naming the table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

    @Id
    private Long id = 1L; // Ensures we only ever have one global configuration row

    private Double budgetGoal = 5000.0;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}