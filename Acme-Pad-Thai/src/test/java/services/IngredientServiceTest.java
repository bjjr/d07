package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.Ingredient;

import utilities.AbstractTest;
import utilities.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
public class IngredientServiceTest extends AbstractTest{
	
	//Service under test
	@Autowired
	private IngredientService ingredientService;
	
	//Tests
	@Test
	public void testCreateIngredient(){
		Ingredient ingredient;
		
		super.authenticate("Nutritionist1");
		
		ingredient = ingredientService.create();
		
		super.authenticate(null);
		
		System.out.println("Ingredient" + ingredient.getId() + "created");
	}
	
	@Test
	public void testFindAllIngredients(){
		Collection<Ingredient> ingredients;
		
		super.authenticate("Nutritionist1");
		
		ingredients = ingredientService.findAll();
		
		super.authenticate(null);
		
		for(Ingredient i : ingredients){
			System.out.println("Ingredient" + i.getId() + "found");
		}	
	}
	
	@Test
	public void testFindOneIngredient(){
		Ingredient ingredient;
		
		int ingredientId;
		
		ingredientId = TestUtils.getIdFromBeanName("ingredient1");
		
		
		ingredient = ingredientService.findOne(ingredientId);
		
		System.out.println("Ingredient" + ingredient.getId() + "found");
	}
	
	@Test
	public void testSaveIngredient(){
		Ingredient ingredient, saved;
		int ingredientId;
		
		ingredientId = TestUtils.getIdFromBeanName("ingredient1");
		
		super.authenticate("Nutritionist1");
		
		ingredient = ingredientService.findOne(ingredientId);
		saved = ingredientService.save(ingredient);
		ingredientService.flush();
		
		super.authenticate(null);
		
		System.out.println("Ingredient" + saved.getId() + "saved");
	}
	
	@Test
	public void testDeleteIngredient(){
		Ingredient ingredient;
		int ingredientId;
		
		ingredientId = TestUtils.getIdFromBeanName("ingredient1");
		
		super.authenticate("Nutritionist1");
		
		ingredient = ingredientService.findOne(ingredientId);
		
		try{
			ingredientService.delete(ingredient);
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		
		super.authenticate(null);
		
		System.out.println("Ingredient deleted");
	}
	
	@Test
	public void testFindAvgIngredientPerRecipe(){
		Double d;
		
		super.authenticate("Administrator1");
		
		d = ingredientService.findAvgIngredientPerRecipe();
		
		super.authenticate(null);
		
		System.out.println("The average is" + d);
	}
	
	@Test
	public void testFindStandardDeviationIngredientPerRecipe(){
		Double d;
		
		super.authenticate("Administrator1");
		
		d = ingredientService.findStandardDeviationIngredientPerRecipe();
		
		super.authenticate(null);
		
		System.out.println("The standard deviation is" + d);
	}
	
	

}
