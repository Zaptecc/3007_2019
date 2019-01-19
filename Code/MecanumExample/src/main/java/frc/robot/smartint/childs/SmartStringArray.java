package frc.robot.smartint.childs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.smartint.*;

public class SmartStringArray extends SmartVal
{
    public SmartStringArray(String id, String[] defval)
    {
        super(id, defval);
        SmartDashboard.setDefaultStringArray(id, defval);
    }

    protected Object getVal()
    {
        return SmartDashboard.getStringArray(this.valIdentifier, new String[]{});
    }
    public String[] getArray()
    {
        return (String[])getVal();
    }
    protected boolean setVal(Object val)
    {
        return SmartDashboard.putStringArray(this.valIdentifier, (String[])val);
    }
    public boolean setArray(String[] array)
    {
        return setVal(array);
    }
}
