package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.*;
import java.lang.String;

public abstract class SmartIntegration
{
    protected abstract class SmartVal<T>
    {
        protected T valToSync;
        protected String valIdentifier;

        public SmartVal(String id, T val)
        {
            valToSync = val;
        }

        public T getVal()
        {
            return valToSync;
        }

        public boolean setVal(T val)
        {
            valToSync = val;
            return true;
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

        public abstract void syncToSmartdash();
    }

    public class SmartNum<Double> extends SmartVal
    {
        public SmartNum(String id, Double default_val)
        {
            super(id, default_val);
            SmartDashboard.setDefaultNumber(id, (double)default_val);
        }

        @Override
        public void syncToSmartdash() {
            SmartDashboard.putNumber(this.valIdentifier, (double)this.valToSync);
        }
    }

    public class SmartBool<Boolean> extends SmartVal
    {
        public SmartBool(String id, Boolean default_val)
        {
            super(id, default_val);
            SmartDashboard.setDefaultBoolean(id, (boolean)default_val);
        }

        @Override
        public void syncToSmartdash() {
            SmartDashboard.putBoolean(this.valIdentifier, (boolean)this.valToSync);
        }
    }

    public class SmartString<String> extends SmartVal
    {
        public SmartString(String id, String default_val)
        {
            super((java.lang.String) id, default_val);
            valToSync = default_val;
            SmartDashboard.setDefaultString((java.lang.String)id, (java.lang.String)default_val);
        }

        @Override
        public void syncToSmartdash() {
            SmartDashboard.putString(this.valIdentifier, (java.lang.String)this.valToSync);
        }
    }

    protected static final ArrayList<SmartVal> syncedItems = new ArrayList<SmartVal>();

    /**
     * Syncs all registered SmartVals to the SmartDashboard.
     */
    public static void syncSmartItems()
    {
        for(int i = 0; i < syncedItems.size(); ++i)
        {
            syncedItems.get(i).syncToSmartdash();
        }
    }

    /**
     * Adds a SmartVal to the list of updated SmartVals.
     * @param val the SmartVal to add to the list.
     */
    public static void addSmartItem(SmartVal val)
    {
        syncedItems.add(val);
    }

    /**
     * Gets the value of the 
     * @param id the identifier of the SmartVal.
     * @return Returns the value of the SmartVal as an object, or null if a registered smartval with the given identifier can't be found.
     */
    public static Object getSmartValue(String id)
    {
        syncSmartItems();

        for(int i = 0; i < syncedItems.size(); ++i)
        {
            if(id.equals(syncedItems.get(i).getIdentifier()))
            {
                return syncedItems.get(i).getVal();
            }
        }

        return null;
    }
}