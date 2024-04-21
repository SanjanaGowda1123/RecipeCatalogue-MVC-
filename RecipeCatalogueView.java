import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class RecipeCatalogueView extends JFrame {
    private JList<String> recipeList;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton rateButton;

    private RecipeController controller;
    private RecipeUpdate update;

    public static void main(String[] args) {
        RecipeModel model = new RecipeModel();
        RecipeController controller = new RecipeController(model, new RecipeCatalogueView());
        List<Recipe> recipes = model.getAllRecipes();
        RecipeCatalogueView view = new RecipeCatalogueView(recipes, controller);
        RecipeUpdate update = new RecipeUpdate(model, view);
        view.setUpdater(update);
        view.setVisible(true);
    }

    public void setUpdater(RecipeUpdate update) {
        this.update = update;
    }

    public RecipeCatalogueView() {
        // Initialize with an empty list and null controller
        this(new ArrayList<>(), null);
    }

    public RecipeCatalogueView(List<Recipe> recipes, RecipeController controller) {
        this.controller = controller;

        setTitle("Recipe Catalog");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold the recipe list
        JPanel panel = new JPanel(new BorderLayout());
        recipeList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(recipeList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create buttons for CRUD operations
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add Recipe");
        updateButton = new JButton("Update Recipe");
        deleteButton = new JButton("Delete Recipe");
        rateButton = new JButton("Rate Recipe");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(rateButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Populate the recipe list
        updateRecipeList(recipes);

        // Add the panel to the frame
        getContentPane().add(panel);

        // Add action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = JOptionPane.showInputDialog("Enter recipe title:");
                String ingredientStr = JOptionPane.showInputDialog("Enter ingredients separated by commas:");
                List<String> ingredients = List.of(ingredientStr.split(","));
                String instructions = JOptionPane.showInputDialog("Enter instructions:");
                int cookingTime = Integer.parseInt(JOptionPane.showInputDialog("Enter cooking time in minutes:"));
                String difficultyLevel = JOptionPane.showInputDialog("Enter difficulty level:");
                double averageRating = Double.parseDouble(JOptionPane.showInputDialog("Enter average rating:"));
                Recipe newRecipe = new Recipe(null, title, ingredients, instructions, cookingTime, difficultyLevel, averageRating);
                controller.addRecipe(newRecipe);
                update.getAllRecipes();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRecipe = recipeList.getSelectedValue();
                if (selectedRecipe != null) {
                    // Extract recipe ID from selected recipe string
                    String[] parts = selectedRecipe.split("\\|");
                    String idStr = parts[0].substring(parts[0].indexOf(":") + 1).trim();
                    Long recipeId = Long.parseLong(idStr);

                    // Show dialog to get updated details
                    String title = JOptionPane.showInputDialog("Enter updated recipe title:");
                    String ingredientStr = JOptionPane.showInputDialog("Enter updated ingredients separated by commas:");
                    List<String> ingredients = List.of(ingredientStr.split(","));
                    String instructions = JOptionPane.showInputDialog("Enter updated instructions:");
                    int cookingTime = Integer.parseInt(JOptionPane.showInputDialog("Enter updated cooking time in minutes:"));
                    String difficultyLevel = JOptionPane.showInputDialog("Enter updated difficulty level:");
                    double averageRating = Double.parseDouble(JOptionPane.showInputDialog("Enter updated average rating:"));
                    Recipe updatedRecipe = new Recipe(recipeId, title, ingredients, instructions, cookingTime, difficultyLevel, averageRating);
                    controller.updateRecipe(updatedRecipe);
                    update.getAllRecipes();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRecipe = recipeList.getSelectedValue();
                if (selectedRecipe != null) {
                    // Extract recipe ID from selected recipe string
                    String[] parts = selectedRecipe.split("\\|");
                    String idStr = parts[0].substring(parts[0].indexOf(":") + 1).trim();
                    Long recipeId = Long.parseLong(idStr);
                    controller.deleteRecipe(recipeId);
                    update.getAllRecipes();
                }
            }
        });

        rateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRecipe = recipeList.getSelectedValue();
                if (selectedRecipe != null) {
                    // Extract recipe ID from selected recipe string
                    String[] parts = selectedRecipe.split("\\|");
                    String idStr = parts[0].substring(parts[0].indexOf(":") + 1).trim();
                    Long recipeId = Long.parseLong(idStr);

                    // Show dialog to get rating
                    double rating = Double.parseDouble(JOptionPane.showInputDialog("Enter rating:"));
                    controller.rateRecipe(recipeId, rating);
                    update.getAllRecipes();
                }
            }
        });
    }

    // Method to update the recipe list with new data
    public void updateRecipeList(List<Recipe> recipes) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Recipe recipe : recipes) {
            String recipeInfo = String.format("ID: %d | Title: %s | Cooking Time: %d | Difficulty: %s | Rating: %.1f",
                    recipe.getId(), recipe.getTitle(), recipe.getCookingTime(), recipe.getDifficultyLevel(), recipe.getAverageRating());
            listModel.addElement(recipeInfo);
        }
        recipeList.setModel(listModel);
    }

    // Method to show a message dialog
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
