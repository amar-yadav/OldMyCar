package com.example.amar.mycar.commands.fuel;

import com.example.amar.mycar.commands.PercentageObdCommand;
import com.example.amar.mycar.enums.AvailableCommandNames;

public class FuelLevelCommand extends PercentageObdCommand {

    public FuelLevelCommand() {
        super("01 2F");
    }

    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        percentage = 100.0f * buffer.get(2) / 255.0f;
    }

    @Override
    public String getName() {
        return AvailableCommandNames.FUEL_LEVEL.getValue();
    }

    public float getFuelLevel() {
        return percentage;
    }

}
