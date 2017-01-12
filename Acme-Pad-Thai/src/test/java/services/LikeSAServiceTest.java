package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.LikeSA;
import domain.Recipe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@Transactional
public class LikeSAServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private LikeSAService likeSAService;

	// Supporting services
	@Autowired
	private RecipeService recipeService;

	// Tests
	@Test
	public void testCreateLikeSA() {
		LikeSA likeSA;
		Collection<Recipe> recipes;
		Recipe recipe = null;

		super.authenticate("Nutritionist1");

		recipes = recipeService.findByKeyword("156897-TBtJ");
		for (Recipe r: recipes) {
			if (r.getTicker().equals("156897-TBtJ"))
				recipe = r;
		}
		likeSA = likeSAService.create(recipe);

		super.authenticate(null);

		System.out.println("LikeSA" + likeSA.getId() + "created");
	}

	@Test
	public void testFindOneLikeSA() {
		LikeSA likeSA;

		likeSA = likeSAService.findOne(120);

		System.out.println("LikeSA" + likeSA.getId() + "found");
	}

	@Test
	public void testSaveLikeSA() {
		LikeSA likeSA, saved;

		super.authenticate("Nutritionist2");

		likeSA = likeSAService.findOne(122);
		saved = likeSAService.save(likeSA);
		likeSAService.flush();

		super.authenticate(null);

		System.out.println("LikeSA" + saved.getId() + "saved");
	}

	@Test
	public void testDeleteLikeSA() {
		LikeSA likeSA;

		likeSA = likeSAService.findOne(122);

		likeSAService.delete(likeSA);

		System.out.println("LikeSA deleted");
	}

}
