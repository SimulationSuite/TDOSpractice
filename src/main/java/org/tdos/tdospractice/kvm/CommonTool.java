package org.tdos.tdospractice.kvm;

public interface CommonTool {

    void start(String id);

    void stop(String id);

    void restart(String id);

    void remove(String id);
}
