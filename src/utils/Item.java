package utils;

import utils.FeaturesItem;
import utils.ItemType;

public class Item extends FeaturesItem {
    public Item(int x, int y, ItemType itemType) {
        super(x, y, itemType);
    }

    public Item(FeaturesItem item) {
        super(item.getX(), item.getY(), item.getItemType());
    }

    public Item(Item item) {
        super(item.getX(), item.getY(), item.getItemType());
    }
}
