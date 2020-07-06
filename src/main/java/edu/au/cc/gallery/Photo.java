package edu.au.cc.gallery;

import edu.au.cc.gallery.PhotoDAO;

public class Photo {
        private String username;
	private String imageName;

        public Photo(String username, String imageName) {
                this.username = username;
		this.imageName = imageName;
        }

        public String getUsername() {
                return username;
        }
        public void setUsername(String u) {
                username = u;
        }
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String i) {
		imageName = i;
	}
	
	 @Override
        public String toString() {
                return "Username: " + username + " image name: " + imageName;
        }
}

