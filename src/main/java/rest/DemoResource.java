package rest;

import com.google.gson.Gson;
import entity.User;
import fetch.ParallelPinger;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.PuSelector;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {
        EntityManager em = PuSelector.getEntityManagerFactory("pu").createEntityManager();
        try {
            List<User> users = em.createQuery("select user from User user").getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFlightId(@PathParam("id") String id) throws Exception {
        String jsonString = ParallelPinger.getJsonFromAllServers("https://magnusklitmose.com/Flights-1.0/api/flight/"+id);
        return jsonString;
    }

    @GET
    @Path("/country/date/{from}/{to}/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFlightCountryDate(@PathParam("from") String from, @PathParam("to") String to, @PathParam("date") String date) throws Exception {
        String param = "https://magnusklitmose.com/Flights-1.0/api/flight/country/date/" + from + "/" + to + "/" + date;
        String jsonString = ParallelPinger.getJsonFromAllServers(param);
        return jsonString;
    }
    
    
    @GET
    @Path("/event/{lat}/{long}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEvents(@PathParam("lat") String lat, @PathParam("long") String longi) throws Exception {
        String param = "https://mddenner.dk/Semesterprojekt/api/show/events/"+ lat + "/" + longi + "/1000";
        String jsonString = ParallelPinger.getJsonFromAllServers(param);
        return jsonString;
    }
}
