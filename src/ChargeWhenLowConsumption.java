public class ChargeWhenLowConsumption {
    public static void main(String[] args) {
        chargeWhenLowConsumption();

    }

    public static void chargeWhenLowConsumption() {
        double[] baseLoad = BaseLoadHouse.performGetRequest();

        if (baseLoad != null) {
            System.out.println("Baseload per hour:");
            for (double d : baseLoad) {
                System.out.println(d);
            }
        }

        double batteryMaxCapacity = 46.3; //kWh
        double chargeStation = 7.4; //kW

        //When 20% charged the capacity is:
        double twentyPercentChargeCapacity = 0.2 * batteryMaxCapacity; //kWh

        //When 80% charged the capacity is:
        double eightyPercentChargeCapacity = 0.8 * batteryMaxCapacity; //kWh

        //ChargeAmount
        double chargeAmount = eightyPercentChargeCapacity - twentyPercentChargeCapacity; //kWh

        double timeToCharge = chargeAmount / chargeStation; //h
        System.out.println("The charging time is " + timeToCharge + " hours");

        double minSum = Double.MAX_VALUE;
        int startIndex = 0;
        for (int i = 0; i <= baseLoad.length - 4; i++) {
            double currentSum = baseLoad[i] + baseLoad[i + 1] + baseLoad[i + 2] + baseLoad[i + 3];

            if (currentSum < minSum) {
                minSum = currentSum;
                startIndex = i;
            }
        }

        System.out.println("Start to charge when time is " + startIndex);

        //Controll so household total consumption is <11kW
        boolean check = true;
        for (int i = startIndex; i < startIndex + 4; i++) {
//            System.out.println(baseLoad[i]);
            if (baseLoad[i] + chargeStation > 11) {
//                System.out.println(baseLoad[i]);
                check = false;
                break;
            }
        }
//        System.out.println(check);

        while (true) {
            SetBatteryTo20Percent.setBatteryTo20Percent();
            int currentHour = Info.getSimulatedTime();

            if (currentHour == startIndex) {
                Chargefrom20to80.perform();
                break;
            } else {
                try {
                    Thread.sleep(5000); // Wait 5s (hence the data will be updated)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
