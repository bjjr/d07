package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Curriculum;
import domain.Endorser;
import domain.Nutritionist;

import repositories.CurriculumRepository;

@Service
@Transactional
public class CurriculumService {
	
	// Managed repository -----------------------------------
	
	@Autowired
	private CurriculumRepository curriculumRepository;
	
	// Supporting services ----------------------------------
	
	@Autowired
	private EndorserService endorserService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private NutritionistService nutritionistService;
	
	// Constructors -----------------------------------------
	
	public CurriculumService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------
	
	public Curriculum create(){
		Assert.isTrue(actorService.checkAuthority("NUTRITIONIST"));
		Curriculum result;
		
		result = new Curriculum();
		
		return result;
	}
	
	public Curriculum findOne(int curriculumID){
		Assert.isTrue(actorService.checkAuthority("NUTRITIONIST"));
		Curriculum result;
	
		result = curriculumRepository.findOne(curriculumID);
		Assert.notNull(result);
		
		return result;
	}
	
	public Collection<Curriculum> findAll(){
		Assert.isTrue(actorService.checkAuthority("NUTRITIONIST"));
		Collection<Curriculum> result;
		
		result = curriculumRepository.findAll();
		Assert.notNull(result);
		
		return result;
	}
	
	public Curriculum save(Curriculum curriculum){
		Assert.isTrue(actorService.checkAuthority("NUTRITIONIST") || 
				(actorService.checkAuthority("ADMINISTRATOR")));
		Assert.notNull(curriculum);
		
		Curriculum result;
		Nutritionist nutritionist;
		
		nutritionist = nutritionistService.findByPrincipal();
		result = curriculumRepository.save(curriculum);
		nutritionist.setCurriculum(result);
		nutritionistService.save(nutritionist);
		
		return result;
	}
	
	public void flush() {
		curriculumRepository.flush();
	}
	
	public void delete(Curriculum curriculum){
		Assert.isTrue(actorService.checkAuthority("NUTRITIONIST"));
		Assert.notNull(curriculum);
		Assert.isTrue(curriculum.getId() != 0);

		Assert.isTrue(curriculumRepository.exists(curriculum.getId()));
		
		Collection<Endorser> endorsers;
		Nutritionist nutritionist;
		
		endorsers = curriculum.getEndorsers();
		nutritionist = nutritionistService.findByPrincipal();
		
		for(Endorser e:endorsers){
			e.getCurricula().remove(curriculum);
			endorserService.save(e);
		}
		
		nutritionist.setCurriculum(null);
		nutritionistService.save(nutritionist);
		
		curriculumRepository.delete(curriculum);
	}
	
	// Other business methods -------------------------------

}
