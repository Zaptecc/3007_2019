package frc.robot.smartint.childs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.smartint.*;

public class SmartNumArray extends SmartVal
{
    public SmartNumArray(String id, double[] defval)
    {
        super(id, defval);
        SmartDashboard.setDefaultNumberArray(id, defval);
    }

    protected Object getVal()
    {
        return SmartDashboard.getNumberArray(this.valIdentifier, new double[]{});
    }
    public double[] getArray()
    {
        return (double[])getVal();
    }
    protected boolean setVal(Object val)
    {
        return SmartDashboard.putNumberArray(this.valIdentifier, (double[])val);
    }
    public boolean setArray(double[] array)
    {
        return setVal(array);
    }
}
