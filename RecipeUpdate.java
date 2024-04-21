import java.util.List;

public class RecipeUpdate {
    private RecipeModel model;
    private RecipeCatalogueView view;
    
    public RecipeUpdate(RecipeModel model, RecipeCatalogueView view) {
        this.model = model;
        this.view = view;
    }
    
    public void getAllRecipes() {
        List<Recipe> recipes = model.getAllRecipes();
        view.updateRecipeList(recipes); // Call updateRecipeList in the view
    }
}
