package frc.robot.smartint.childs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.smartint.*;

public class SmartNum extends SmartVal
    {
        public SmartNum(String id, double defval)
        {
            super(id, defval);
            SmartDashboard.setDefaultNumber(id, defval);
        }

        protected Object getVal()
        {
            return SmartDashboard.getNumber(this.valIdentifier, -1.0d);
        }
        public double getNum()
        {
            return (double)getVal();
        }
        protected boolean setVal(Object val)
        {
            return SmartDashboard.putNumber(this.valIdentifier, (double)val);
        }
        public boolean setNum(double num)
        {
            return setVal(num);
        }
    }