package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Folder;
import domain.Message;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
public class FolderServiceTest extends AbstractTest{
	
	// Service under test --------------------------------
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private ActorService actorService;
	
	// Tests ---------------------------------------------
	
	@Test
	public void testCreateFolder(){
		authenticate("user2");
		
		Folder res;
		Actor actor;
		
		actor = actorService.findByPrincipal();
		res = folderService.create(actor);
		
		Assert.isTrue(res.getName() == null);
		Assert.isTrue(res.isObligatory() == false);
		Assert.isTrue(res.getMessages() == null);
		
		System.out.println("------- TEST CREATE -------");
		System.out.println("Folder created \n");
		
		unauthenticate();
	}
	
	@Test
	public void testFindOneFolder(){
		authenticate("user2");
		
		Folder folder;
		int id = 179;

		folder = folderService.findOne(id);
		
		System.out.println("------- TEST FIND ONE -------");
		System.out.println("Folder whose id is " + id + " found: " + folder + "\n");
		
		unauthenticate();
	}
	
	@Test
	public void testFindAllFolder(){
		authenticate("user2");
		
		Collection<Folder> folders;
		
		folders = folderService.findAll();
		
		System.out.println("------- TEST FIND ALL -------");
		System.out.println(folders.size() + " folders found \n");
		
		unauthenticate();
	}
	
	@Test
	public void testSaveFolder(){
		authenticate("user2");
		
		Folder folder;
		Folder saved;
		Collection<Folder> folders;
		Collection<Message> messages;
		Actor actor;
		
		actor = actorService.findByPrincipal();
		folder = folderService.create(actor);
		messages = new ArrayList<Message>();
		
		folder.setName("NameFolderTest");
		folder.setObligatory(false);
		folder.setActor(actor);
		folder.setMessages(messages);
		
		saved = folderService.save(folder);
		folderService.flush();
		
		folders = folderService.findAll();
		
		Assert.isTrue(folders.contains(saved));
		
		System.out.println("------- TEST SAVE -------");
		System.out.println("Folder saved \n");
		
		unauthenticate();
	}
	
	@Test
	public void testDeleteFolder(){
		authenticate("user2");
		
		Folder folder;

		folder = folderService.findOne(179);
		
		System.out.println("------- TEST DELETE -------");
		
		try{ 
			folderService.delete(folder);
			System.out.println("Folder deleted correctly \n");
		}
		catch(Exception e){
			System.out.println(e + "\n");
		}
		
		unauthenticate();
	}
	
	@Test
	public void testCreateFolderObligatory(){
		Collection<Folder> folders;
		Actor actor;
		
		actor = new Actor();
		folders = folderService.createFolderObligatory(actor);
		actor.setFolders(folders);
		
		Assert.isTrue(actor.getFolders().size()>=3);
		
		System.out.println("------- TEST CREATE FOLDER OBLIGATORY -------");
		
		System.out.println("Obligatory folders created");
		for(Folder f:folders){
			System.out.println("Folder name: " + f.getName() + ", " + f.isObligatory());
		}
		
		System.out.println("\n");
	}
}
