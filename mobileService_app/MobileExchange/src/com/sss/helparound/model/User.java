package com.sss.helparound.model;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String sexe;

    public User() {
    }

    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public User(final String email,
                final String firstName, final String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(final String email, final String password,
                final String firstName, final String lastName) {
        this(email, firstName, lastName);
        this.password = password;
    }

    public User(final String email, final String password,
                final String firstName, final String lastName, final String address, final String sexe) {
        this(email, password, firstName, lastName);
        this.address = address;
        this.sexe = sexe;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set the email of the user
     *
     * @param email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return first of the User
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return lastname
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return the address of the user
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return sexe of the user
     */
    public String getSexe() {
        return sexe;
    }

    /**
     *
     * @param sexe
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || User.class != o.getClass()) return false;

        User user = (User) o;

        if (!email.equals(user.email)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
