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

    public SequenceClimb(CANSparkMax fang, Jaguar pulley, ADIS16448_IMU imu)
    {
        super(-1,"ClimbStep");
        fangMotor = fang;
        pulleyMotor = pulley;
        tiltSensor = imu;
    }

    public void sequenceUpdate() 
    {
        if(Robot.climbModeEnabled)
        {
            if(Button.JOY_TOPRIGHT.isPressed())
            {
                double motorSpeed = (1.0d / 20.0d);
                //            fangMotor.set(motorSpeed);
                double xAng = tiltSensor.getAccelX();
                if(xAng > 0.1)
                {
                    pulleyMotor.set(pulleyMotor.get() - motorSpeed);
                }
                else if(Math.abs(xAng) < 0.0125)
                {
                    pulleyMotor.set(0.0d);
                }
                else if(xAng < -0.1)
                {
                    pulleyMotor.set(pulleyMotor.get() + motorSpeed);
                }    
            }
        }
        else
        {
            //negative for the pulley is up,
            //positive for the pulley is down.
            pulleyMotor.set(-0.8d);
        }
    }
}