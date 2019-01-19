package frc.robot.smartint;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class SmartVal
{
    protected String valIdentifier;

    public SmartVal(String id, Object defval)
    {
        valIdentifier = id;
        this.setVal(defval);
    }

    /**
     * Get this variable's SmartDashboard identifier.
     * @return the identifier for the SmartDashboard.
     */
    public String getIdentifier()
    {
        return valIdentifier;
    }

    /**
     * Set whether or not a SmartDashboard variable persists between runs.
     * @param persistent whether to make the variable persistent or to clear it's persistency.
     */
    public void setPersistent(boolean persistent)
    {
        if(persistent)
        {
            SmartDashboard.setPersistent(valIdentifier);
        }
        else
        {
            SmartDashboard.clearPersistent(valIdentifier);
        }
    }

    /**
     * Gets the value from the SmartDashboard.
     * @return the SmartDashboard value.
     */
    protected abstract Object getVal();
    /**
     * Sets the value to the SmartDashboard.
     * @param val value to replace the existing one with.
     * @return Returns whether or not the replacement was successful in the SmartDashboard.
     */
    protected abstract boolean setVal(Object val);
}