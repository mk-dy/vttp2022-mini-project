package sg.edu.nus.iss.vttp2022_project.service;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.vttp2022_project.model.Recipe;

@Service
public class RecipeService {
    
    @Value("${edamam.api.key}")
    private String apiKey;

    @Value("${edamam.api.id}")
    private String apiId;

    private String urlAccessPoint = "https://api.edamam.com/api/recipes/v2";

    public List<Recipe> getRecipe(String query) {

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

    
}
