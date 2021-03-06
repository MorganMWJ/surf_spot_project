package com.example.morgan.surf_spot_app.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class PlaceTest {

    /**
     * Tests the implementation of comparable was good.
     * By checking places are sorted in order of descending rating.
     */
    @Test
    public void testCorrectSorting(){

        Place goodPlace = new Place("Good", 3.2, null, null);
        Place betterPlace = new Place("Better", 4.3, null, null);
        Place bestPlace = new Place("Best", 4.9, null, null);

        /* Create unsorted list of places */
        List<Place> places = new ArrayList<>();
        places.add(betterPlace);
        places.add(goodPlace);
        places.add(bestPlace);

        /* Create sorted list of same places */
        List<Place> sortedPlaces = new ArrayList<>();
        sortedPlaces.add(bestPlace);
        sortedPlaces.add(betterPlace);
        sortedPlaces.add(goodPlace);

        /* Sort the unsorted list */
        Collections.sort(places);

        /* Assert that when sorted the first list matches the expected 2nd list */
        Assert.assertEquals(sortedPlaces.get(0), places.get(0));
        Assert.assertEquals(sortedPlaces.get(1), places.get(1));
        Assert.assertEquals(sortedPlaces.get(2), places.get(2));
    }

    /**
     * Checks sorting lists places with null rating last.
     */
    @Test
    public void testCorrectSortingWhenRatingNull(){
        Place nullPlace1 = new Place("Null", null, null, null);
        Place nullPlace2 = new Place("Null", null, null, null);
        Place goodPlace = new Place("Good", 3.2, null, null);
        Place betterPlace = new Place("Better", 4.3, null, null);
        Place bestPlace = new Place("Best", 4.9, null, null);

        /* Create unsorted list of places */
        List<Place> places = new ArrayList<>();
        places.add(nullPlace1);
        places.add(nullPlace2);
        places.add(betterPlace);
        places.add(goodPlace);
        places.add(bestPlace);

        /* Create sorted list of same places */
        List<Place> sortedPlaces = new ArrayList<>();
        sortedPlaces.add(bestPlace);
        sortedPlaces.add(betterPlace);
        sortedPlaces.add(goodPlace);
        sortedPlaces.add(nullPlace1);
        sortedPlaces.add(nullPlace2);

        /* Sort the unsorted list */
        Collections.sort(places);

        /* Assert that when sorted the first list matches the expected 2nd list */
        Assert.assertEquals(sortedPlaces.get(0), places.get(0));
        Assert.assertEquals(sortedPlaces.get(1), places.get(1));
        Assert.assertEquals(sortedPlaces.get(2), places.get(2));

        /* We do not care the order of null rated places so long as they are not before the others */
    }

}