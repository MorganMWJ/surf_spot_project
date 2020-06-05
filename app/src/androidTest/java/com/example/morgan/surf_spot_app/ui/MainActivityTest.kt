package com.example.morgan.surf_spot_app.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.pm.ActivityInfo
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.morgan.surf_spot_app.R
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule @JvmField val mainActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)
    //@Rule @JvmField val listActivityRule: IntentsTestRule<ListActivity> = IntentsTestRule(ListActivity::class.java)

    private var activity: MainActivity?  = null

    @Before
    fun setUp() {
        this.activity = mainActivityRule.activity
    }

//    @Before
//    fun stubAllExternalIntents(){
//        intending(not(isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
//    }

    @Test
    fun testLaunch(){
//        var view: View? = activity!!.findViewById(R.id.edit_lat)
//        assertNotNull(view)
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun onLaunchCheckLatitudeInputIsDisplayed() {
        /* Launch MainActivity */
        ActivityScenario.launch(MainActivity::class.java)

        /* Check input field for latitude is displayed */
        onView(withId(R.id.edit_lat)).check(matches(isDisplayed()))
    }

    @Test
    fun onLaunchCheckLongitudeInputIsDisplayed() {
        /* Launch MainActivity */
        ActivityScenario.launch(MainActivity::class.java)

        /* Check input field for latitude is displayed */
        onView(withId(R.id.edit_long)).check(matches(isDisplayed()))
    }

    @Test
    fun onLaunchSearchButtonIsDisplayed() {
        /* Launch MainActivity */
        ActivityScenario.launch(MainActivity::class.java)

        /* Check view with 'Search' text is displayed */
        onView(withText(R.string.search_button_text))
                .check(matches(isDisplayed()))
    }

    @Test
    fun checkUserIsNotifiedOfValidationIssuesWhenLatitudeAndLongitudeAreEmpty(){
        /* Launch MainActivity */
        ActivityScenario.launch(MainActivity::class.java)

        /* Click search button */
        onView(withId(R.id.run_search_button))
                .perform(click())

        /* Check Snackbar alert is displayed */
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.empty_field_exception)))
    }

    @Test
    fun checkUserIsNotifiedOfValidationIssuesWhenLatitudeIsEmpty(){
        /* Launch MainActivity */
        ActivityScenario.launch(MainActivity::class.java)

        /* Enter value for longitude */
        onView(withId(R.id.edit_long))
                .perform(typeText("11"))

        /* Click search button */
        onView(withId(R.id.run_search_button))
                .perform(click())

        /* Check Snackbar alert is displayed */
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.empty_field_exception)))
    }

    @Test
    fun checkUserIsNotifiedOfValidationIssuesWhenLongitudeIsEmpty(){
        /* Launch MainActivity */
        ActivityScenario.launch(MainActivity::class.java)

        /* Enter value for latitude */
        onView(withId(R.id.edit_lat))
                .perform(typeText("11"))

        /* Click search button */
        onView(withId(R.id.run_search_button))
                .perform(click())

        /* Check Snackbar alert is displayed */
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.empty_field_exception)))
    }

    @Test
    fun checkSavedInstanceStateMaintainsInputsOnScreenReorientation(){
        /* Launch MainActivity */
        ActivityScenario.launch(MainActivity::class.java)

        /* Set values for inputs */
        onView(withId(R.id.edit_lat))
                .perform(typeText("1234.5"))
        onView(withId(R.id.edit_long ))
                .perform(typeText("2345.6"))
        onView(withId(R.id.keyword_checkbox))
                .perform(scrollTo(), click())
        onView(withId(R.id.edit_key))
                .perform(scrollTo(), typeText("XXXX"))

        /* Re-orientate the screen */
        mainActivityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        /* Check inputs are still as previously set */
        onView(withId(R.id.edit_lat))
                .check(matches(withText("1234.5")))
        onView(withId(R.id.edit_long))
                .check(matches(withText("2345.6")))
        onView(withId(R.id.keyword_checkbox))
                .check(matches(isNotChecked()))
        onView(withId(R.id.edit_key))
                .check(matches(withText("XXXX")))


    }

//    @Test
//    fun checkListActivityIsLaunchedWithResultsDisplayedUponSearch(){
//        /* Launch MainActivity */
//        ActivityScenario.launch(MainActivity::class.java)
//
//        /* This test searches for the coordinates of Fistral Beach */
//        val fistralLat = 50.4165
//        val fistralLong = 5.1002
//
//        /* Enter value for latitude */
//        onView(withId(R.id.edit_lat))
//                .perform(typeText(fistralLat.toString()))
//
//        /* Enter value for longitude */
//        onView(withId(R.id.edit_long))
//                .perform(typeText(fistralLong.toString()))
//
//        /* Click search button */
//        onView(withId(R.id.run_search_button))
//                .perform(click())
//
//        /* Check ListActivity is open */
//        intended(hasComponent(ListActivity::class.java.name))
//
//    }





//    @UiThreadTest
//    fun testUpdateListView_goodResult(){
//
//        /* Create ResultWrapper object with places */
//        val places = ArrayList<Place>()
//        places.add(Place("Morgan's Place", 3.2, null))
//        places.add(Place("Josh's Place", 4.2, null))
//        var resWrapper: ResultWrapper = ResultWrapper(places, "OK")
//
//        /* Call handle result */
//        activity!!.handleResult(resWrapper)
//
//        /* Check places are set in Recycler View adapter */
//        var rv: RecyclerView = activity!!.findViewById(R.id.place_list)
//        if(rv.adapter is  PlacesRecyclerWithListAdapter){
//            assertEquals(2, rv.adapter!!.itemCount)
//        }
//
//        /* Check places are displayed in recycler view */
//
//
//        /* Check alternate result text view is 'gone' */
//        onView(withId(R.id.results_text)).check(doesNotExist())
//
//
//    }

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