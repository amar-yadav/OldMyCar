package com.example.amar.mycar.commands.protocol;

import com.example.amar.mycar.commands.PersistentCommand;

/**
 * Retrieve available PIDs ranging from 21 to 40.
 *
 * @author pires
 * @version $Id: $Id
 */
public abstract class AvailablePidsCommand extends PersistentCommand {

    /**
     * Default ctor.
     *
     * @param command a {@link String} object.
     */
    public AvailablePidsCommand(String command) {
        super(command);
    }

    public AvailablePidsCommand(AvailablePidsCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {

    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getCalculatedResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        //First 4 characters are a copy of the command code, don't return those
        return String.valueOf(rawData).substring(4);
    }
}
