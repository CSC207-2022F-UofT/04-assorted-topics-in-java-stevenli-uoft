/* TODO: Create a subclass of Trader named DrivableTrader
 * This class should be identical to Trader, except that it takes
 * only Drivable objects in its inventory, wishlist, etc.
 *
 * The sellingPrice returned should also be overridden. The selling price
 * should be equal to the:
 *     Object's price + Object's max speed
 * If the object is Tradable (and Tradable.MISSING_PRICE otherwise.)
 *
 * Look at DomesticatableTrader.java for an example.
 */

import java.util.ArrayList;
import java.util.List;


public class DrivableTrader extends Trader<Drivable> {
    private int money;
    /**
     * Construct a Trader, giving them the given inventory,
     * wishlist, and money.
     *
     * @param inventory Objects in this Trader's inventory
     * @param wishlist  Objects in this Trader's wishlist
     * @param money     The Trader's money
     */
    public DrivableTrader(List<Drivable> inventory, List<Drivable> wishlist, int money) {
        super(inventory, wishlist, money);
        this.money = money;
    }

    public DrivableTrader(int money) {
        super(money);
    }

    public void addToWishList(Drivable thing) {
        this.getWishlist().add(thing);
    }

    @Override
    public int getSellingPrice(Drivable item) {
        if (!(item instanceof Tradable)) {
            return Tradable.MISSING_PRICE;
        }
        return super.getSellingPrice(item) + item.getMaxSpeed();

    }


    /**
     * Exchange money from other to this Trader according to the price of item,
     * if other has enough money. Otherwise, returns False.
     *
     * @return True if the exchange was completed.
     */
    public boolean exchangeMoney(DrivableTrader other, Drivable item) {
        int selling_price = this.getSellingPrice(item);
        if (selling_price == Tradable.MISSING_PRICE) {
            return false;
        }

        if (selling_price <= other.money) {
            other.money -= selling_price;
            this.money += selling_price;
            return true;
        }
        return false;
    }

    /**
     * Attempt to sell all items that are in both this Trader's inventory
     * and in other's wishlist.
     *
     * @return True iff at least one item was sold from this Trader to other
     */
    public boolean sellTo(DrivableTrader other) {
        boolean sold_at_least_one = false;
        List<Drivable> temp = new ArrayList<>();

        for (Drivable item : this.getInventory()) {
            if (other.getWishlist().contains(item) && exchangeMoney(other, item)) {
                temp.add(item);
                sold_at_least_one = true;
            }
        }

        this.getInventory().removeAll(temp);
        other.getInventory().addAll(temp);
        other.getInventory().removeAll(temp);

        return sold_at_least_one;
    }

    /**
     * Buy items from other.
     *
     * @return True iff at least one item was bought from other.
     */
    public boolean buyFrom(DrivableTrader other) {
        return other.sellTo(this);
    }


    @Override
    public String toString() {
        StringBuilder details = new StringBuilder("-- Inventory --\n");

        for (Drivable item : this.getInventory()) {
            details.append(item).append("\n");
        }

        details.append("-- Wishlist --\n");
        for (Drivable item : this.getWishlist()) {
            details.append(item).append("\n");
        }

        return details.toString();
    }
}