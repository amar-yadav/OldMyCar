package com.example.amar.mycar.commands.engine;

import com.example.amar.mycar.commands.PercentageObdCommand;
import com.example.amar.mycar.enums.AvailableCommandNames;

/**
 * Calculated Engine Load value.
 *
 * @author pires
 * @version $Id: $Id
 */
public class LoadCommand extends PercentageObdCommand {

    /**
     * <p>Constructor for LoadCommand.</p>
     */
    public LoadCommand() {
        super("01 04");
    }

    /**
     * <p>Constructor for LoadCommand.</p>
     *
     *
     */
    public LoadCommand(LoadCommand other) {
        super(other);
    }

    /*
     * (non-Javadoc)
     *
     * @see pt.lighthouselabs.obd.commands.ObdCommand#getName()
     */
    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_LOAD.getValue();
    }

}
