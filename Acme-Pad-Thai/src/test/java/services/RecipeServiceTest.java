package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import utilities.TestUtils;
import domain.Contest;
import domain.Recipe;
import domain.RecipeCopy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@Transactional
public class RecipeServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private RecipeService recipeService;
	
	//Supported services
	@Autowired
	private ContestService contestService;
	
	// Tests

	@Test
	public void testCreateRecipe() {
		Recipe recipe;

		super.authenticate("User4");

		recipe = recipeService.create();

		super.authenticate(null);

		System.out.println("Recipe" + recipe.getId() + "created");
	}

	@Test
	public void testSaveRecipe() {
		Recipe recipe, saved;

		super.authenticate("User1");

		recipe = recipeService.findOne(TestUtils.getIdFromBeanName("recipe1"));
		saved = recipeService.save(recipe);
		recipeService.flush();

		super.authenticate(null);

		System.out.println("Recipe" + saved.getId() + "saved");
	}

	@Test
	public void testDeleteRecipe() {
		Recipe recipe;

		super.authenticate("User1");

		recipe = recipeService.findOne(83);

		recipeService.delete(recipe);

		super.authenticate(null);

		System.out.println("Recipe deleted");
	}
	
	@Test
	public void testFindMinRecipesUser(){
		Double d;
		
		super.authenticate("Administrator1");
		
		d = recipeService.findMinRecipesUser();
		
		super.authenticate(null);
		
		System.out.println("The minimun is"+ d);
	}
	
	@Test
	public void testFindAvgRecipesUser(){
		Double d;
		
		super.authenticate("Administrator1");
		
		d = recipeService.findAvgRecipesUser();
		
		super.authenticate(null);
		
		System.out.println("The average is"+ d);
	}
	
	@Test
	public void testFindMaxRecipesUser(){
		Double d;
		
		super.authenticate("Administrator1");
		
		d = recipeService.findMaxRecipesUser();
		
		super.authenticate(null);
		
		System.out.println("The maximun is"+ d);
	}
	
	@Test
	public void testFindAllRecipesGroupByCategory(){
		Collection<Recipe> recipes;
		
		recipes = recipeService.findAllRecipesGroupByCategory();
		
		for(Recipe r : recipes){
			System.out.println("Recipe" + r.getId() + "found");
		}
	}
	
	@Test
	public void testFindRecipeByKeyword(){
		Collection<Recipe> recipes;
		
		recipes = recipeService.findByKeyword("123456-abCD");
		
		System.out.println("Recipes" + recipes + "found");
	}
	
	@Test
	public void testQualifyRecipe(){
		Recipe recipe;
		RecipeCopy recipeCopy;
		Contest contest;
		
		super.authenticate("User4");
		
		recipe = recipeService.findOne(88);
		recipeCopy = recipeService.copyRecipe(recipe);
		contest = contestService.findOne(261);
		
		recipeService.qualifyRecipe(recipeCopy, contest);
		
		System.out.println("The recipe has been qualified for the contest");
		
		
	}
	
	@Test
	public void testRecipesFollows(){
		Collection<Recipe> recipes;
		
		super.authenticate("User1");
		
		recipes = recipeService.recipesFollows();
		
		super.authenticate(null);
		
		for(Recipe r : recipes){
			System.out.println("Recipe" + r.getId() + "found");
		}
	}
	
	@Test
	public void testFindLikes(){
		Integer likes;
		Recipe recipe;
		
		recipe = recipeService.findOne(88);
		likes = recipeService.findLikes(recipe);
		
		System.out.println("This recipe has " + likes + " likes");
	}
	
	@Test
	public void testFindDislikes(){
		Integer dislikes;
		Recipe recipe;
		
		recipe = recipeService.findOne(88);
		dislikes = recipeService.findDislikes(recipe);
		
		System.out.println("This recipe has " + dislikes + " dislikes");
	}

}
