package sg.redapp.com.redappdriver.Classes;

public class User {
    private String approve;
    private String country_code;
    private String email;
    private boolean in_progress;
    private String name;
    private boolean online_status;
    private String passengerRideId;
    private String phone_number;
    private String userCarPlate;
    private String role;
    private String type_of_service;

    public User() {
    }

    public User(String approve, String country_code, String email, boolean in_progress, String name, boolean online_status, String passengerRideId, String phone_number, String userCarPlate, String role, String type_of_service) {
        this.approve = approve;
        this.country_code = country_code;
        this.email = email;
        this.in_progress = in_progress;
        this.name = name;
        this.online_status = online_status;
        this.passengerRideId = passengerRideId;
        this.phone_number = phone_number;
        this.userCarPlate = userCarPlate;
        this.role = role;
        this.type_of_service = type_of_service;
    }


    public User( String email,String name,String phone_number, String type_of_service,String carPlateNum) {
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
        this.type_of_service = type_of_service;
        this.userCarPlate = carPlateNum;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIn_progress() {
        return in_progress;
    }

    public void setIn_progress(boolean in_progress) {
        this.in_progress = in_progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline_status() {
        return online_status;
    }

    public void setOnline_status(boolean online_status) {
        this.online_status = online_status;
    }

    public String getPassengerRideId() {
        return passengerRideId;
    }

    public void setPassengerRideId(String passengerRideId) {
        this.passengerRideId = passengerRideId;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUserCarPlate() {
        return userCarPlate;
    }

    public void setUserCarPlate(String userCarPlate) {
        this.userCarPlate = userCarPlate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType_of_service() {
        return type_of_service;
    }

    public void setType_of_service(String type_of_service) {
        this.type_of_service = type_of_service;
    }
}
