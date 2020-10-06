/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.dipvvf.crick.vvfcalendar.rest;

import it.dipvvf.crick.vvfcalendar.model.Events;
import it.dipvvf.crick.vvfcalendar.producer.CalendarLogger;
import it.dipvvf.crick.vvfcalendar.support.Utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;

/**
 *
 * @author Crick
 */
@Stateless
@LocalBean
@Path("events")
@Produces(MediaType.APPLICATION_JSON)
public class EventService {
    public final static String DEFAULT_FROM = "20000101";
    public final static String DEFAULT_TO = "20991231";
    public final static int UNPROCESSABLE_ENTITY = 422;
    @PersistenceContext
    EntityManager em;
    @Inject
    @CalendarLogger
    Logger logger;

    @PostConstruct
    public void init() {
        logger.debug("Service started.");
    }
    
    @GET
    public Response getEvents(@QueryParam("owner") String owner, @QueryParam("from") String from, @QueryParam("to") String to) {
        if (from == null && to == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Date tFrom = toDate((from != null) ? from : DEFAULT_FROM);
        Date tTo = toDate((to != null) ? to : DEFAULT_TO);
        
        if(tFrom==null || tTo==null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        logger.debug("Calling with [{}] owner from [{}] to [{}]", ((owner==null) ? owner : "no-owner"), tFrom, tTo);

        Query q = em.createNativeQuery("SELECT * FROM events.recurring_events_for(?1, ?2, ?3)", Events.class);
        q.setParameter(1, owner);
        q.setParameter(2, tFrom, TemporalType.TIMESTAMP);
        q.setParameter(3, tTo, TemporalType.TIMESTAMP);
        return Response.ok(q.getResultList()).build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getEvent(@PathParam("id") int id) {
        logger.debug("Requesting event with ID={}", id);
        Events e = em.find(Events.class, id);
        if(e==null) {
            logger.debug("Event with ID={} not found.", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        else {
            return Response.ok(e).build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewEvent(Events e, @Context UriInfo info) {
        // Remove any reference to ID, we want to insert...
        e.setId(null);
        logger.debug("Adding new event {}...", e.toString());
        
        em.persist(e);
        em.flush();
        
        logger.debug("New event added with ID={}", e.getId());
        
        return Response.created(Utils.resourceToURI(info, e.getId())).build();
    }
    
    @PUT
    @Path("{id: \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editEvent(@PathParam("id") int id, Events e) {
        logger.debug("Editing event with ID={}...", id);
        
        // Does this id exist?
        Events _e = em.find(Events.class, id);
        if(_e==null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        // Force updated of desidered id only...
        if(e.getId()!=id) {
            return Response.status(UNPROCESSABLE_ENTITY).build();
        }
        
        em.merge(e);
        em.flush();
        
        logger.debug("Event with ID={} updated.", id);
        
        return Response.noContent().build();
    }
    
    @DELETE
    @Path("{id: \\d+}")
    public Response deleteEvent(@PathParam("id") int id) {
        logger.debug("Deleting event with ID={}...", id);
        
        // Does this id exist?
        Events _e = em.find(Events.class, id);
        if(_e==null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        em.remove(_e);
        em.flush();
        
        logger.debug("Event with ID={} removed.", id);
        
        return Response.noContent().build();
    }
    
    @GET
    @Path("tz")
    public String getTimeZone() {
        return "Europe/Rome";
    }
    
    private Date toDate(String s) {
        String formats[] = {"yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyyMMdd"};
        for (String fmt : formats) {
            try {
                logger.debug("Trying to format [{}] with pattern [{}]...", s, fmt);
                SimpleDateFormat sdf = new SimpleDateFormat(fmt);
                Date d = sdf.parse(s);
                logger.debug("Formatting successful!");
                return d;
            } catch (ParseException pe) {
                // niente da fare...prova ancora
                logger.debug("Formatting was no good due to {}", pe.toString());
            }
        }

        // se siamo qui nessun formato era valido...
        logger.debug("No format was good for [{}], returning null", s);
        return null;
    }
}
