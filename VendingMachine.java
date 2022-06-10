import java.util.Random;

public class VendingMachine {
    private static double totalSales = 0;
    // shelf[row][column][pos] --> in stack; pos 0 would be front of stack
    private VendingItem[][][] shelf = new VendingItem[6][3][5];
    private int luckyChance = 0;
    private Random rand = new Random();

    public VendingMachine() {
        this.restock();
    }

    public VendingItem vend(String code) {
        code = code.toUpperCase();
        int row = code.charAt(0) - 65;
        int col = Integer.parseInt(String.valueOf(code.charAt(1)));
        VendingItem chosenOne;

        // Checks if selection is valid
        // use try-catch instead??
        if (row < 6 && col < 3) {
            // if (Arrays.asList(validItems).contains(shelf[row][col][0])) {
            if (shelf[row][col][0] != null) {
                chosenOne = shelf[row][col][0];
            } else {
                System.out.println("Selection not available");
                return null;
            }
        } else {
            System.out.println("Selection invalid");
            return null;
        }

        // checks if item is free
        // updates totalSales if item is not free
        if (free()) {
            System.out.println("Today is your lucky day!");
            System.out.println("This item is free!");
        } else {
            System.out.println(luckyChance);
            totalSales += chosenOne.getPrice();
        }

        // moves all items' pos forward; sets last item to null
        for (int i = 5; i > 1; i--) {
            shelf[row][col][5 - i] = shelf[row][col][5 - i + 1];
        }
        shelf[row][col][4] = null;
        return chosenOne;
    }

    private boolean free() {
        if (rand.nextInt(100) < luckyChance) {
            luckyChance = 0;
            return true;
        } else {
            luckyChance++;
            return false;
        }
    }

    public void restock() {
        for (int row = 0; row < shelf.length; row++) {
            for (int col = 0; col < shelf[row].length; col++) {
                for (int pos = 0; pos < shelf[row][col].length; pos++) {
                    int pick = rand.nextInt(VendingItem.values().length);
                    shelf[row][col][pos] = VendingItem.values()[pick];
                }
            }
        }
    }

    public static double getTotalSales() {
        return totalSales;
    }

    public int getNumberOfItems() {
        int itemsCount = 0;
        for (int row = 0; row < shelf.length; row++) {
            for (int col = 0; col < shelf[row].length; col++) {
                for (int pos = 0; pos < shelf[row][col].length; pos++) {
                    if (shelf[row][col][pos] != null) {
                        itemsCount++;
                    }
                }
            }
        }
        return itemsCount;
    }

    public double getTotalValue() {
        double totalVal = 0;
        for (int row = 0; row < shelf.length; row++) {
            for (int col = 0; col < shelf[row].length; col++) {
                for (int pos = 0; pos < shelf[row][col].length; pos++) {
                    if (shelf[row][col][pos] != null) {
                        totalVal += shelf[row][col][pos].getPrice();
                    }
                }
            }
        }
        return totalVal;
    }

    public int getLuckyChance() {
        return luckyChance;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("----------------------------------------------------------"
                + "------------\n");
        s.append("                            VendaTron 9000                "
                + "            \n");
        for (int i = 0; i < shelf.length; i++) {
            s.append("------------------------------------------------------"
                    + "----------------\n");
            for (int j = 0; j < shelf[0].length; j++) {
                VendingItem item = shelf[i][j][0];
                String str = String.format("| %-20s ",
                        (item == null ? "(empty)" : item.name()));
                s.append(str);
            }
            s.append("|\n");
        }
        s.append("----------------------------------------------------------"
                + "------------\n");
        s.append(String.format("There are %d items with a total "
                + "value of $%.2f.%n", getNumberOfItems(), getTotalValue()));
        s.append(String.format("Total sales across vending machines "
                + "is now: $%.2f.%n", getTotalSales()));
        return s.toString();
    }
}