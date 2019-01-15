/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#include <frc/drive/MecanumDrive.h>
#include <frc/IterativeRobot.h>
#include <frc/Joystick.h>
#include <frc/VictorSP.h>
#include <frc/Jaguar.h>


	int kFrontLeftChannel = 0;
	int kRearLeftChannel = 1;
	int kFrontRightChannel = 2;
	int kRearRightChannel = 3;

	int kJoystickChannel = 0;
/**
 * This is a demo program showing how to use Mecanum control with the
 * MecanumDrive class.
 */
class Robot : public frc::IterativeRobot {
public:
	void RobotInit() {
		// Invert the left side motors
		// You may need to change or remove this to match your robot
		m_frontLeft.SetInverted(false);
		m_rearLeft.SetInverted(false);
	}

	void TeleopPeriodic() override {
		/* Use the joystick X axis for lateral movement, Y axis for
		 * forward
		 * movement, and Z axis for rotation.
		 */
		m_robotDrive.DriveCartesian(
				m_stick.GetX(), m_stick.GetY(), -m_stick.GetZ());
	}

	frc::Jaguar m_frontLeft{kFrontLeftChannel};
	frc::Jaguar m_rearLeft{kRearLeftChannel};
	frc::Jaguar m_frontRight{kFrontRightChannel};
	frc::Jaguar m_rearRight{kRearRightChannel};
	frc::MecanumDrive m_robotDrive{
			m_frontLeft, m_rearLeft, m_frontRight, m_rearRight};

	frc::Joystick m_stick{kJoystickChannel};
};

START_ROBOT_CLASS(Robot)
