package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author silviall
 */
public class usuario {

    public String queryTest (String user, String passwd)
    {
        String result = "El usuario existe";
     
        Connection c = null;
        try {
            PreparedStatement statement;
            
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // create a database connection
            c = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            
            String query = "select count(*) from usuarios where id_usuario=? and password=?";
            
            statement = c.prepareStatement(query);
            statement.setString(1, user);
            statement.setString(2, passwd);   
            
            ResultSet r = statement.executeQuery();
            
            if (r.next())
            {
                if (r.getInt(1) == 0)
                    result = "El usuario no existe";
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) 
                    c.close();                
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        
        return result;                        
    }
    
}