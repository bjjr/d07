package utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Entity;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class TestUtils {

	public static Map<String,Integer> beans;
	
	/**
	 * Reads the population context of the application and, assuming a sequential behaviour of
	 * ids when populating, maps each bean name read in the xml with its id in the database.
	 */
	public static void mapBeansToIds() {
		beans = new HashMap<String,Integer>();
		
		ApplicationContext populationContext = new ClassPathXmlApplicationContext("classpath:PopulateDatabase.xml");
		
		int beanId = 0;
		for (Entry<String,Object> entry: populationContext.getBeansWithAnnotation(Entity.class).entrySet()) {
			String beanName;
			
			beanName = entry.getKey();
			beanId++;
			
			beans.put(beanName, beanId);
		}
	}
	
	/**
	 * Given the name of a bean, returns the id of such a bean in the database
	 * @param beanName The name of a certain bean
	 * @return The id that is associated to the bean passed as parameter
	 */
	public static int getIdFromBeanName(String beanName) {
		return beans.get(beanName);
	}
}