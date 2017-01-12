package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.SpamWord;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
public class SpamWordServiceTest extends AbstractTest{
	
	// Service under test --------------------------------
	
	@Autowired
	private SpamWordService spamWordService;
	
	// Tests ---------------------------------------------
		
	@Test
	public void testCreateSpamWord(){
		authenticate("administrator1");
		
		SpamWord res;
			
		res = spamWordService.create();
			
		Assert.isTrue(res.getWord() == null);
		
		System.out.println("------- TEST CREATE -------");
		System.out.println("SpamWord created \n");
			
		unauthenticate();
	}
		
	@Test
	public void testFindOneSpamWord(){
		authenticate("administrator1");
			
		SpamWord spamWord;
		int id = 20;

		spamWord = spamWordService.findOne(id);
		System.out.println("------- TEST FIND ONE -------");
		System.out.println("SpamWord whose id is " + id + " found: " + spamWord + "\n");
			
		unauthenticate();
	}
		
	@Test
	public void testFindAllSpamWord(){
		authenticate("administrator1");
			
		Collection<SpamWord> spamWords;
			
		spamWords = spamWordService.findAll();
			
		System.out.println("------- TEST FIND ALL -------");
		System.out.println(spamWords.size() + " spamWords found \n");
		
		unauthenticate();
	}
		
	@Test
	public void testSaveSpamWord(){
		authenticate("administrator1");
			
		SpamWord spamWord;
		SpamWord saved;
		Collection<SpamWord> spamWords;
			
		spamWord = spamWordService.create();
			
		spamWord.setWord("spamWordTest");
			
		saved = spamWordService.save(spamWord);
		spamWordService.flush();
		spamWords = spamWordService.findAll();
		
		
		Assert.isTrue(spamWords.contains(saved));
			
		System.out.println("------- TEST SAVE -------");
		System.out.println("SpamWord saved \n");
			
		unauthenticate();
	}
		
	@Test
	public void testDeleteSpamWord(){
		authenticate("administrator1");
		
		SpamWord spamWord;

		spamWord = spamWordService.findOne(20);
		
		System.out.println("------- TEST DELETE -------");
		
		try{
			spamWordService.delete(spamWord);
			System.out.println("SpamWord deleted correctly \n");
		}
		catch(Exception e){
			System.out.println(e + "\n");
		}
			
		unauthenticate();
	}
}
