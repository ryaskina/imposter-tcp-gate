package com.example.impostergate.constant;

import java.util.StringJoiner;

public enum SocketProperties {
    SIS("SIS", 4005, "name(//SIS/*/*)"),
    CIA("CIA", 4006, "name(//CIA/*/*)");

    SocketProperties(String protocol, int port, String xpathExpression) {
        this.protocol = protocol;
        this.port = port;
        this.xpathExpression = xpathExpression;
    }

    private final String protocol;
    private final int port;
    private final String xpathExpression;

    public static SocketProperties findSocketPropsByProtocol(String protocol) {
        for (SocketProperties socketProperties : values()) {
            if (socketProperties.getProtocol().equals(protocol)) {
                return socketProperties;
            }
        }
        throw new IllegalArgumentException("Can't find SocketProperties by protocol=" + protocol);
    }

    public static SocketProperties findSocketPropsByPort(int port) {
        for (SocketProperties socketProperties : values()) {
            if (socketProperties.getPort() == port) {
                return socketProperties;
            }
        }
        throw new IllegalArgumentException("Can't find SocketProperties by port=" + port);
    }

    public String getProtocol() {
        return protocol;
    }

    public int getPort() {
        return port;
    }

    public String getXpathExpression() {
        return xpathExpression;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SocketProperties.class.getSimpleName() + "[", "]")
                .add("protocol='" + protocol + "'")
                .add("port=" + port)
                .add("xpathExpression='" + xpathExpression + "'")
                .toString();
    }
}
