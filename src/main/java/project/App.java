package project;

import java.util.logging.Level;
import java.util.logging.Logger;

import project.controller.Controller;
import project.controller.ControllerImpl;
import project.dao.TaskDaoImpl;
import project.service.ServiceImpl;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main( String[] args )
    {
        try {
            Controller c = new ControllerImpl(new ServiceImpl(new TaskDaoImpl()));
            c.command(args);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }
}
