/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.robot.sequence.*;
import frc.robot.sequence.SequenceGrab.GrabStates;
import frc.robot.smartint.*;
import frc.robot.smartint.childs.*;
import com.analog.adis16448.frc.*;
import com.revrobotics.*;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 1;
  private static final int kRearLeftChannel = 0;
  private static final int kFrontRightChannel = 3;
  private static final int kRearRightChannel = 2;

  private static final int kFangMotor = 2;
  private static final int kPulleyMotor = 4;

  private static final int kJoystickChannel = 0;
  public static final ADIS16448_IMU theGyro = new ADIS16448_IMU();

  public enum Button
  {
    BASE_TOPRIGHT(12),
    BASE_BOTRIGHT(11),
    BASE_TOPCENTER(10),
    BASE_BOTCENTER(9),
    BASE_TOPLEFT(8),
    BASE_BOTLEFT(7),
    JOY_TOPRIGHT(6),
    JOY_TOPLEFT(5),
    JOY_BOTRIGHT(4),
    JOY_BOTLEFT(3),
    JOY_SIDEBUTTON(2),
    JOY_TRIGGER(1);

    public final int id;
    private Button(int id)
    {
      this.id = id;
    }

    /**
     *  Checks to see what the current state of the button is.
     * @return Returns true if the button is pressed.
     */
    public boolean isPressed()
    {
      return isPressed(M_Joystick);
    }

    /**
     * Checks the specified joystick to see what the current state of the button is.
     * @param stick The joystick to check.
     * @return Returns true if the button is pressed.
     */
    public boolean isPressed(Joystick stick)
    {
      if(stick == null) {
        System.out.println("Joystick is not found, cannot determine if button is pressed!!");
        return false;
      }
      return stick.getRawButton(this.id);
    }
  }

  private static MecanumDrive m_robotDrive;
  private static Joystick M_Joystick;
  private static Joystick M_Joystick2;
  public CANSparkMax fangMotor = new CANSparkMax(kFangMotor, MotorType.kBrushless);
  public Jaguar pulleyMotor = new Jaguar(kPulleyMotor);
  public Compressor comp = new Compressor(0);
  public Solenoid solArm = new Solenoid(0);
  public Solenoid solHand = new Solenoid(1);
  public SequenceGrab grabSequence;
  public SequenceClimb climbSequence;
  public static MecanumDrive getDrive()
  {
    return m_robotDrive;
  }

  public static Joystick getJoystick()
  {
    return M_Joystick;
  }

  public static Joystick getJoystick2()
  {
    return M_Joystick2;
  }
  
  @Override
  public void robotInit() {
    Jaguar frontLeft = new Jaguar(kFrontLeftChannel);
    Jaguar rearLeft = new Jaguar(kRearLeftChannel);
    Jaguar frontRight = new Jaguar(kFrontRightChannel);
    Jaguar rearRight = new Jaguar(kRearRightChannel);
    
    //frontLeft.setInverted(false);
    //rearLeft.setInverted(false);
    
    SmartIntegration.addSmartItem(new SmartBool("RotationEnabled",true));
    SmartIntegration.addSmartItem(new SmartBool("ClimbModeEnabled",false));
    SmartIntegration.addSmartItem(new SmartBool("Limelight Adjust Enabled",true));
    SmartIntegration.addSmartItem(new SmartNum("Acceleration X", theGyro.getAccelX()));
    SmartIntegration.addSmartItem(new SmartNum("Acceleration Y", theGyro.getAccelY()));
    SmartIntegration.addSmartItem(new SmartNum("Acceleration Z", theGyro.getAccelZ()));
    SmartIntegration.addSmartItem(new SmartNum("LimelightX", 0.0d));
    SmartIntegration.addSmartItem(new SmartNum("LimelightY", 0.0d));
    SmartIntegration.addSmartItem(new SmartNum("LimelightArea", 0.0d));
    SmartIntegration.addSmartItem(new SmartNum("Limelight Adjust Num", 0.0d));
    SmartIntegration.addSmartItem(new SmartBool("Auto Adjust Enabled", false));
    SmartIntegration.addSmartItem(new SmartNum("Fang Motor Speed", 0.0d));
    SmartIntegration.addSmartItem(new SmartNum("Slide System Speed", 0.0d));
    SequenceMaster.addSequence(new SequenceAutoAdjust(-1)); 
    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
    theGyro.reset();
    theGyro.calibrate();
    M_Joystick = new Joystick(kJoystickChannel);
    M_Joystick2 = new Joystick(kJoystickChannel + 1);
    comp.start();
    m_robotDrive.setExpiration(0.1d); 
    grabSequence = new SequenceGrab(solArm, solHand);
    climbSequence = new SequenceClimb(fangMotor, pulleyMotor, theGyro);
    SequenceMaster.addSequence(grabSequence);
    SequenceMaster.addSequence(climbSequence);
  }

  public int ticks = 0;
  public boolean testIng = false;
  public int testNum = 0;

  @Override
  public void teleopPeriodic() {
    robotUpdate();
  }
  @Override
  public void autonomousPeriodic() {
    robotUpdate();
  }


  public static boolean climbModeEnabled = false;
  //The main update function, called from both autonomous and teleop as of right now.
  public void robotUpdate()
  {
    ++ticks;
    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    double damper = 0.3d;
    double xval = -floorClip(M_Joystick.getX() * damper, 0.125);
    double yval = floorClip(M_Joystick.getY() * damper, 0.125);
    double zval = -floorClip(M_Joystick.getZ() * damper, 0.3);
    SmartIntegration.setSmartValue("Fang Motor Speed", fangMotor.get());
    SequenceMaster.updateSequences();
    GrabStates curState = Button.BASE_BOTLEFT.isPressed() ? GrabStates.HOLDING : 
    Button.BASE_TOPLEFT.isPressed() ? GrabStates.ACTIVE : 
    Button.BASE_BOTCENTER.isPressed() ? GrabStates.INACTIVE : 
    grabSequence.handState;
    
    if(curState != grabSequence.handState) {grabSequence.handOffset = 0;}
    grabSequence.handState = curState;

    if(ticks % 8 == 0)
    {
      NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight"); //Accessing the Limelight from the network.
      NetworkTableEntry tx = table.getEntry("tx"); //Target X-offset from Limelight.
      NetworkTableEntry ty = table.getEntry("ty"); //Target Y-offset from Limelight.
      NetworkTableEntry ta = table.getEntry("ta"); //% of screen target fills of Limelight.
  
      //read values periodically
      double x = tx.getDouble(0.0); //Convert the NetworkTableEntry to useable, double format.
      double y = ty.getDouble(0.0);
      double area = ta.getDouble(0.0);
  
      //post to smart dashboard periodically
      SmartIntegration.setSmartValue("LimelightX", x); //Push the Limelight variables to SmartDashboard.
      SmartIntegration.setSmartValue("LimelightY", y);
      SmartIntegration.setSmartValue("LimelightArea", area);
      SmartIntegration.setSmartValue("Acceleration X", theGyro.getAccelX()); //Push the IMU accelerations to SmartDashboard.
      SmartIntegration.setSmartValue("Acceleration Y", theGyro.getAccelY());
      SmartIntegration.setSmartValue("Acceleration Z", theGyro.getAccelZ());  
    }

    SmartIntegration.setSmartValue("Limelight Adjust Enabled", autoAdjustEnabled());
    
    //Yuliana's section





    //Celina's section





    //Samuel's section





    //Varneeka's section





    //Jihaan's section





    //End sections

    if(Button.BASE_BOTRIGHT.isPressed(getJoystick()))
    {
      climbModeEnabled = true;
    }
    if(Button.BASE_TOPRIGHT.isPressed(getJoystick()))
    {
      climbModeEnabled = false;
    }


    //Testing the fang motor, setting speed of Y value of second joystick if button is pressed
    if(Button.JOY_TOPRIGHT.isPressed(getJoystick()) && climbModeEnabled)
    {
      fangMotor.set(-getJoystick().getY() * 0.55);
    }
    else
    {
      fangMotor.set(0.0d);
    }

    SmartIntegration.setSmartValue("ClimbModeEnabled", climbModeEnabled);

    //Testing the pulley motor, setting speed of Y value of joystick if button is pressed

    if(Button.JOY_BOTLEFT.isPressed())
    {
      boolean isClimbing = climbModeEnabled;
      m_robotDrive.driveCartesian(isClimbing ? 0.0d : -xval, isClimbing ? -Math.abs(yval) : -yval, isClimbing ? 0.0d : -zval, 0.0);
    }
  }

  public static boolean autoAdjustEnabled()
  {
    double limeXOff = (double)SmartIntegration.getSmartValue("LimelightX");
    double limeYOff = (double)SmartIntegration.getSmartValue("LimelightY");
    double limeArea = (double)SmartIntegration.getSmartValue("LimelightArea");

    return Math.abs(limeXOff) <= 20.0f && limeArea >= 3.0d && limeArea <= 35.0d;
  }

  public static double floorClip(double in, double floor)
  {
    return Math.abs(in) < floor ? 0.0d : in;
  }
}
