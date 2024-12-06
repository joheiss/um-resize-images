package com.jovisco.um.imagescalerui;

public class ProgressCounter {

    private static class SingletonHelper {
        private static final ProgressCounter INSTANCE = new ProgressCounter();
    }

    private volatile int countOriginalImages = 0;
    private volatile int countResizedImages = 0;

    private ProgressCounter() {}

    public static ProgressCounter getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public int getCountOriginalImages() {
        return countOriginalImages;
    }

    public synchronized void incrementOriginalImages() {
        countOriginalImages++;
    }

    public int getCountResizedImages() {
        return countResizedImages;
    }

    public synchronized void incrementResizedImages() {
        countResizedImages++;
    }

    public synchronized void resetCounters() {
        countOriginalImages = 0;
        countResizedImages = 0;
    }
}
