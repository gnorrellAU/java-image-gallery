package edu.au.cc.gallery;

import java.util.List;

public interface PhotoDAO {
       List<Photo> getPhotos(String username) throws Exception;
  //      List<Photo> getPhotosByUsername(String username) throws Exception;
   //     void addPhoto(Photo p) throws Exception;
}
