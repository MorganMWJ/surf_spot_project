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

        Place goodPlace = new Place("Good", 3.2, null);
        Place betterPlace = new Place("Better", 4.3, null);
        Place bestPlace = new Place("Best", 4.9, null);

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
        //todo
    }

}