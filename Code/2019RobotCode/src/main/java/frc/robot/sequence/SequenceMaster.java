package frc.robot.sequence;

import java.util.ArrayList;

/**
 * A class to handle all things sequences. Adding, removing, updating and checking if they are running.
 */
public final class SequenceMaster
{
    public static final ArrayList<Sequence> activeSequences = new ArrayList<Sequence>();

    public static void updateSequences()
    {
        for(int i = 0; i < activeSequences.size(); ++i)
        {
            Sequence s = activeSequences.get(i);

            if(!s.baseSequenceUpdate())
            {
                activeSequences.remove(i);
                break;
            }
        }
    }

    public static void addSequence(Sequence sequence)
    {
        if(sequence != null)
        {
            activeSequences.add(sequence);
        }
    }

    public static boolean isSequenceRunning(String id)
    {
        for(int i = 0; i < activeSequences.size(); ++i)
        {
            Sequence s = activeSequences.get(i);

            if(s.id.equals(id))
            {
                return true;
            }
        }

        return false;
    }
}