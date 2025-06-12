public class CalculateCurrentChargeLevel {
    public static void main(String[] args) {
        Info info = new Info();
        double batteryCapacity = info.performGetRequest();
        ChargeLevel(batteryCapacity);
    }

    public static double ChargeLevel(double batteryCapacity) {
//        Info info = new Info();
//        double batteryCapacity = info.performGetRequest();

        double batteryMaxCapacity = 46.3; //kWh
        double chargeLevel = batteryCapacity * 100 / batteryMaxCapacity;

        //to get rid of decimals
        double newValue = Math.round(chargeLevel);
        System.out.println("Charge level is: " + newValue + "%");

        return newValue;
    }


}
