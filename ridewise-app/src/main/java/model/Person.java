package model;

public class Person {
    private String name;
    private long contactNo;

    public Person() { }

    public Person(String name, long contactNo) {
        this.name = name;
        this.contactNo = contactNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactNo(long contactNo) {
        this.contactNo = contactNo;
    }

    public String getName() {
        return name;
    }

    public long getContactNo() {
        return contactNo;
    }
}
