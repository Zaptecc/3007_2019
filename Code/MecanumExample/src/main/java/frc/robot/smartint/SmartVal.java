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

        public String getIdentifier()
        {
            return valIdentifier;
        }

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

        protected abstract Object getVal();
        protected abstract boolean setVal(Object val);
    }