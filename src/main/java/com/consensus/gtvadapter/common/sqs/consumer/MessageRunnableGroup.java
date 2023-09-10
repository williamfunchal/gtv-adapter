package com.consensus.gtvadapter.common.sqs.consumer;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class MessageRunnableGroup implements Iterable<Runnable> {

    private final String groupName;
    private final Collection<Runnable> runnableGroup;

    @Override
    public Iterator<Runnable> iterator() {
        return runnableGroup.iterator();
    }

    @Override
    public String toString() {
        return String.format("groupName=%s, groupSize=%d", groupName, runnableGroup.size());
    }
}
