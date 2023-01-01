package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite kerho-ohjelmalle
 * @author vesal
 * @version 3.1.2019
 */
@RunWith(Suite.class)
@SuiteClasses({
    turnaus.test.FencerTest.class,
    turnaus.test.FencersTest.class,
    turnaus.test.MatchTest.class,
    turnaus.test.MatchesTest.class,
    turnaus.test.TurnausTest.class
    })
public class AllTests {
 //
}
