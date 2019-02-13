package frc.robot.sequence;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.*;
import frc.robot.Robot;
import frc.robot.Robot.Button;

public class SequenceClimb extends Sequence
{
    private CANSparkMax fangMotor;
    private Jaguar pulleyMotor;
    private ADIS16448_IMU tiltSensor;
    private boolean moveUp;

    public SequenceClimb(CANSparkMax fang, Jaguar pulley, ADIS16448_IMU imu, boolean up)
    {
        super(-1,"ClimbStep");
        fangMotor = fang;
        pulleyMotor = pulley;
        tiltSensor = imu;
        moveUp = up;
    }

    public void sequenceUpdate() 
    {
        if(Button.JOY_TOPRIGHT.isPressed())
        {
            double motorSpeed = (1.0d / 300.0d);
            fangMotor.set(fangMotor.get()+motorSpeed);
            double yAng = tiltSensor.getAngleY();
            if(yAng > 0.1)
            {
                pulleyMotor.set(pulleyMotor.get() + motorSpeed);
            }
        }
        else
        {
            double retractSpeed = 1.0d / 180.0d;
        }
    }
}