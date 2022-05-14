package sg.edu.nus.iss.vttp2022_project.model;

import java.util.LinkedList;
import java.util.List;

public class Recipe {
    
    private String recipeId;
    private String recipeName;
    private String imageLink;
    private String orgSrc;
    private String orgSrcLink;
    private Integer serving;
    private Integer calories;
    public List<String> ingredientLines = new LinkedList<>();
    public List<String> healthLabels = new LinkedList<>();
    private Integer userId;

    public String getRecipeName() {
        return recipeName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public String getImageLink() {
        return imageLink;
    }
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
    public String getOrgSrc() {
        return orgSrc;
    }
    public void setOrgSrc(String orgSrc) {
        this.orgSrc = orgSrc;
    }
    public String getOrgSrcLink() {
        return orgSrcLink;
    }
    public void setOrgSrcLink(String orgSrcLink) {
        this.orgSrcLink = orgSrcLink;
    }
    public Integer getServing() {
        return serving;
    }
    public void setServing(Integer serving) {
        this.serving = serving;
    }
    public Integer getCalories() {
        return calories;
    }
    public void setCalories(Integer calories) {
        this.calories = calories;
    }
    public String getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
    public List<String> getIngredientLines() {
        return ingredientLines;
    }
    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }
    public List<String> getHealthLabels() {
        return healthLabels;
    }
    public void setHealthLabels(List<String> healthLabels) {
        this.healthLabels = healthLabels;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
}
