/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.robot.sequence.*;
import frc.robot.sequence.SequenceGrab.GrabStates;
import frc.robot.smartint.*;
import frc.robot.smartint.childs.*;
import com.analog.adis16448.frc.*;
import com.revrobotics.*;
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

  //IMU
  public static final ADIS16448_IMU theGyro = new ADIS16448_IMU();

  //DRIVE SYSTEM
  private static MecanumDrive m_robotDrive;

  //INPUT DEVICES
  private static Joystick M_Joystick;
  private static Joystick M_Joystick2;

  //CLIMBING MOTORS
  public CANSparkMax fangMotor = new CANSparkMax(kFangMotor, MotorType.kBrushless);
  public Jaguar pulleyMotor = new Jaguar(kPulleyMotor);
  
  //PNEUMATICS
  public Compressor comp = new Compressor(0);
  public Solenoid solArm = new Solenoid(0);
  public Solenoid solHand = new Solenoid(1);

  //SEQUENCES (SEGMENTS OF CODE)
  public SequenceGrab grabSequence;
  public SequenceClimb climbSequence;
  public SequenceAutoAdjust adjustSequence;

  public int movementFromMotorDamper;
  public static final int MOVE_MOTOR_MAX = 100;



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
  public void autonomousInit() {
    enableInit();
  }
  @Override
  public void teleopInit() {
    enableInit();
  }  

  public void enableInit()
  {
    theGyro.reset();
    theGyro.calibrate();
    m_robotDrive.setExpiration(1.0d); 
    SequenceMaster.activeSequences.clear();
    grabSequence = new SequenceGrab(solArm, solHand);
    climbSequence = new SequenceClimb(fangMotor, pulleyMotor, theGyro);
    adjustSequence = new SequenceAutoAdjust();
    SequenceMaster.addSequence(grabSequence);
    SequenceMaster.addSequence(climbSequence);
    SequenceMaster.addSequence(adjustSequence);
    comp.start();
    grabSequence.handState = GrabStates.INITIALIZE;
    //CameraServer.getInstance().getServer().setSource(CameraServer.getInstance().startAutomaticCapture(1));
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
    SmartIntegration.addSmartItem(new SmartNum("LimelightStrafe", 0.0d));
    SmartIntegration.addSmartItem(new SmartNum("LimelightArea", 0.0d));
    SmartIntegration.addSmartItem(new SmartNum("Limelight Adjust Num", 0.0d));
    SmartIntegration.addSmartItem(new SmartNum("Sequences Running", 0.0d));
    SmartIntegration.addSmartItem(new SmartNum("Fang Motor Speed", 0.0d));
    SmartIntegration.addSmartItem(new SmartNum("Slide System Speed", 0.0d));
    SmartIntegration.addSmartItem(new SmartNum("Motor to Movement Damper", 0.0d));

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    M_Joystick = new Joystick(kJoystickChannel);
    M_Joystick2 = new Joystick(kJoystickChannel + 1);

    enableInit();
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
    double damper = 0.45d;
    double xval = -floorClip(M_Joystick.getX() * damper, 0.05);
    double yval = floorClip(M_Joystick.getY() * damper, 0.05);
    double zval = -floorClip(M_Joystick.getZ() * damper, 0.05);
    SmartIntegration.setSmartValue("Fang Motor Speed", fangMotor.get());
    SequenceMaster.updateSequences();
    GrabStates curState = Controls.getPneumaticHOLDING() ? GrabStates.HOLDING : 
    Controls.getPneumaticACTIVE() ? GrabStates.ACTIVE : 
    Controls.getPneumaticINACTIVE() ? GrabStates.INACTIVE : 
    grabSequence.handState;
    
    if(curState != grabSequence.handState) {grabSequence.handOffset = 0;}
    grabSequence.prevState = grabSequence.handState;
    grabSequence.handState = curState;

    if(ticks % 8 == 0)
    {
      NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight"); //Accessing the Limelight from the network.
      NetworkTableEntry tx = table.getEntry("tx"); //Target X-offset from Limelight.
      NetworkTableEntry ty = table.getEntry("ty"); //Target Y-offset from Limelight.
      NetworkTableEntry ta = table.getEntry("ta"); //% of screen target fills of Limelight.
      NetworkTableEntry ts = table.getEntry("ts"); //% of screen target fills of Limelight.
  
      //read values periodically
      double x = tx.getDouble(0.0); //Convert the NetworkTableEntry to useable, double format.
      double y = ty.getDouble(0.0);
      double area = ta.getDouble(0.0);
  
      //post to smart dashboard periodically
      SmartIntegration.setSmartValue("LimelightX", x); //Push the Limelight variables to SmartDashboard.
      SmartIntegration.setSmartValue("LimelightY", y);
      SmartIntegration.setSmartValue("LimelightStrafe", ts.getDouble(0.0));
      SmartIntegration.setSmartValue("LimelightArea", area);
      SmartIntegration.setSmartValue("Acceleration X", theGyro.getAccelX()); //Push the IMU accelerations to SmartDashboard.
      SmartIntegration.setSmartValue("Acceleration Y", theGyro.getAccelY());
      SmartIntegration.setSmartValue("Acceleration Z", theGyro.getAccelZ());  
    }

    SmartIntegration.setSmartValue("Slide System Speed", pulleyMotor.get());
    SmartIntegration.setSmartValue("Fang Motor Speed", fangMotor.get());
    SmartIntegration.setSmartValue("Motor to Movement Damper", (double)movementFromMotorDamper);
    SmartIntegration.setSmartValue("Limelight Adjust Enabled", autoAdjustEnabled());
    SmartIntegration.setSmartValue("Sequences Running", (double)SequenceMaster.activeSequences.size());

    if(Controls.getAutoAdjustMode() && autoAdjustEnabled())
    {
      zval = -adjustSequence.rotationOffset; //negative because it gets flipped in driveCartesian.
    }

    if(Controls.getClimbModeEnabled())
    {
      climbModeEnabled = true;
    }
    if(Controls.getClimbModeDisabled())
    {
      climbModeEnabled = false;
    }

    //If in climb mode and the top right button on top of the joystick is pressed, operate the fang motor.
    if(Controls.getFangControl() && !Controls.getAutoFang())
    {
      fangMotor.set(Controls.isSecondJoystickControllingFangMotor() ? -getJoystick2().getY() * 0.55 : -getJoystick().getY() * 0.55d);
    }
    else
    {
      if(Controls.getAutoFang() && climbModeEnabled)
      {
        fangMotor.set(0.25d);
      }
      else
      {
        fangMotor.set(0.0d);
      }
    }

    if(Controls.getSlideControl() && !Controls.getAutoFang() && !Controls.getFangControl())
    {
        pulleyMotor.set(Controls.isSecondJoystickControllingSlideMotor() ? -Robot.getJoystick2().getY() * 0.85 : -Robot.getJoystick().getY() * 0.85d);
    }
    else if(!climbSequence.changingSlide)
    {
        pulleyMotor.set(0.0d);
    }

    SmartIntegration.setSmartValue("ClimbModeEnabled", climbModeEnabled);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(Controls.getAutoAdjustMode() ? 0 : 1);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(Controls.getAutoAdjustMode() ? 3 : 1);

    if(Controls.getDriveMode())
    {
      xval *= 0.8d;
      yval *= 0.5d;
      zval *= 0.5d;
    }

    boolean isOnlyAutoAdjust = Controls.getAutoAdjustMode() && !Controls.getDriveMode();
    boolean isClimbing = climbModeEnabled;
    boolean disableAllMovement = Controls.getSlideControl() || Controls.getFangControl();

    if (disableAllMovement)
    {
      xval = yval = zval = 0.0d;
      movementFromMotorDamper = MOVE_MOTOR_MAX;
    }
    else
    {
      if(movementFromMotorDamper > 0)
      {
        --movementFromMotorDamper;
      }
      else
      {
        movementFromMotorDamper = 0;
      }
    }

    double moveDamper = (1.0d - 0.95d * (double)movementFromMotorDamper / (double)MOVE_MOTOR_MAX);
    xval *= moveDamper;
    yval *= moveDamper;
    zval *= moveDamper;
    System.out.println("" + moveDamper);

    if(Controls.getAutoAdjustMode())
    {
      double limeArea = (double)SmartIntegration.getSmartValue("LimelightArea");
      //double limeSkew = (double)SmartIntegration.getSmartValue("LimelightStrafe");
      double scale = 0.15d + 0.85d * (((35.0d - limeArea) / 35.0d));
      //xval = xval * scale + (limeSkew + 45);
      xval = xval / 0.3d;
      yval = yval * scale;
    }

    if(!(boolean)SmartIntegration.getSmartValue("RotationEnabled") == true)
    {
      zval = 0.0d;
    }
    m_robotDrive.driveCartesian(isClimbing ? 0.0d : -xval, isClimbing ? -Math.abs(yval) : -yval, isClimbing ? 0.0d : -zval, 0.0);
  }

  public static boolean autoAdjustEnabled()
  {
    double limeXOff = (double)SmartIntegration.getSmartValue("LimelightX");
    double limeArea = (double)SmartIntegration.getSmartValue("LimelightArea");

    return Math.abs(limeXOff) <= 28.0f && limeArea >= 1.5d && limeArea <= 35.0d;
  }

  public static double floorClip(double in, double floor)
  {
    return Math.abs(in) < floor ? 0.0d : in;
  }
}
