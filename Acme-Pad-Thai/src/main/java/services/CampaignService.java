package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CampaignRepository;
import domain.Actor;
import domain.Bill;
import domain.Campaign;
import domain.Message;
import domain.Priority;

@Service
@Transactional
public class CampaignService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private CampaignRepository campaignRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService actorService;
	@Autowired
	private SponsorService sponsorService;
	@Autowired
	private BillService billService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private PriorityService priorityService;

	// Constructors -----------------------------------------------------------
	public CampaignService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Campaign create() {
		Assert.isTrue(actorService.checkAuthority("SPONSOR"),
				"Only an sponsor could create campaign");

		Campaign res;

		res = new Campaign();

		res.setSponsor(sponsorService.findByPrincipal());
		res.setDisplayed(0);

		return res;
	}

	public Campaign save(Campaign campaign) {
		Assert.notNull(campaign);
		Assert.isTrue(actorService.checkAuthority("SPONSOR"),
				"Only an sponsor could save campaign");
		
		if(campaign.getId()!=0){
			Assert.isTrue(new Date(System.currentTimeMillis()).compareTo(campaign
				.getStartMoment()) < 0, "Could not edit a started campaign");
		}
		
			Assert.isTrue(new Date(System.currentTimeMillis())
					.compareTo(campaign.getStartMoment()) < 0);
			Assert.isTrue(new Date(System.currentTimeMillis())
					.compareTo(campaign.getEndMoment()) < 0);
		Assert.isTrue(campaign.getStartMoment().compareTo(
				campaign.getEndMoment()) < 0);

		return campaignRepository.save(campaign);
	}

	public Campaign saveAnyone(Campaign campaign) {
		Assert.notNull(campaign);
		Campaign originalCampaign;
		originalCampaign=campaignRepository.findOne(campaign.getId());
		Assert.isTrue(campaign.getStartMoment().equals(originalCampaign.getStartMoment()));
		Assert.isTrue(campaign.getEndMoment().equals(originalCampaign.getEndMoment()));
		Assert.isTrue(campaign.getBanners().equals(originalCampaign.getBanners()));
		Assert.isTrue(campaign.getMaxDisplayed()==originalCampaign.getMaxDisplayed());
		Assert.isTrue(campaign.isStar()==originalCampaign.isStar());
		Assert.isTrue(campaign.getBills().equals(originalCampaign.getBills()));
		Assert.isTrue(campaign.getSponsor().equals(originalCampaign.getSponsor()));
		
		return campaignRepository.save(campaign);
	}

	public Campaign saveAdmin(Campaign campaign) {
		Assert.notNull(campaign);
		Assert.isTrue(actorService.checkAuthority("ADMINISTRATOR"),
				"Only an administrator could save campaign");
		
		
		Campaign originalCampaign;
		originalCampaign=campaignRepository.findOne(campaign.getId());
		Assert.isTrue(campaign.getStartMoment().equals(originalCampaign.getStartMoment()));
		Assert.isTrue(campaign.getEndMoment().equals(originalCampaign.getEndMoment()));
		Assert.isTrue(campaign.getBanners().equals(originalCampaign.getBanners()));
		Assert.isTrue(campaign.getMaxDisplayed()==originalCampaign.getMaxDisplayed());
		Assert.isTrue(campaign.isStar()==originalCampaign.isStar());
		Assert.isTrue(campaign.getSponsor().equals(originalCampaign.getSponsor()));
		
		return campaignRepository.save(campaign);
	}
	
	public void flush() {
		campaignRepository.flush();
	}

	public void delete(Campaign campaign) {
		Assert.notNull(campaign);
		Assert.isTrue(actorService.checkAuthority("SPONSOR"),
				"Only an sponsor could delete campaign");
		Assert.isTrue(new Date(System.currentTimeMillis()).compareTo(campaign
				.getStartMoment()) < 0, "Could not delete a started campaign");

		campaignRepository.delete(campaign);
	}

	public Collection<Campaign> findAll() {
		Assert.isTrue(actorService.checkAuthority("ADMINISTRATOR"),
				"Only an admin could search this");
		Collection<Campaign> result;

		result = campaignRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Campaign findOne(int id) {
		Campaign result;

		result = campaignRepository.findOne(id);
		Assert.notNull(result);
		return result;
	}

	public void generateBills() {
		Collection<Campaign> campaigns;
		campaigns = findAll();
		campaigns.removeAll(campaignsWithBillThisMonth());
		for (Campaign c : campaigns) {
			Bill b, b1;
			b = billService.create(c);
			b.setDescription("Bill of month "
					+ b.getCreationMoment().getMonth()+1);
			b1 = billService.save(b);
			c.addBill(b1);
			c.setDisplayed(0);
			saveAdmin(c);
		}
	}

	public Boolean exist(int id) {
		Boolean res;
		res = campaignRepository.exists(id);
		return res;
	}
	
	public void sendBulkMessages(){
		Collection<Actor> debtors;
		Message bulkMessage;
		Priority p;
		
		p = priorityService.findOne(23);
		bulkMessage = messageService.create();
		debtors = sponsorService.delinquentDebtorSponsors();
		
		bulkMessage.setBody("You have unpaid bills");
		bulkMessage.setPriority(p);
		bulkMessage.setSubject("Unpaid bills");
		
		messageService.sendMessage(bulkMessage, actorService.findByPrincipal(), debtors);
	}

	// Other business methods -------------------------------------------------
	public Collection<Campaign> campaignsWithBillThisMonth() {
		Assert.isTrue(actorService.checkAuthority("ADMINISTRATOR"),
				"Only an admin could search this");
		Collection<Campaign> res;
		res = campaignRepository.campaignsWithBillThisMonth();
		return res;
	}

	/** Minimo de campañas de un sponsor **/
	public Integer minCampignsPerSponsor() {
		Assert.isTrue(actorService.checkAuthority("ADMINISTRATOR"),
				"Only an admin could search this");
		Integer res;
		res = campaignRepository.minCampignsPerSponsor();
		return res;
	}

	/** Maximo de campañas de un sponsor **/
	public Integer maxCampignsPerSponsor() {
		Assert.isTrue(actorService.checkAuthority("ADMINISTRATOR"),
				"Only an admin could search this");
		Integer res;
		res = campaignRepository.maxCampignsPerSponsor();
		return res;
	}

	/** Media de campañas por sponsor **/
	public Double avgCampignsPerSponsor() {
		Assert.isTrue(actorService.checkAuthority("ADMINISTRATOR"),
				"Only an admin could search this");
		Double res;
		res = campaignRepository.avgCampignsPerSponsor();
		return res;
	}

	/** Minimo de campañas activas de un sponsor **/
	public Integer minActiveCampignsPerSponsor() {
		Assert.isTrue(actorService.checkAuthority("ADMINISTRATOR"),
				"Only an admin could search this");
		Integer res;
		res = campaignRepository.minActiveCampignsPerSponsor();
		return res;
	}

	/** Maximo de campañas activas de un sponsor **/
	public Integer maxActiveCampignsPerSponsor() {
		Assert.isTrue(actorService.checkAuthority("ADMINISTRATOR"),
				"Only an admin could search this");
		Integer res;
		res = campaignRepository.maxActiveCampignsPerSponsor();
		return res;
	}

	/** Media de campañas activas por sponsor **/
	public Double avgActiveCampignsPerSponsor() {
		Assert.isTrue(actorService.checkAuthority("ADMINISTRATOR"),
				"Only an admin could search this");
		Double res;
		res = campaignRepository.avgActiveCampignsPerSponsor();
		return res;
	}

	public ArrayList<Campaign> findCampaignsActiveWithDisplays() {
		ArrayList<Campaign> res;

		res = campaignRepository.findCampaignsActiveWithDisplays();

		return res;
	}

	public ArrayList<Campaign> findStarCampaignsActiveWithDisplays() {
		ArrayList<Campaign> res;

		res = campaignRepository.findStarCampaignsActiveWithDisplays();

		return res;
	}

	public void incrementDisplayed(Campaign c) {
		Campaign res = c, saved;
		res.setDisplayed(c.getDisplayed() + 1);
		saved = saveAnyone(res);
		Assert.isTrue(res.getDisplayed() == saved.getDisplayed());
	}

	public String displayBanner() {
		String res = "";
		ArrayList<String> banners;
		ArrayList<Campaign> listC = findCampaignsActiveWithDisplays();
		if (listC.size() > 0) {
			Campaign c = listC.get(new Random().nextInt(listC.size()));
			banners = new ArrayList<>(c.getBanners());
			res = banners.get(new Random().nextInt(banners.size()));
			incrementDisplayed(c);
		}
		return res;
	}

	public String displayBannerStar() {
		String res = "";
		ArrayList<String> banners;
		ArrayList<Campaign> listC = findStarCampaignsActiveWithDisplays();
		if (listC.size() > 0) {
			Campaign c = listC.get(new Random().nextInt(listC.size()));
			banners = new ArrayList<>(c.getBanners());
			res = banners.get(new Random().nextInt(banners.size()));
			incrementDisplayed(c);
		}
		return res;
	}
}
