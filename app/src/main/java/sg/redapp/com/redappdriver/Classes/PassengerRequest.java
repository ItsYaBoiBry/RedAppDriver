package sg.redapp.com.redappdriver.Classes;

/**
 * Created by angruixian on 29/6/18.
 */

public class PassengerRequest {
    private String destinationName;
    private String name;
    private double pickupLatitude;
    private double pickupLongitude;
    private String pickupName;
    private double Price;
    private String serviceType;
    private String vehicleModel;
    private String passengeruid;
    private String vehicleNumber;

    public  PassengerRequest(){
    }

    public PassengerRequest(String destinationName, String name, double pickupLatitude, double pickupLongitude, String pickupName, double price, String serviceType, String vehicleModel, String vehicleNumber) {
        this.destinationName = destinationName;
        this.name = name;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.pickupName = pickupName;
        Price = price;
        this.serviceType = serviceType;
        this.vehicleModel = vehicleModel;
        this.vehicleNumber = vehicleNumber;
    }
    public PassengerRequest(String destinationName, String name, double pickupLatitude, double pickupLongitude,String passengeruid, String pickupName, double price, String serviceType, String vehicleModel, String vehicleNumber) {
        this.destinationName = destinationName;
        this.name = name;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.pickupName = pickupName;
        this.passengeruid = passengeruid;
        Price = price;
        this.serviceType = serviceType;
        this.vehicleModel = vehicleModel;
        this.vehicleNumber = vehicleNumber;
    }

    public String getPassengeruid() {
        return passengeruid;
    }

    public void setPassengeruid(String passengeruid) {
        this.passengeruid = passengeruid;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getPickupName() {
        return pickupName;
    }

    public void setPickupName(String pickupName) {
        this.pickupName = pickupName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}

