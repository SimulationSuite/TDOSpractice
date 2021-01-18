package org.tdos.tdospractice.kvm;

import lombok.Setter;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Setter
public class AutoportTool {

    public Map<Integer, Set<Integer>> usedPorts;

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private int startPort;

    private int count;

    public AutoportTool(int startPort, int count) {
        this.startPort = startPort;
        this.count = count;
        this.usedPorts = new HashMap<>();
    }

    public void addRunPorts(int index, Collection<Integer> ports) {
        rwLock.writeLock().lock();
        try {
            Set<Integer> sets = usedPorts.getOrDefault(index, new HashSet<>());
            sets.addAll(ports);
            usedPorts.put(index, sets);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void removeRunPorts(int index, Collection<Integer> ports) {
        rwLock.writeLock().lock();
        try {
            Set<Integer> sets = usedPorts.getOrDefault(index, new HashSet<>());
            sets.removeAll(ports);
            usedPorts.put(index, sets);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public int getFreePort(int index) {
        if (!usedPorts.containsKey(index)) {
            return -1;
        }
        rwLock.readLock().lock();
        try {
            Set<Integer> sets = usedPorts.get(index);
            for (int x = startPort; x <= (startPort + count); x++) {
                if (!sets.contains(x)) {
                    return x;
                }
            }
            return -1;
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
