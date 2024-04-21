import java.util.List;

public class RecipeController {
    private RecipeModel model;
    private RecipeCatalogueView view; // Add reference to the view

    public RecipeController(RecipeModel model, RecipeCatalogueView view) {
        this.model = model;
        this.view = view;
    }

    public void getAllRecipes() {
        List<Recipe> recipes = model.getAllRecipes();
        view.updateRecipeList(recipes); // Call updateRecipeList in the view
    }

    public void addRecipe(Recipe recipe) {
        model.addRecipe(recipe);
        getAllRecipes();
    }

    public void updateRecipe(Recipe recipe) {
        model.updateRecipe(recipe);
        getAllRecipes();
    }

    public void deleteRecipe(Long recipeId) {
        model.deleteRecipe(recipeId);
        getAllRecipes();
    }

    public void rateRecipe(Long recipeId, double rating) {
        model.rateRecipe(recipeId, rating);
        getAllRecipes();
    }
}
