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
import sg.edu.nus.iss.vttp2022_project.repository.RecipeRepository;

@Service
public class RecipeService {
    
    @Autowired
    private RecipeRepository likedRecipeRepo;

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

    // just trying
    public String randomiseARecipeId() {
	
        List<String> recipeIdList = new LinkedList<>();
        recipeIdList.add("d2fe0d280de91cd9cd5f3781fc5441a3");
        recipeIdList.add("9b4b3972ae2d7ebcb88f37abfbb65a33");
        recipeIdList.add("353aa6aeaf25e64e79067f80dd16513e");
        recipeIdList.add("110880e24455ef15e2de76501fd5d26e");
        recipeIdList.add("20022d91be0968092a8eab1aceee81be");
        recipeIdList.add("ab51e7da2d4500b785c2eabbc724664c");
        recipeIdList.add("05f46d75e1f5c3366592918362371301");
        recipeIdList.add("adf2a40f821c63b55af05157998f3f8d");
        recipeIdList.add("b5a115a74e3aa0b693067a42174d7afb");
        recipeIdList.add("99e85c42da7481abb0b1e58a7503447a");
        recipeIdList.add("adfe1df79c4433a02f80c666f3039f29");
        recipeIdList.add("1189c407cab16cfb2c21a66e0cd55e9b");
        recipeIdList.add("c73dc8121dbf545e504721b1a32fb137");
        recipeIdList.add("9f7601d8128273e4612af5accde1ddac");
        recipeIdList.add("72790cad14ca5813b4f283820cf75600");
        
        int min = 0;
        int max = 14;
        int randomInt = (int)Math.floor(Math.random()*(max-min+1)+min);
        System.out.println(randomInt);
        String randomisedRecipeId = recipeIdList.get(randomInt);

        return randomisedRecipeId;
    }
    
}
