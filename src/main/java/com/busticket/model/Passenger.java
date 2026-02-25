package com.busticket.model;


public class Passenger {

    private String name;
    private String phone;
    private String email;

    public Passenger(String name, String phone, String email) {
        this.name  = name;
        this.phone = phone;
        this.email = email;
    }



    public String validateContact() {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be empty.";
        }
        if (phone == null || !phone.matches("\\d{10}")) {
            return "Phone must be exactly 10 digits.";
        }
        if (email == null || !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            return "Please enter a valid email address.";
        }
        return null;
    }


    public String getName()           { return name; }
    public void setName(String name)  { this.name = name; }

    public String getPhone()          { return phone; }
    public void setPhone(String phone){ this.phone = phone; }

    public String getEmail()          { return email; }
    public void setEmail(String email){ this.email = email; }

    @Override
    public String toString() {
        return name + " | " + phone + " | " + email;
    }
}
