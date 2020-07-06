package edu.au.cc.gallery;

import java.sql.SQLException;

public class Postgres {
	public static UserDAO getUserDAO() throws SQLException {
		return new PostgresUserDAO();
	}

	public static PhotoDAO getPhotosDAO() throws SQLException {
		return new PostgresPhotoDAO();
	}
}
