package com.example.amar.mycar.commands.engine;

import com.example.amar.mycar.commands.temperature.TemperatureCommand;
import com.example.amar.mycar.enums.AvailableCommandNames;

/**
 * Displays the current engine Oil temperature.
 *
 * @author pires
 * @version $Id: $Id
 */
public class OilTempCommand extends TemperatureCommand {

    /**
     * Default ctor.
     */
    public OilTempCommand() {
        super("01 5C");
    }

    /**
     * Copy ctor.
     *
     *
     */
    public OilTempCommand(OilTempCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.ENGINE_OIL_TEMP.getValue();
    }

}
