package org.tdos.tdospractice.kvm;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "kvm")
@Data
public class KvmConfiguration {

    private Docker docker;

    @Data
    public static class Docker {

        private int startPort;

        private int count;

        private List<ServerInfo> serverInfo;
    }

    @Data
    public static class ServerInfo {

        private String name;

        private String path;
    }


}