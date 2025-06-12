public class ChargeWhenPriceIsLow {
    public static void main(String[] args) {
        chargeWhenPriceIsLow();
    }
    public static void chargeWhenPriceIsLow() {
        double[] prices = SubmitGetPriceInfo.performGetRequest();
        double[] baseLoad = BaseLoadHouse.performGetRequest();

        if (prices != null) {
            System.out.println("Prices per hour:");
            for (double d : prices) {
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
        double timeToChargeRoundedUp = Math.ceil(timeToCharge);
//        System.out.println("The charging time is " + timeToCharge + " hours");
        System.out.println("The charging time is " + timeToChargeRoundedUp + " hours");

        //Since time to charge is 4h, check when the prices is lowest for 4 hours in a row (assumes the charging needs to be in a row), also check total consumption.
        double minSum = Double.MAX_VALUE;
        int startIndex = 0;
        for (int i = 0; i <= prices.length - 4; i++) {
            double currentSum = prices[i] + prices[i + 1] + prices[i + 2] + prices[i + 3];

            boolean allBelow11kW = true;
            for (int j = 0; j < 4; j++) {
                if (baseLoad[i + j] + chargeStation > 11) {
                    allBelow11kW = false;
                    break;
                }
            }

            if (currentSum < minSum && allBelow11kW) {
                minSum = currentSum;
                startIndex = i;
            }
        }

        System.out.println("Start to charge when hours is " + startIndex);


        SetBatteryTo20Percent.setBatteryTo20Percent();
        while (true) {

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

