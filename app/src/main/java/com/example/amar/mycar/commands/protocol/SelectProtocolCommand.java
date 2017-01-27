package com.example.amar.mycar.commands.protocol;

import com.example.amar.mycar.enums.ObdProtocols;

public class SelectProtocolCommand extends ObdProtocolCommand {

    private final ObdProtocols protocol;

    public SelectProtocolCommand(final ObdProtocols protocol) {
        super("AT SP " + protocol.getValue());
        this.protocol = protocol;
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Select Protocol " + protocol.name();
    }

}
