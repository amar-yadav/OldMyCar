package com.example.amar.mycar.commands.control;

import com.example.amar.mycar.commands.PercentageObdCommand;
import com.example.amar.mycar.enums.AvailableCommandNames;

/**
 * Timing Advance
 *
 * @author pires
 * @version $Id: $Id
 */
public class TimingAdvanceCommand extends PercentageObdCommand {

    /**
     * <p>Constructor for TimingAdvanceCommand.</p>
     */
    public TimingAdvanceCommand() {
        super("01 0E");
    }

    /**
     * <p>Constructor for TimingAdvanceCommand.</p>
     *
     *
     */
    public TimingAdvanceCommand(TimingAdvanceCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.TIMING_ADVANCE.getValue();
    }

}
