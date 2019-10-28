package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejb.UserDao;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import entities.IoTDevice;
import entities.User;

@Path("devices")
@Stateless
public class IoTDeviceService {

	@PersistenceContext(unitName = "Dat250IoTApp")
	private EntityManager em;
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getDevices() {
		TypedQuery<IoTDevice> query = em.createNamedQuery("IoTDevice.findAll", IoTDevice.class);
		List<IoTDevice> devices = query.getResultList();
		return Response.ok(devices).build();
	}
	
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getDevice(@PathParam("id") String id) {
		long idInt = Long.parseLong(id);
		IoTDevice device = em.find(IoTDevice.class, idInt);
		if (device == null)
			throw new NotFoundException();
		return Response.ok(device).build();
	}
	
	@GET
	@Path("{id}/registrations/")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getRegistrations(@PathParam("id") String id) {
		UserDao uDao = new UserDao();
		long idLong = Long.parseLong(id);
		IoTDevice device = em.find(IoTDevice.class, idLong);
		if (device == null)
			throw new NotFoundException();
		List<User> users = uDao.getDevicesFollowed(device);
		/*if(users.isEmpty()) 
			throw new NotFoundException();*/
		return Response.ok(users).build();
	}
	
	@GET
	@Path("{id}/registrations/{rid}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getRegistrationOfDevice(@PathParam("id") String id, @PathParam("rid") String rid) {
		UserDao uDao = new UserDao();
		long idLong = Long.parseLong(id);
		long ridLong = Long.parseLong(rid);
		IoTDevice device = em.find(IoTDevice.class, idLong);
		if (device == null)
			throw new NotFoundException();
		List<User> users = uDao.getDevicesFollowed(device);
		if(users.isEmpty()) 
			throw new NotFoundException();
		for(User u : users) {
			if(u.getId() == ridLong) {
				return Response.ok(u).build();
			}
		}
		throw new NotFoundException();	
	}
	
	@POST
	@Path("{id}/")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})//@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	public String registerDevice(@PathParam("id") String id, @QueryParam("user") long user_id) {
		UserDao uDao = new UserDao();
		long idLong = Long.parseLong(id);
		IoTDevice device = em.find(IoTDevice.class, idLong);
		if (device == null)
			throw new NotFoundException();
		List<User> users = new ArrayList<User>();
		if(users.isEmpty()) {
			throw new NotFoundException();
		}
		User u = users.get(0);
		u.followDevice(device);
		uDao.persist(u);
		return "Done "+u.getUsername();
	}
	
	@GET
	@Path("test")
	public Response getTest() {
		return Response.ok("Hello test").build();
	}
}
