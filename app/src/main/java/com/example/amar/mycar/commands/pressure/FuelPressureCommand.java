package com.example.amar.mycar.commands.pressure;

import com.example.amar.mycar.enums.AvailableCommandNames;

public class FuelPressureCommand extends PressureCommand {

    /**
     * <p>Constructor for FuelPressureCommand.</p>
     */
    public FuelPressureCommand() {
        super("01 0A");
    }

    public FuelPressureCommand(FuelPressureCommand other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     * <p>
     * TODO describe of why we multiply by 3
     */
    @Override
    protected final int preparePressureValue() {
        return buffer.get(2) * 3;
    }

    public String getName() {
        return AvailableCommandNames.FUEL_PRESSURE.getValue();
    }

}
