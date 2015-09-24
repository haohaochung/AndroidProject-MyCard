package com.cardcamera;

public class shop {

    private int id;
    private String name;
    
    public shop() {
		
	}
	
	public shop(int id, String shopname) {
		this.id = id;
		this.name = shopname;
	}
	
	public shop(String shopname) {
		this.name = shopname;
	}
    
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
