/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.dipvvf.crick.vvfcalendar.producer;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Crick
 */
@Singleton
@Startup
public class LogProducer {
    private final static String OUTPUT_DIR = "OUTPUT_DIR";
    private final static String OUTPUT_FILENAME = "OUTPUT_FILENAME";
    @Resource(lookup="calendar/outputDir")
    String outputDir;
    @Resource(lookup="calendar/outputFileName")
    String outputFileName;

    @PostConstruct
    public void setup() {
        try {
            LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
            JoranConfigurator jc = new JoranConfigurator();
            jc.setContext(context);
            context.reset();
            if(outputDir!=null) context.putProperty(OUTPUT_DIR, outputDir);
            if(outputFileName!=null) context.putProperty(OUTPUT_FILENAME, outputFileName);
            jc.doConfigure(this.getClass().getClassLoader().getResourceAsStream("logback.xml"));
            
            System.out.println("Logger initialized. Context OutputDir: ["+outputDir+"], Context OutputFileName: ["+outputFileName+"].");
        } catch (JoranException ex) {
            System.err.println("init logging failed: "+ex);
        }
    }

    /**
     * Restituisce il logger relativo alla classe indicata dall'injection point
     * passato per argomento
     *
     * @param ip l'injection point su cui applicare il logger
     * @return il logger associato
     */
    @Produces
    @CalendarLogger
    public Logger produceLogger(InjectionPoint ip) {
        return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
    }
}
