/* AdministratorController.java
 *
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.BillService;
import services.CampaignService;
import services.ContestService;
import services.CookService;
import services.FolderService;
import services.IngredientService;
import services.MasterClassService;
import services.PresentationService;
import services.RecipeService;
import services.SponsorService;
import services.StepService;
import services.TextService;
import services.UserService;
import services.VideoService;
import domain.Administrator;
import domain.Cook;
import domain.Folder;
import domain.User;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	// Services ----------------------------------------------------------

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private StepService stepService;

	@Autowired
	private IngredientService ingredientService;

	@Autowired
	private UserService userService;

	@Autowired
	private ContestService contestService;

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private SponsorService sponsorService;

	@Autowired
	private BillService billService;

	@Autowired
	private MasterClassService masterClassService;

	@Autowired
	private TextService textService;

	@Autowired
	private PresentationService presentationService;

	@Autowired
	private VideoService videoService;

	@Autowired
	private CookService cookService;
	
	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private FolderService folderService;

	// Dashboard
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard() {

		ModelAndView result;
		Map<String, String> queries = new HashMap<String, String>();

		// Queries C
		queries.put("administrator.minRPU", recipeService.findMinRecipesUser()
				.toString());
		queries.put("administrator.avgRPU", recipeService.findAvgRecipesUser()
				.toString());
		queries.put("administrator.maxRPU", recipeService.findMaxRecipesUser()
				.toString());
		queries.put("administrator.avgNSPR", stepService.findAvgStepsRecipe()
				.toString());
		queries.put("administrator.sdNSPR", stepService
				.findStandardDeviationStepsRecipe().toString());
		queries.put("administrator.avgNIPR", ingredientService
				.findAvgIngredientPerRecipe().toString());
		queries.put("administrator.sdNIPR", ingredientService
				.findStandardDeviationIngredientPerRecipe().toString());

		queries.put("administrator.avgRCpC", contestService
				.avgRecipeCopyPerContest().toString());
		queries.put("administrator.minRCpC", contestService
				.minRecipeCopyPerContest().toString());
		queries.put("administrator.maxRCpC", contestService
				.maxRecipeCopyPerContest().toString());
		queries.put("administrator.CmRCc", contestService
				.findContestMoreRecipesQualified().toString());

		// Queries B -------------------------------------------------------
		queries.put("administrator.avgCpS", campaignService
				.avgCampignsPerSponsor().toString());
		queries.put("administrator.maxCpS", campaignService
				.maxCampignsPerSponsor().toString());
		queries.put("administrator.minCpS", campaignService
				.minCampignsPerSponsor().toString());
		queries.put("administrator.avgACpS", campaignService
				.avgActiveCampignsPerSponsor().toString());
		queries.put("administrator.maxACpS", campaignService
				.maxActiveCampignsPerSponsor().toString());
		queries.put("administrator.minACpS", campaignService
				.minActiveCampignsPerSponsor().toString());
		queries.put("administrator.avgPB", billService.avgPaidBills()
				.toString());
		queries.put("administrator.stdPB", billService.stddevPaidBills()
				.toString());
		queries.put("administrator.avgUB", billService.avgUnpaidBills()
				.toString());
		queries.put("administrator.stdUB", billService.stddevUnpaidBills()
				.toString());

		// Queries A -------------------------------------------------------
		queries.put("administrator.minMCpCk", masterClassService
				.findMinNumMasterClassesPerCook().toString());
		queries.put("administrator.maxMCpCk", masterClassService
				.findMaxNumMasterClassesPerCook().toString());
		queries.put("administrator.avgMCpCk", masterClassService
				.findAvgNumMasterClassesPerCook().toString());
		queries.put("administrator.stdMCpCk", masterClassService
				.findStddevNumMasterClassesPerCook().toString());
		queries.put("administrator.avgVpMC", videoService.findAvgNumVideo()
				.toString());
		queries.put("administrator.avgPpMC", presentationService
				.findAvgNumPresentation().toString());
		queries.put("administrator.avgTpMC", textService.findAvgNumText()
				.toString());
		queries.put("administrator.numProMC", masterClassService
				.findNumPromotedMasterClasses().toString());
		queries.put("administrator.avgProMcpCk", cookService
				.findAvgPromotedMasterClasses().toString());
		queries.put("administrator.avgDemMcpCk", cookService
				.findAvgDemotedMasterClasses().toString());

		// List queries ----------------------------------------------------

		// Queries C
		Collection<User> usersAuthMoRecipe = userService
				.findUsersAuthoredMoreRecipes();
		Collection<User> usersOrderPop = userService.listUsersPopularity();
		Collection<User> usersOrderLike = userService.listUsersAverageLikes();
		Collection<User> usersOrderDislike = userService
				.listUsersAverageDislikes();

		// Queries B
		Collection<String> compByCampaigns = sponsorService
				.companiesByNumCampaigns();
		Collection<String> compByBills = sponsorService.companiesByNumBills();
		Collection<String> inactiveSponsors = sponsorService.inactiveSponsors();
		Collection<String> compSpentLess = sponsorService
				.companiesSpentLessThanAverage();
		Collection<String> compSpentLeast = sponsorService
				.companiesSpentAtLeastNinety();

		// Queries A -------------------------------------------------------
		Collection<Cook> cooksByProMC = cookService
				.findAllOrderByNumPromotedMasterClasses();

		result = new ModelAndView("administrator/dashboard");
		result.addObject("queries", queries);

		result.addObject("compByCampaigns", compByCampaigns);
		result.addObject("compByBills", compByBills);
		result.addObject("inactiveSponsors", inactiveSponsors);
		result.addObject("compSpentLess", compSpentLess);
		result.addObject("compSpentLeast", compSpentLeast);
		result.addObject("compSpentLeast", compSpentLeast);
		result.addObject("cooksByProMC", cooksByProMC);
		result.addObject("usersAuthMoRecipe", usersAuthMoRecipe);
		result.addObject("usersOrderPop", usersOrderPop);
		result.addObject("usersOrderLike", usersOrderLike);
		result.addObject("usersOrderDislike", usersOrderDislike);

		return result;
	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Administrator principal;

		principal = administratorService.findByPrincipal();
		result = createEditModelAndView(principal);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Administrator admin, BindingResult binding) {
		ModelAndView result;
		String password;
		String newPassword;
		Md5PasswordEncoder encoder;
		Collection<Folder> folders;

		if (admin.getId() == 0) {
			folders = folderService.createFolderObligatory(admin);
			admin.setFolders(folders);
		}

		if (binding.hasErrors()) {
			result = createEditModelAndView(admin);
		} else {
			try {
				encoder = new Md5PasswordEncoder();
				password = admin.getUserAccount().getPassword();
				newPassword = encoder.encodePassword(password, null);
				admin.getUserAccount().setPassword(newPassword);

				administratorService.save(admin);
				result = new ModelAndView("redirect:../welcome/index.do");
				result.addObject("message", "user.commit.ok");
			} catch (Throwable oops) {
				result = createEditModelAndView(admin, "user.commit.error");
			}
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(Administrator admin) {
		ModelAndView result;

		result = createEditModelAndView(admin, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Administrator admin, String message) {
		ModelAndView result;

		result = new ModelAndView("administrator/edit");
		result.addObject("admin", admin);
		result.addObject("message", message);

		return result;
	}

}