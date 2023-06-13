package com.baeldung.grpc.client;

import io.grpc.ProxiedSocketAddress;
import io.grpc.ProxyDetector;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.SocketAddress;

public class myProxy implements ProxyDetector {
    @Nullable
    @Override
    public ProxiedSocketAddress proxyFor(SocketAddress socketAddress) throws IOException {
        // i'm not using a proxy , so returning null
        // HttpProxy addr can be returned if a client proxy is involved
        // TBD -> should the client proxy support hhtp/2 ? i assume so
        return null;
    }
}
