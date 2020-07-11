package edu.au.cc.gallery;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresPhotoDAO implements PhotoDAO {

        private DB connection;

        public PostgresPhotoDAO() throws SQLException {
                connection = new DB();
                connection.connect();
        }
 
        public List<Photo> getPhotos(String username) throws SQLException {
                List<Photo> result = new ArrayList<>();
                ResultSet rs = connection.executeQuery("select username, photoname from photos where username=?", new String[] {username});
                while(rs.next()) {
                        result.add(new Photo(rs.getString(1), rs.getString(2)));
              		System.out.println(rs.getString(1) + " " +  rs.getString(2));
	      	}
                rs.close();
                return result;
        } 
//	List<Photo> getPhotosByUsername(String username) throws Exception;
        
//	void addPhoto(Photo p) throws Exception;

}
