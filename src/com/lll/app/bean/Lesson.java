package com.lll.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Lesson  implements Serializable{
	public List<LessonPhoto> photos;//ПаІб
	
	public Lesson(){
		photos = new ArrayList<LessonPhoto>();
	}

	
	public List<LessonPhoto> getPhotos() {
		return photos;
	}

	public void setPhotos(List<LessonPhoto> photos) {
		this.photos = photos;
	}
	
}
