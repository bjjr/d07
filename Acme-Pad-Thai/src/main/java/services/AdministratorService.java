package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Administrator;

import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class AdministratorService {
	
	// Managed repository -----------------------------------
	
	@Autowired
	private AdministratorRepository administratorRepository;
	
	@Autowired
	private ActorService actorService;
	
	// Constructors -----------------------------------------
	
	public AdministratorService(){
		super();
	}

	// Simple CRUD methods ----------------------------------
	
	public Administrator save(Administrator admin) {
		Assert.isTrue(actorService.checkAuthority("ADMINISTRATOR"));
		Assert.notNull(admin);
		
		Administrator result;
		
		result = administratorRepository.save(admin);
		
		return result;
	}
	
	// Other business methods -------------------------------
	
	public Administrator findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);

		Administrator result;

		result = administratorRepository.findByUserAccountId(userAccount.getId());		

		return result;
	}
	
	public Administrator findByPrincipal(){
		Administrator result;
		UserAccount userAccount;
		
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = administratorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		
		return result;
	}
	
}
