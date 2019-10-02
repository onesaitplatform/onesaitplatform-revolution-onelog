/*
 * Logback GELF - zero dependencies Logback GELF appender library.
 * Copyright (C) 2016 Oliver Siegmar
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

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class GelfAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private static final int DEFAULT_GELF_PORT = 12201;

    /**
     * IP or hostname of graylog server.
     */
    private String graylogHost;

    /**
     * Port of graylog server. Default: 12201.
     */
    private int graylogPort = DEFAULT_GELF_PORT;

    private GelfEncoder encoder;

    @Override
    public final void start() {
        if (graylogHost == null) {
            addError("No graylogHost configured");
            return;
        }

        if (encoder == null) {
            encoder = new GelfEncoder();
            encoder.setContext(getContext());
            encoder.start();
        }

        if (encoder.isAppendNewline()) {
            addError("Newline separator must not be enabled in layout");
            return;
        }

        try {
            startAppender();

            super.start();
        } catch (final Exception e) {
            addError("Couldn't start appender", e);
        }
    }

    protected void startAppender() throws IOException {
    }

    @Override
    protected void append(final ILoggingEvent event) {
        final byte[] binMessage = encoder.encode(event);

        try {
            appendMessage(binMessage);
        } catch (final Exception e) {
            // Could be IOException or some kind of RuntimeException
            addError("Error sending GELF message", e);
        }
    }

    protected abstract void appendMessage(byte[] messageToSend) throws IOException;

    @Override
    public void stop() {
        super.stop();
        try {
            close();
        } catch (final IOException e) {
            addError("Couldn't close appender", e);
        }
    }

    protected abstract void close() throws IOException;

}
