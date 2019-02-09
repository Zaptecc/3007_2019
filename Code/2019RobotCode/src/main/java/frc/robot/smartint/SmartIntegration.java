package frc.robot.smartint;

import java.util.*;
import java.lang.String;

public abstract class SmartIntegration
{

    protected static final ArrayList<SmartVal> syncedItems = new ArrayList<SmartVal>();

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
        for(int i = 0; i < syncedItems.size(); ++i)
        {
            if(id.equals(syncedItems.get(i).getIdentifier()))
            {
                return syncedItems.get(i).getVal();
            }
        }

        return null;
    }

    public static boolean setSmartValue(String id, Object val)
    {
        for(int i = 0; i < syncedItems.size(); ++i)
        {
            if(id.equals(syncedItems.get(i).getIdentifier()))
            {
                return syncedItems.get(i).setVal(val);
            }
        }

        return false;
    }
}