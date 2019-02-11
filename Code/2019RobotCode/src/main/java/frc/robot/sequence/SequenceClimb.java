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
        if(Button.JOY_TOPRIGHT.isPressed())
        {
            double motorSpeed = (1.0d / 300.0d);
            double curSpeed = fangMotor.getPosition() + motorSpeed;
            fangMotor.set(Robot.getJoystick().getY());
            if(tiltSensor.getAngleX() > 0.5d && fangMotor.getPosition() 
            {

            }
        }
        else
        {
            double retractSpeed = 1.0d / 180.0d;
            if (fangMotor.getPosition() <= retractSpeed)
            {
                fangMotor.setPosition(0.0d);
                this.length = 0;
            }
            else
            {
                fangMotor.setPosition(fangMotor.getPosition() * retractSpeed);
            }
        }
    }
}