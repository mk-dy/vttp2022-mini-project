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


    // might have to use "url" in place of "query" to accomodate "next page" button
    public List<Recipe> getRecipeList(String url) {

        List<Recipe> recipeList = new LinkedList<>();
        
        // System.out.println(query);

        // String url = UriComponentsBuilder.fromUriString(urlAccessPoint)
        //     .queryParam("app_key", apiKey)
        //     .queryParam("app_id", apiId)
        //     .queryParam("q", query)
        //     .queryParam("type", "public")
        //     .toUriString();

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

    public String getUrlForRecipeSearch(String query) {

        // enables queries with more than one word
        String actualQuery = query.trim();
        actualQuery = actualQuery.replaceAll("\\s", "+");
        System.out.println(actualQuery);

        String url = UriComponentsBuilder.fromUriString(urlAccessPoint)
            .queryParam("app_key", apiKey)
            .queryParam("app_id", apiId)
            .queryParam("q", actualQuery)
            .queryParam("type", "public")
            .toUriString();

        return url;
    }

    public String getUrlForNextPageUsingQuery(String query) {

        String fill = null;

        // enables queries with more than one word
        String actualQuery = query.trim();
        actualQuery = actualQuery.replaceAll("\\s", "+");
        System.out.println(actualQuery);

        String url = UriComponentsBuilder.fromUriString(urlAccessPoint)
            .queryParam("app_key", apiKey)
            .queryParam("app_id", apiId)
            .queryParam("q", actualQuery)
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
            return fill;
        }

        JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
        JsonObject obj = reader.readObject();
        String nextPageUrl;
        JsonObject link = obj.getJsonObject("_links")
                    .getJsonObject("next");
        if (link == null) {
            nextPageUrl = null;
            return nextPageUrl;
        }
        nextPageUrl = obj.getJsonObject("_links")
                    .getJsonObject("next")
                    .getString("href");

        return nextPageUrl;
    }

    public String getUrlForNextPageUsingLink(String url) {

        String fill = null;

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
            return fill;
        }

        JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
        JsonObject obj = reader.readObject();
        String nextPageUrl;
        JsonObject link = obj.getJsonObject("_links")
                    .getJsonObject("next");
        if (link == null) {
            nextPageUrl = null;
            return nextPageUrl;
        }
        nextPageUrl = obj.getJsonObject("_links")
                    .getJsonObject("next")
                    .getString("href");
        
        nextPageUrl = nextPageUrl.replaceAll("%3D", "=");
        return nextPageUrl;
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

    @Transactional
    public boolean insertLikedRecipe(Recipe recipe, Integer userId) {
        Optional<List<Recipe>> optRecipeList = likedRecipeRepo.getRecipe(userId);
        System.out.println("check website recipeid: " + recipe.getRecipeId());
        System.out.println("check website name: " + recipe.getRecipeName());
        System.out.println("check website userid: " + userId);
        
        // if table does not contain any recipes at all, just insert
        if (optRecipeList.isEmpty()) {
            boolean check = likedRecipeRepo.insertLikedRecipe(recipe, userId);
            return check;
        }
        
        // if table has data, check for recipe
        if (optRecipeList.isPresent()) {
            List<Recipe> recipeList = optRecipeList.get();
            try {
                for (Recipe recipeCheck: recipeList) {
                    // == FOR SAME MEMORY LOCATION, .EQUALS() FOR COMPARING THE INNER VALUES
                    if (recipeCheck.getRecipeId().equals(recipe.getRecipeId()) 
                    && recipeCheck.getUserId().equals(userId)) {
                        // throw error
                         throw new IllegalArgumentException("You have already liked this recipe previously!");
                    }     
                }
                // if table has data but looped and found nothing,
                // insert the data into table
                boolean check = likedRecipeRepo.insertLikedRecipe(recipe, userId);
                return check;
                
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }

        }
        return false;
    
    }
    
    public List<Recipe> getLikedRecipes(Integer userId) {
        Optional<List<Recipe>> optRecipeList = likedRecipeRepo.getRecipe(userId);
        if (optRecipeList.isEmpty()) {
            return null;
        }
        List<Recipe> recipeList = optRecipeList.get();
        return recipeList;
    }

    public boolean deleteLikedRecipe(String recipeId) {
        boolean check = likedRecipeRepo.deleteLikedRecipe(recipeId);
        if (check == true) {
            return true;
        }
        return false;

    }

    
}
