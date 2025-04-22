public class Administrator {
    private int admin_id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;

    public Administrator(int admin_id, String first_name, String last_name, String email, String password) {
        this.admin_id = admin_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public Administrator(String first_name, String last_name, String email, String password) {
        this.admin_id = 0;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Administrator that = (Administrator) o;

        return getAdmin_id() == that.getAdmin_id();
    }

    @Override
    public int hashCode() {
        return getAdmin_id();
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "admin_id=" + admin_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
