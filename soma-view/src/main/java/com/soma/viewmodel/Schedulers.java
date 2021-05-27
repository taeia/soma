package com.soma.viewmodel;

import reactor.core.scheduler.Scheduler;

public class Schedulers {

    public static final Scheduler single = reactor.core.scheduler.Schedulers.newSingle("soma-single");
    public static final int cores = Runtime.getRuntime().availableProcessors();
    public static final Scheduler computation = reactor.core.scheduler.Schedulers.newParallel("soma-parallel", Runtime.getRuntime().availableProcessors());
}