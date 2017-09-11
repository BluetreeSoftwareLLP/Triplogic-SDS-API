package org.taxireferral.sds.DAORoles;

import com.zaxxer.hikari.HikariDataSource;
import org.taxireferral.sds.Globals.Globals;
import org.taxireferral.sds.ModelRoles.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sumeet on 21/4/17.
 */
public class DAOUserNew {

    private HikariDataSource dataSource = Globals.getDataSource();




    // insert user : check if email or phone is verified
        // registerUsingEmail
        // RegisterUsingPhone
        // RegisterUsingUsername
    // update user : email, phone, password update is excluded because they have special requirements for update
    // update email : check while update whether email is verified or not
    // update phone : check while update whether phone is verified or not
    // update password (Change password by user) : check old password while updating


    // generate email verification code : checks code is expired or not and if yes then generates new
        // check email verification code : check if verification code exist and not expired for the given e-mail
        // update email verification code : if the previous code expired or does not exist

    // update password (Forgot password): update password using reset code

    // delete user

    // verify user | for authentication filter
    // get profile | for login

    // check username exists | to check username unique at time of registration
    // check email exists | to check email unique at time of registration
    // check phone exists | to check phone unique at time of registration
    // check email verification code correct | to check verification code at time of registration
    // check phone verification code correct | to check verification code at time of registration

    // check google ID | to check whether the person is registered or not
    // save google profile | create google account





    public User verifyUser(String username, String token)
    {

        boolean isFirst = true;

        String query = "SELECT "

                + User.USER_ID + ","
                + User.USERNAME + ","
                + User.ENABLED + ","
                + User.ROLE + ""

                + " FROM " + User.TABLE_NAME
                + " WHERE "
                + " ( " + User.USERNAME + " = ? "
                + " OR " + " CAST ( " +  User.USER_ID + " AS text ) " + " = ? "
                + " OR " + " ( " + User.E_MAIL + " = ?" + ")"
                + " OR " + " ( " + User.PHONE + " = ?" + ")" + ")"
                + " AND " + User.PASSWORD + " = ? ";

//                + " AND " + User.TIMESTAMP_TOKEN_EXPIRES + " > now()";

//        CAST (" + User.TIMESTAMP_TOKEN_EXPIRES + " AS TIMESTAMP)"



        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        //Distributor distributor = null;
        User user = null;

        try {

//            System.out.println(query);

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            int i = 0;
            statement.setString(++i,username); // username
            statement.setString(++i,username); // userID
            statement.setString(++i,username); // email
            statement.setString(++i,username); // phone
            statement.setString(++i,token); // token
//            statement.setTimestamp(++i,new Timestamp(System.currentTimeMillis()));


            rs = statement.executeQuery();

            while(rs.next())
            {
                user = new User();

                user.setUserID(rs.getInt(User.USER_ID));
                user.setUsername(rs.getString(User.USERNAME));
                user.setEnabled(rs.getBoolean(User.ENABLED));
                user.setRole(rs.getInt(User.ROLE));
            }


            //System.out.println("Total itemCategories queried " + itemCategoryList.size());



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally

        {

            try {
                if(rs!=null)
                {rs.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statement!=null)
                {statement.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return user;
    }



}


