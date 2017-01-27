package com.example.amar.mycar.commands.protocol;

import com.example.amar.mycar.enums.AvailableCommandNames;

public class AvailablePidsCommand_41_60 extends AvailablePidsCommand {

    /**
     * Default ctor.
     */
    public AvailablePidsCommand_41_60() {
        super("01 40");
    }

    public AvailablePidsCommand_41_60(AvailablePidsCommand_41_60 other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.PIDS_41_60.getValue();
    }
}
