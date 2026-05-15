package financeplanner.planner;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/financial")
public class HelloController {

    private final ChatModel chatModel;
    private final ExpenseRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private UserRepository userRepository;

    public HelloController(ChatModel chatModel, ExpenseRepository repository) {
        this.chatModel = chatModel;
        this.repository = repository;
    }

    // --- SECURITY CONTEXT HELPER ---
    private User getAuthenticatedUser() {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Nexus Security: Session mismatch."));
    }

    // --- CORE OPERATIONS ---
    @PostMapping("/add")
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        User user = getAuthenticatedUser();
        expense.setUser(user);

        if (expense.getCategory() != null && expense.getCategory().getId() != null) {
            Category cat = categoryRepository.findById(expense.getCategory().getId()).orElse(null);
            expense.setCategory(cat);
        }

        return ResponseEntity.ok(repository.save(expense));
    }

    @GetMapping("/all")
    public List<Expense> getAllExpenses() {
        return repository.findByUserUsername(getAuthenticatedUser().getUsername());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        User user = getAuthenticatedUser();
        return repository.findByIdAndUser(id, user)
                .map(expense -> {
                    repository.delete(expense);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access Denied: You do not own this record."));
    }

    // --- AI ENGINES ---
    @GetMapping("/predict-category")
    public String predictCategory(@RequestParam String description) {
        List<Category> availableCategories = categoryRepository.findAll();
        String categoryList = availableCategories.stream()
                .map(Category::getName)
                .collect(Collectors.joining(", "));

        String prompt = String.format(
                "Task: Classify transaction '%s'. List: [%s]. " +
                        "Rule: Return ONLY the category name. If no match, return 'others'.",
                description, categoryList
        );
        return chatModel.call(prompt).trim();
    }

    @GetMapping("/advice")
    public String getAiAdvice(@RequestParam(required = false) Double goal) {
        User user = getAuthenticatedUser();
        List<Expense> userExpenses = repository.findByUserUsername(user.getUsername());

        if (userExpenses.isEmpty()) return "System Ready. Log your first transaction to begin the audit.";

        Map<String, Double> totals = userExpenses.stream()
                .filter(e -> e.getCategory() != null)
                .collect(Collectors.groupingBy(e -> e.getCategory().getName(),
                        Collectors.summingDouble(Expense::getAmount)));

        var topEntry = totals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get();

        Expense topExpense = userExpenses.stream()
                .filter(e -> e.getCategory() != null && e.getCategory().getName().equals(topEntry.getKey()))
                .findFirst().orElse(null);

        String description = (topExpense != null) ? topExpense.getDescription() : "spending";

        // Professional Prompt with User Context (Email included)
        String prompt = String.format(
                "Act as a Financial Auditor for %s (%s). " +
                        "DATA: Spent ₹%.2f on '%s' in '%s'. " +
                        "TASK: Provide 10 lines of clinical, high-impact optimization advice, avoid negotiation " +
                        "Include: psychological trigger, bulk-saving tip, and a 5-word mantra.",
                user.getUsername(), user.getEmail(), topEntry.getValue(), description, topEntry.getKey()
        );

        return chatModel.call(prompt);
    }
    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
    @PostMapping("/categories/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        // This saves the new category (like 'food') to your PostgreSQL table
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @GetMapping("/top-category")
    public String getTopCategory() {
        User user = getAuthenticatedUser();
        List<Expense> expenses = repository.findByUserUsername(user.getUsername());

        if (expenses.isEmpty()) return "None";

        return expenses.stream()
                .filter(e -> e.getCategory() != null)
                .collect(Collectors.groupingBy(e -> e.getCategory().getName(), Collectors.summingDouble(Expense::getAmount)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("General");
    }
    // --- USER-SPECIFIC SETTINGS ---
    @GetMapping("/budget")
    public Double getBudget() {
        User user = getAuthenticatedUser();
        // Fixed: We now find the budget specific to the logged-in user
        return settingsRepository.findByUserId(user.getId())
                .map(Settings::getBudgetGoal)
                .orElse(5000.0);
    }

    @PostMapping("/budget/save")
    public void saveBudget(@RequestParam Double goal) {
        User user = getAuthenticatedUser();
        Settings s = settingsRepository.findByUserId(user.getId()).orElse(new Settings());
        s.setBudgetGoal(goal);
        s.setUser(user); // Tie the settings to the user
        settingsRepository.save(s);
    }
}