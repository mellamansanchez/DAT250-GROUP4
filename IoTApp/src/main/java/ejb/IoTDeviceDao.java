package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSSessionMode;
import javax.jms.Topic;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entities.IoTDevice;

@Stateless
public class IoTDeviceDao {
	//buscar por nombre
	//buscar por popular
	//buscar por rating
	//obtener todos los devices
	//buscar por categoria
	
	// Injected database connection:
	@PersistenceContext(unitName="Dat250IoTApp")
    private EntityManager em;
	
	
	/*@Inject
	@JMSConnectionFactory("jms/dat250/ConnectionFactory")
	@JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
	private JMSContext context;
	
	@Resource(lookup = "jms/dat250/Topic")
	private Topic topic;*/
	
    // Stores a new device:
    public void persist(IoTDevice device) throws NamingException, JMSException {
        em.persist(device);
        //Send the topic to the JMS Topic
		//context.createProducer().setProperty("topicUser", device.getTopic()).send(topic, device);

    }
    
    public void update(IoTDevice device) {
        em.merge(device);
    }

    // Retrieves all the devices:
	@SuppressWarnings("unchecked")
	public List<IoTDevice> getAllDevices() {
        Query query = em.createQuery("SELECT d FROM IoTDevice d");
        List<IoTDevice> devices = new ArrayList<IoTDevice>();
        devices = query.getResultList();
        return devices;
    }
	
	@SuppressWarnings("unchecked")
	public List<IoTDevice> getPopularDevices() {
        Query query = em.createQuery("SELECT d FROM IoTDevice d ORDER BY d.numFollowers DESC");
        List<IoTDevice> devices = new ArrayList<IoTDevice>();
        devices = query.getResultList();
        return devices;
    }
	
	@SuppressWarnings("unchecked")
	public List<IoTDevice> getTopRatedDevices() {
        Query query = em.createQuery("SELECT d FROM IoTDevice d ORDER BY d.averageRating DESC");
        List<IoTDevice> devices = new ArrayList<IoTDevice>();
        devices = query.getResultList();
        return devices;
    }
	
	@SuppressWarnings("unchecked")
	public List<IoTDevice> getDeviceByName(String name) {
        Query query = em.createQuery("SELECT d FROM IoTDevice d WHERE d.name = '"+name+"'");
        List<IoTDevice> devices = new ArrayList<IoTDevice>();
        devices = query.getResultList();
        return devices;
    }
}
