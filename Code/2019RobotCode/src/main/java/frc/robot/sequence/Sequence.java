package frc.robot.sequence;

/**
 * A class to help segment code and organize. Can be limited to a certain time limit.
 */
public abstract class Sequence
{
    /**
     * The length of this sequence left in 60th of seconds.
     * Set to -1 if this sequence is neverending.
     */
    protected int length;
    public final String id;

    /**
     * @param length length in seconds. If the sequence is infinite, set length to -1.
     * @param id the string identifier for this sequence. You can check if this sequence is running already in SequenceMaster.isSequenceRunning(id).
     */
    public Sequence(int length, String id)
    {
        this.length = length * 60;
        this.id = id;
    }

    public boolean baseSequenceUpdate()
    {
        --length;
        sequenceUpdate();
        return length != 0;
    }

    public abstract void sequenceUpdate();
}