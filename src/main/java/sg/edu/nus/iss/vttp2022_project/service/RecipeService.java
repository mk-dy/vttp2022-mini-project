package sg.edu.nus.iss.vttp2022_project.service;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.vttp2022_project.model.Recipe;
import sg.edu.nus.iss.vttp2022_project.repository.LikedRecipeRepository;

@Service
public class RecipeService {
    
    @Autowired
    private LikedRecipeRepository likedRecipeRepo;

    @Value("${edamam.api.key}")
    private String apiKey;

    @Value("${edamam.api.id}")
    private String apiId;

    private String urlAccessPoint = "https://api.edamam.com/api/recipes/v2";

    public List<Recipe> getRecipeList(String query) {

        List<Recipe> recipeList = new LinkedList<>();
        
        System.out.println(query);

        String url = UriComponentsBuilder.fromUriString(urlAccessPoint)
            .queryParam("app_key", apiKey)
            .queryParam("app_id", apiId)
            .queryParam("q", query)
            .queryParam("type", "public")
            .toUriString();

        RequestEntity<Void> req = RequestEntity
            .get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = null;
        
        try {
            resp = template.exchange(req, String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return recipeList;
        }

        JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
        JsonObject obj = reader.readObject();
        JsonArray jArr = obj.getJsonArray("hits");

        for (int i = 0; i < jArr.size(); i++) {
            Recipe fRecipe= new Recipe();
            JsonObject rec = jArr.getJsonObject(i);
            JsonObject recipeInfo = rec.getJsonObject("recipe");

            String recipeId = recipeInfo.getString("uri");
            recipeId = recipeId.replace("http://www.edamam.com/ontologies/edamam.owl#recipe_", "");
            String recipeName = recipeInfo.getString("label");
            String foodImage = recipeInfo.getJsonObject("images")
                                .getJsonObject("REGULAR")
                                .getString("url");
            String recipeSource = recipeInfo.getString("source");
            String recipeSourceUrl = recipeInfo.getString("url");
            Integer serving = recipeInfo.getInt("yield");
            Integer calories = recipeInfo.getInt("calories");
            
            fRecipe.setRecipeId(recipeId);
            fRecipe.setRecipeName(recipeName);
            fRecipe.setImageLink(foodImage);
            fRecipe.setOrgSrc(recipeSource);
            fRecipe.setOrgSrcLink(recipeSourceUrl);
            fRecipe.setServing(serving);
            fRecipe.setCalories(calories);
            recipeList.add(fRecipe);
            
        }

        return recipeList;
    }

    public Recipe getSingleRecipe(String recipeId) {

        Recipe recipe = new Recipe();

        String urlAccessPoint = "https://api.edamam.com/api/recipes/v2/" + recipeId;

        String url = UriComponentsBuilder.fromUriString(urlAccessPoint)
            .queryParam("app_key", apiKey)
            .queryParam("app_id", apiId)
            .queryParam("type", "public")
            .toUriString();

        RequestEntity<Void> req = RequestEntity
            .get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = null;
        
        try {
            resp = template.exchange(req, String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            return recipe;
        }

        JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
        JsonObject obj = reader.readObject();
        JsonObject recipeInfo = obj.getJsonObject("recipe");

        String recipeId2 = recipeInfo.getString("uri");
        recipeId2 = recipeId2.replace("http://www.edamam.com/ontologies/edamam.owl#recipe_", "");
        String recipeName = recipeInfo.getString("label");
        String foodImage = recipeInfo.getJsonObject("images")
                            .getJsonObject("REGULAR")
                            .getString("url");
        String recipeSource = recipeInfo.getString("source");
        String recipeSourceUrl = recipeInfo.getString("url");
        Integer serving = recipeInfo.getInt("yield");
        Integer calories = recipeInfo.getInt("calories");
        recipe.setRecipeId(recipeId2);
        recipe.setRecipeName(recipeName);
        recipe.setImageLink(foodImage);
        recipe.setOrgSrc(recipeSource);
        recipe.setOrgSrcLink(recipeSourceUrl);
        recipe.setServing(serving);
        recipe.setCalories(calories);

        JsonArray healthLabelArray = recipeInfo.getJsonArray("healthLabels");
        for (int i = 0; i < healthLabelArray.size(); i++) {
            String label = healthLabelArray.getString(i);
            recipe.healthLabels.add(label);
        }

        JsonArray ingredLinesArray = recipeInfo.getJsonArray("ingredientLines");
        for (int i = 0; i < ingredLinesArray.size(); i++) {
            String ingredient = ingredLinesArray.getString(i);
            recipe.ingredientLines.add(ingredient);
        }

        return recipe;
    }

    
    public boolean insertLikedRecipe(Recipe recipe, Integer userId) {
        Optional<List<Recipe>> optRecipeList = likedRecipeRepo.getRecipe(userId);
        System.out.println("check website recipeid: " + recipe.getRecipeId());
        System.out.println("check website name: " + recipe.getRecipeName());
        System.out.println("check website userid: " + userId);
        
        // if table does not contain the recipe at all
        if (optRecipeList.isEmpty()) {
            boolean check = likedRecipeRepo.insertLikedRecipe(recipe, userId);
            return check;
        }
        
        // if table has data
        if (optRecipeList.isPresent()) {
            List<Recipe> recipeList = optRecipeList.get();
            // System.out.println("check database recipeid: " + recipeList.get(0).getRecipeId());
            // System.out.println("check database name: " + recipeList.get(0).getRecipeName());
            // System.out.println("check database userid: " + recipeList.get(0).getUserId());
            // check if list already contains existing recipe

            // for (int i = 0; i < recipeList.size(); i++) {
            //     if (recipe.getRecipeId() == recipeList.get(i).getRecipeId() && userId == recipeList.get(i).getUserId()) {
            //         throw new IllegalArgumentException("Recipe has already been saved previously.");
            //     }
                
            // }
            
            // needs fixing
            for (Recipe recipeCheck: recipeList) {
                if (recipeCheck.getRecipeId() == recipe.getRecipeId()
                    && recipeCheck.getUserId() == userId) {
                    
                    // throw error
                    throw new IllegalArgumentException("Recipe has already been saved previously.");
                }     
            }

            // if table has data but looped and found nothing,
            // insert the data into table
            boolean check = likedRecipeRepo.insertLikedRecipe(recipe, userId);
            return check;
        }
        
        return false;
        

    }

    // if recipe already exists in liked table, prompt error
    // how to check?
    // if recipe id, recipe name and userId already exists in the list of recipes
    // prompt error
    


    
}
