package com.example.morgan.surf_spot_app.ui


import android.view.View
import android.widget.EditText
import androidx.test.annotation.UiThreadTest
import com.example.morgan.surf_spot_app.R
import com.example.morgan.surf_spot_app.model.Place
import com.example.morgan.surf_spot_app.model.ResultWrapper
import kotlinx.android.synthetic.main.activity_main.view.*
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.util.ArrayList

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule

class MainActivityTest {

    @Rule @JvmField val mainActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java)

    private var activity: MainActivity?  = null

    @Before
    fun setUp() {
        this.activity = mainActivityRule.activity
    }

    @Test
    fun testLaunch(){
        var view: View? = activity!!.findViewById(R.id.edit_lat)
        assertNotNull(view)
    }

    @Test
    @UiThreadTest
    fun testUpdateListView_goodResult(){

        /* Create ResultWrapper object with places */
        val places = ArrayList<Place>()
        places.add(Place("Morgan's Place", 3.2, null))
        places.add(Place("Josh's Place", 4.2, null))
        var resWrapper: ResultWrapper = ResultWrapper(places, "OK")

        /* Call handle result */
        activity!!.handleResult(resWrapper)

        /* Check places are displayed in recycler view */

        /* Check alternate result text view is 'gone' */
        onView(withId(R.id.results_text))
                .check(doesNotExist())


    }

    @Test
    fun testUpdateListView_zeroResultsResult(){
        //todo
    }

    @Test
    fun testUpdateListView_RequestDeniedResult(){
        //todo
    }


    @After
    fun tearDown() {
        this.activity = null
    }
}