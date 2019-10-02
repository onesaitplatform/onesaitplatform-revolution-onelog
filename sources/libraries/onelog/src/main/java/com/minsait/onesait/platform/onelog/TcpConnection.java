/*
 * Logback GELF - zero dependencies Logback GELF appender library.
 * Copyright (C) 2018 Oliver Siegmar
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.minsait.onesait.platform.onelog;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.SocketFactory;

import com.minsait.onesait.platform.onelog.pool.AbstractPooledObject;

public class TcpConnection extends AbstractPooledObject {

    private final AddressResolver addressResolver;
    private final SocketFactory socketFactory;
    private final int port;
    private final int connectTimeout;

    private volatile OutputStream outputStream;

    TcpConnection(final SocketFactory socketFactory,
                  final AddressResolver addressResolver, final int port, final int connectTimeout) {

        this.addressResolver = addressResolver;
        this.socketFactory = socketFactory;
        this.port = port;
        this.connectTimeout = connectTimeout;
    }

    public void write(final byte[] messageToSend) throws IOException {
        if (outputStream == null) {
            connect();
        }

        outputStream.write(messageToSend);
        outputStream.flush();
    }

    private void connect() throws IOException {
        final Socket socket = socketFactory.createSocket();
        final InetAddress ip = addressResolver.resolve();
        socket.connect(new InetSocketAddress(ip, port), connectTimeout);
        outputStream = socket.getOutputStream();
    }

    @Override
    protected void close() {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

}
