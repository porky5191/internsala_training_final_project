package porky.training.zypko.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import porky.training.zypko.model.Restaurant;

public class RestaurantUtil {

    //sort by cost from low to high
    public ArrayList<Restaurant> sortCostLowToHigh(ArrayList<Restaurant> arrayList){
        Collections.sort(arrayList, Comparator.comparing(Restaurant::getCostForOne).thenComparing(Restaurant::getName));
        return arrayList;
    }

    //sort by cost from high to low
    public ArrayList<Restaurant> sortCostHighToLow(ArrayList<Restaurant> arrayList){
        Collections.sort(arrayList, Comparator.comparing(Restaurant::getCostForOne).reversed().thenComparing(Restaurant::getName));
        return arrayList;
    }

    //sort by rating descending
    public ArrayList<Restaurant> sortBtRating(ArrayList<Restaurant> arrayList){
        Collections.sort(arrayList, Comparator.comparing(Restaurant::getRating).reversed().thenComparing(Restaurant::getName));
        return arrayList;
    }

    //sort by name lexically
    public ArrayList<Restaurant> sortByName(ArrayList<Restaurant> arrayList){
        Collections.sort(arrayList, Comparator.comparing(Restaurant::getName));
        return arrayList;
    }
}
