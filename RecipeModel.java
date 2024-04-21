import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeModel {
    private Connection connection;

    public RecipeModel() {
        // Initialize database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/recipes", "root", "Sanju2325");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Recipes");
            while (resultSet.next()) {
                Recipe recipe = new Recipe(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        List.of(resultSet.getString("ingredients").split(",")),
                        resultSet.getString("instructions"),
                        resultSet.getInt("cookingTime"),
                        resultSet.getString("difficultyLevel"),
                        resultSet.getDouble("averageRating")
                );
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public void addRecipe(Recipe recipe) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Recipes (title, ingredients, instructions, cookingTime, difficultyLevel, averageRating) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, recipe.getTitle());
            statement.setString(2, String.join(",", recipe.getIngredients()));
            statement.setString(3, recipe.getInstructions());
            statement.setInt(4, recipe.getCookingTime());
            statement.setString(5, recipe.getDifficultyLevel());
            statement.setDouble(6, recipe.getAverageRating());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRecipe(Recipe recipe) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Recipes SET title=?, ingredients=?, instructions=?, cookingTime=?, difficultyLevel=?, averageRating=? WHERE id=?");
            statement.setString(1, recipe.getTitle());
            statement.setString(2, String.join(",", recipe.getIngredients()));
            statement.setString(3, recipe.getInstructions());
            statement.setInt(4, recipe.getCookingTime());
            statement.setString(5, recipe.getDifficultyLevel());
            statement.setDouble(6, recipe.getAverageRating());
            statement.setLong(7, recipe.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecipe(Long recipeId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Recipes WHERE id=?");
            statement.setLong(1, recipeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rateRecipe(Long recipeId, double rating) {
        try {
            Recipe recipe = getRecipeById(recipeId);
            double currentRating = recipe.getAverageRating();
    
            // Calculate new average rating
            double newRating = (currentRating + rating) / 2;
    
            PreparedStatement statement = connection.prepareStatement("UPDATE Recipes SET averageRating=? WHERE id=?");
            statement.setDouble(1, newRating);
            statement.setLong(2, recipeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    private Recipe getRecipeById(Long recipeId) {
        Recipe recipe = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Recipes WHERE id=?");
            statement.setLong(1, recipeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                recipe = new Recipe(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        List.of(resultSet.getString("ingredients").split(",")),
                        resultSet.getString("instructions"),
                        resultSet.getInt("cookingTime"),
                        resultSet.getString("difficultyLevel"),
                        resultSet.getDouble("averageRating")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }
}
