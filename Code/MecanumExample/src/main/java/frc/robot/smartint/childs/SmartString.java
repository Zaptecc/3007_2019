package frc.robot.smartint.childs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.smartint.*;

public class SmartString extends SmartVal
{
    public SmartString(String id, String defval)
    {
        super(id, defval);
        SmartDashboard.setDefaultString(id, defval);
    }

    protected Object getVal()
    {
        return SmartDashboard.getString(this.valIdentifier, "");
    }
    public String getString()
    {
        return (String)getVal();
    }
    protected boolean setVal(Object val)
    {
        return SmartDashboard.putString(this.valIdentifier, (String)val);
    }
    public boolean setString(String flag)
    {
        return setVal(flag);
    }
}
