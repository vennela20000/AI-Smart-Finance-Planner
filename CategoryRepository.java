package financeplanner.planner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Used to retrieve a category object to link with an Expense
    Category findByName(String name);

    // ✅ NEW: Defensive check for AI Auto-Categorization
    // Use this to check if a category exists before attempting findByName
    boolean existsByName(String name);
}