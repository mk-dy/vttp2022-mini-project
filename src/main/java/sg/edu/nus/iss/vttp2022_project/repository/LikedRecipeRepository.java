package sg.edu.nus.iss.vttp2022_project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.vttp2022_project.model.Recipe;

import static sg.edu.nus.iss.vttp2022_project.repository.SQLStatements.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class LikedRecipeRepository {
    
    @Autowired
    private JdbcTemplate template;
    
    // returns list of recipeid and recipename given a userid
    public Optional<List<Recipe>> getRecipe(Integer userId) {
        List<Recipe> recipeList = new LinkedList<>();
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_ALL_RECIPES_BY_USERID, userId);

        if (!rs.next()) {
            return Optional.empty();
        }

        rs.beforeFirst();
        while (rs.next()) {
            Recipe recipe = new Recipe();
            recipe.setRecipeId(rs.getString("recipeId"));
            recipe.setRecipeName(rs.getString("recipename"));
            recipe.setImageLink(rs.getString("image"));
            recipe.setUserId(rs.getInt("user_id"));
            recipeList.add(recipe);
        }
        return Optional.of(recipeList);

    }

    // insert our liked recipes into database
    public boolean insertLikedRecipe(Recipe recipe, Integer userId) {
        Integer count = template.update(
            SQL_INSERT_LIKED_RECIPE,
            recipe.getRecipeId(),
            recipe.getRecipeName(),
            recipe.getImageLink(),
            userId
        );
        return count > 0;
    }

    public boolean deleteLikedRecipe(String recipeId) {
        Integer count = template.update(
            SQL_DELETE_RECIPE_BY_RECIPEID,
            recipeId
        );
        return count > 0;
    }
}
