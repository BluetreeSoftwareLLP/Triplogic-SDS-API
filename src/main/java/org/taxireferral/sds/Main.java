package org.taxireferral.sds;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.taxireferral.sds.Globals.Globals;
import org.taxireferral.sds.ModelRoles.User;
import org.taxireferral.sds.ModelSettings.ServiceConfigurationGlobal;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Main class.
 *
 */
public class Main {


    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:5600";




    public static Server startJettyServer() {
        // create a resource config that scans for JAX-RS resources and providers

        final ResourceConfig rc = new ResourceConfig().packages(true,"org.taxireferral.sds");


        return JettyHttpContainerFactory.createServer(URI.create(BASE_URI),rc);
    }




    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {


        createDB();
//        upgradeTables();
        createTables();



        startJettyServer();
    }




    public static void createDB()
    {

        Connection conn = null;
        Statement stmt = null;

        try {

            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres"
                    ,JDBCContract.CURRENT_USERNAME
                    ,JDBCContract.CURRENT_PASSWORD);

            stmt = conn.createStatement();

            String createDB = "CREATE DATABASE \"TaxiReferralSDSDB\" WITH ENCODING='UTF8' OWNER=postgres CONNECTION LIMIT=-1";

            stmt.executeUpdate(createDB);

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        finally{


            // close the connection and statement accountApproved

            if(stmt !=null)
            {

                try {
                    stmt.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


            if(conn!=null)
            {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }




    private static void createTables()
    {

        Connection connection = null;
        Statement statement = null;

        try {

            connection = DriverManager.getConnection(JDBCContract.CURRENT_CONNECTION_URL,
                    JDBCContract.CURRENT_USERNAME, JDBCContract.CURRENT_PASSWORD);


            statement = connection.createStatement();

            statement.executeUpdate(User.createTablePostgres);
            statement.executeUpdate(ServiceConfigurationGlobal.createTablePostgres);

            System.out.println("Tables Created ... !");






            // developers Note: whenever adding a table please check that its dependencies are already created.

            // Insert the default administrator if it does not exit


            User admin = new User();
            admin.setUsername("admin");
            admin.setRole(1);
            admin.setPassword("password");

            try
            {
                int rowCount = Globals.daoUserSignUp.registerUsingUsername(admin,true);

                if(rowCount==1)
                {
                    System.out.println("Admin Account created !");
                }
            }
            catch (Exception ex)
            {
                System.out.println(ex.toString());
            }




            // Insert Default Settings

            // Insert Default Service Configuration




            // create directory images

            final java.nio.file.Path BASE_DIR = Paths.get("./images");

            File theDir = new File(BASE_DIR.toString());

            // if the directory does not exist, create it
            if (!theDir.exists()) {

                System.out.println("Creating directory: " + BASE_DIR.toString());

                boolean result = false;

                try{
                    theDir.mkdir();
                    result = true;
                }
                catch(Exception se){
                    //handle it
                }
                if(result) {
                    System.out.println("DIR created");
                }
            }





        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        finally{


            // close the connection and statement accountApproved

            if(statement !=null)
            {

                try {
                    statement.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


            if(connection!=null)
            {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }



}

