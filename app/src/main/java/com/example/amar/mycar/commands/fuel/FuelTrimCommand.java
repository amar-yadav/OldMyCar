package com.example.amar.mycar.commands.fuel;
import com.example.amar.mycar.commands.PercentageObdCommand;
import com.example.amar.mycar.enums.FuelTrim;

public class FuelTrimCommand extends PercentageObdCommand {

    private final FuelTrim bank;

    public FuelTrimCommand(final FuelTrim bank) {
        super(bank.buildObdCommand());
        this.bank = bank;
    }

    /**
     * <p>Constructor for FuelTrimCommand.</p>
     */
    public FuelTrimCommand() {
        this(FuelTrim.SHORT_TERM_BANK_1);
    }

    /**
     * @param value
     * @return
     */
    private float prepareTempValue(final int value) {
        return (value - 128) * (100.0F / 128);
    }

    /**
     * <p>performCalculations.</p>
     */
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        percentage = prepareTempValue(buffer.get(2));
    }

    /**
     * <p>getValue.</p>
     *
     * @return the readed Fuel Trim percentage value.
     * @deprecated use #getCalculatedResult()
     */
    public final float getValue() {
        return percentage;
    }

    /**
     * <p>Getter for the field <code>bank</code>.</p>
     *
     * @return the name of the bank in string representation.
     */
    public final String getBank() {
        return bank.getBank();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return bank.getBank();
    }

}
