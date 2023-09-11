package com.consensus.gtvadapter.common.sqs.listener;

import java.util.concurrent.SynchronousQueue;

public class SynchronousBlockingQueue<E> extends SynchronousQueue<E> {

    public SynchronousBlockingQueue() {
        super();
    }

    public SynchronousBlockingQueue(boolean fair) {
        super(fair);
    }

    @Override
    public boolean offer(E e) {
        try {
            put(e);
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        return false;
    }
}
