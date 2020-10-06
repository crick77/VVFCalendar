/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.dipvvf.crick.vvfcalendar.rest;

import ch.qos.logback.core.status.Status;
import it.dipvvf.crick.vvfcalendar.producer.CalendarLogger;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;

/**
 *
 * @author Crick
 */
@Provider
public class OptimisticLockExceptionMapper implements ExceptionMapper<EJBException> {
    @Inject
    @CalendarLogger
    Logger logger;
    
    @PostConstruct
    public void init() {
        logger.debug("Initialization ok.");
    }
    
    @Override
    public Response toResponse(EJBException exception) 
    {
        logger.debug("Exception from an EJB cought. Real cause: {}", exception.getCausedByException().toString());
        if(exception.getCausedByException() instanceof OptimisticLockException) {
            logger.debug("OptimisticLock! Send CONFLICT HTTP/409 error.");
            return Response.status(Response.Status.CONFLICT).build();
        }
        else {
            logger.debug("Generic EJB Exception! Send SERVER_ERROR HTTP/500 error.");
            return Response.status(Status.ERROR).entity(exception).build();
        }
    }
}
