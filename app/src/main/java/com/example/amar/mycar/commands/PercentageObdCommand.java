package com.example.amar.mycar.commands;


public abstract class PercentageObdCommand extends ObdCommand {

    protected float percentage = 0f;

    /**
     * <p>Constructor for PercentageObdCommand.</p>
     *
     * @param command a {@link String} object.
     */
    public PercentageObdCommand(String command) {
        super(command);
    }


    public PercentageObdCommand(PercentageObdCommand other) {
        super(other);
    }

    /** {@inheritDoc} */
    @Override
    protected void performCalculations() {
        // ignore first two bytes [hh hh] of the response
        percentage = (buffer.get(2) * 100.0f) / 255.0f;
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return String.format("%.1f%s", percentage, getResultUnit());
    }

    /**
     * <p>Getter for the field <code>percentage</code>.</p>
     *
     * @return a float.
     */
    public float getPercentage() {
        return percentage;
    }

    /** {@inheritDoc} */
    @Override
    public String getResultUnit() {
        return "%";
    }

    /** {@inheritDoc} */
    @Override
    public String getCalculatedResult() {
        return String.valueOf(percentage);
    }

}
