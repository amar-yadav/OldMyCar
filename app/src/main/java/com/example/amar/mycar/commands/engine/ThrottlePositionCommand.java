package com.example.amar.mycar.commands.engine;

import com.example.amar.mycar.commands.PercentageObdCommand;
import com.example.amar.mycar.enums.AvailableCommandNames;

/**
 * Read the throttle position in percentage.
 *
 * @author pires
 * @version $Id: $Id
 */
public class ThrottlePositionCommand extends PercentageObdCommand {

    /**
     * Default ctor.
     */
    public ThrottlePositionCommand() {
        super("01 11");
    }

    /**
     * Copy ctor.
     *
     *
     */
    public ThrottlePositionCommand(ThrottlePositionCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.THROTTLE_POS.getValue();
    }

}
