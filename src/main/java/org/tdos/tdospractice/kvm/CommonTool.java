package org.tdos.tdospractice.kvm;

import java.util.List;

public interface CommonTool {

    void start(String id);

    void stop(String id);

    void restart(String id);

    void remove(String containerId, List<Integer> pubPorts);
}
