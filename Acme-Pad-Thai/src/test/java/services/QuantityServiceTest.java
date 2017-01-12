package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.Quantity;

import utilities.AbstractTest;
import utilities.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@Transactional
public class QuantityServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private QuantityService quantityService;
	
	//Tests
	
	@Test
	public void testCreateQuantity(){
		Quantity quantity;
		
		quantity = quantityService.createDefaultQuantity();
		
		System.out.println("Quantity" + quantity.getId() + "created");
	}
	
	@Test
	public void testSaveQuantity(){
		authenticate("user1");
		Quantity quantity, saved;
		int quantityId;
		
		quantityId = TestUtils.getIdFromBeanName("quantity1");
		
		quantity = quantityService.findOne(quantityId);
		saved = quantityService.save(quantity);
		quantityService.flush();
		
		System.out.println("Quantity" + saved.getId() + "saved");
		unauthenticate();
	}
	
	@Test
	public void testDeleteQuantity(){
		authenticate("user1");
		Quantity quantity;
		int quantityId;
		
		quantityId = TestUtils.getIdFromBeanName("quantity1");
		
		quantity = quantityService.findOne(quantityId);
		quantityService.delete(quantity);
		
		System.out.println("Quantity deleted");
		unauthenticate();
	}
	
	@Test
	public void testFindOneQuantity(){
		authenticate("user1");
		Quantity quantity;
		int quantityId;
		
		quantityId = TestUtils.getIdFromBeanName("quantity1");
		
		quantity = quantityService.findOne(quantityId);		
		System.out.println("Quantity" + quantity.getId() + "found");
		unauthenticate();
	}

}
