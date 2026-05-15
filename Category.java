package financeplanner.planner;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories") // Explicit table name for clarity in PostgreSQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // Ensures category names are never null or duplicated
    private String name;
}