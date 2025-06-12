import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Chargefrom20to80 {
    public static void main(String[] args) {
        perform();
    }

    public static void perform(){
        SetBatteryTo20Percent setBatteryTo20Percent = new SetBatteryTo20Percent();
        setBatteryTo20Percent.setBatteryTo20Percent();
//        Info info = new Info();
//        double batteryCapcity = info.performGetRequest();
//        CalculateCurrentChargeLevel.ChargeLevel(batteryCapcity);


        StartStopCharging.startStopCharging("on");


        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            Info info2 = new Info();
            double batteryCapacity = info2.performGetRequest();
            double chargeLevel = CalculateCurrentChargeLevel.ChargeLevel(batteryCapacity);


            if (chargeLevel >= 80.0) {
                System.out.println("Battery reached 80%, stopping charging.");
                StartStopCharging.startStopCharging("off");
                scheduler.shutdown();

            }
            }, 0, 5, TimeUnit.SECONDS);


    }
}




