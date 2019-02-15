package com.ictpoker.ixi.eval;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.ictpoker.ixi.commons.*;

public class EvaluationTest {

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("\nStarting test: " + description.getMethodName());
        }
    };

    // TODO: Look into how code can be converted to a Hand before passing into Evaluation
/*    @Test
    public void testGetProperties() throws Exception {

        int code = Constants.TWO_PAIR << Constants.OFFSET_TYPE | Rank.QUEEN.ordinal() << Constants.OFFSET_MAJOR
                | Rank.TEN.ordinal() << Constants.OFFSET_MINOR
                | (0x1 << Rank.EIGHT.ordinal()) << Constants.OFFSET_KICKER;

        Evaluation eval = new Evaluation(code);

        // System.out.println(eval.toString());
        Assert.assertEquals("Two-Pair Q T 8", eval.toString());
    }*/
}