package com.github.cb372.persevere.delay;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Author: chris
 * Created: 5/2/13
 */
public class DelayStrategiesTest {

    @Test
    public void retryImmediately() {
        DelayStrategy strategy = DelayStrategies.retryImmediately();
        assertThat(strategy.getNextDelayMs(0), is(0L));
        assertThat(strategy.getNextDelayMs(1), is(0L));
        assertThat(strategy.getNextDelayMs(2), is(0L));
        assertThat(strategy.getNextDelayMs(100), is(0L));
    }

    @Test
    public void fixed() {
        DelayStrategy strategy = DelayStrategies.fixedDelay(50);
        assertThat(strategy.getNextDelayMs(0), is(50L));
        assertThat(strategy.getNextDelayMs(1), is(50L));
        assertThat(strategy.getNextDelayMs(2), is(50L));
        assertThat(strategy.getNextDelayMs(100), is(50L));
    }

    @Test
    public void random() {
        DelayStrategy strategy = DelayStrategies.random(100, 200);
        for (int i=0; i<1000; i++) {
            long nextDelay = strategy.getNextDelayMs(i);
            assertThat(nextDelay, both(gte(100L)).and(lt(200L)));
        }
    }

    @Test
    public void exponential() {
        DelayStrategy strategy = DelayStrategies.exponential(10, 2.0);
        assertThat(strategy.getNextDelayMs(0), is(10L));
        assertThat(strategy.getNextDelayMs(1), is(20L));
        assertThat(strategy.getNextDelayMs(2), is(40L));
        assertThat(strategy.getNextDelayMs(3), is(80L));
        assertThat(strategy.getNextDelayMs(4), is(160L));
    }

    // TODO add dependency on hamcrest-all and delete these

    private Matcher<Long> gte(final long value) {
        return new BaseMatcher<Long>() {
            @Override
            public boolean matches(Object item) {
                return ((Long)item) >= value;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a value >= than ").appendValue(value);
            }
        };
    }

    private Matcher<Long> lt(final long value) {
        return new BaseMatcher<Long>() {
            @Override
            public boolean matches(Object item) {
                return ((Long)item) < value;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a value < than ").appendValue(value);
            }
        };
    }
}
