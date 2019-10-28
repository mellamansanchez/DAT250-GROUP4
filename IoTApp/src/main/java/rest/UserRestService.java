package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Entity;
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

@Path("/user")
@Stateless
public class UserRestService {

	@PersistenceContext(unitName = "Dat250IoTApp")
	private EntityManager em;
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getUsers() {
		TypedQuery<User> query = em.createNamedQuery("User.findAll", User.class);
		List<User> users = query.getResultList();
		return Response.ok(users).build();
	}
	
	@GET
	@Path("test")
	public Response getTest() {
		return Response.ok("Hello test").build();
	}
}
