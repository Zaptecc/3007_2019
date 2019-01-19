package frc.robot.smartint.childs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.smartint.*;

public class SmartBool extends SmartVal
{
    public SmartBool(String id, boolean defval)
    {
        super(id, defval);
        SmartDashboard.setDefaultBoolean(id, defval);
    }

    protected Object getVal()
    {
        return SmartDashboard.getBoolean(this.valIdentifier, false);
    }
    public boolean getBoolean()
    {
        return (boolean)getVal();
    }
    protected boolean setVal(Object val)
    {
        return SmartDashboard.putBoolean(this.valIdentifier, (boolean)val);
    }
    public boolean setBoolean(boolean flag)
    {
        return setVal(flag);
    }
}
