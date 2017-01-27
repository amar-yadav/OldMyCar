package com.example.amar.mycar.commands.pressure;

import com.example.amar.mycar.enums.AvailableCommandNames;

public class BarometricPressureCommand extends PressureCommand {

    /**
     * <p>Constructor for BarometricPressureCommand.</p>
     */
    public BarometricPressureCommand() {
        super("01 33");
    }

    public BarometricPressureCommand(PressureCommand other) {
        super(other);
    }

    public String getName() {
        return AvailableCommandNames.BAROMETRIC_PRESSURE.getValue();
    }

}
