/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import frc.robot.subsystems.Drivetrain;

import com.revrobotics.ColorSensorV3;
import com.kauailabs.navx.frc.AHRS;

public class Robot extends TimedRobot {

  public static AHRS ahrs = new AHRS(SPI.Port.kMXP);

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {

//    System.out.println("constant: " + Constants.ADC_TO_RGB_CONSTANT);

//    System.out.println((float) 256/262144);
//    Color detectedColor = m_colorSensor.getColor();
    double IR = m_colorSensor.getIR();
    double red = m_colorSensor.getRed();
    double green = m_colorSensor.getGreen();
    double blue = m_colorSensor.getBlue();

    SmartDashboard.putNumber("ADC Red", red);
    SmartDashboard.putNumber("ADC Green", green);
    SmartDashboard.putNumber("ADC Blue", blue);
    SmartDashboard.putNumber("IR", IR);

    SmartDashboard.putNumber("Red", red*Constants.ADC_TO_RGB_CONSTANT);
    SmartDashboard.putNumber("Green", green*Constants.ADC_TO_RGB_CONSTANT);
    SmartDashboard.putNumber("Blue", blue*Constants.ADC_TO_RGB_CONSTANT);

    int proximity = m_colorSensor.getProximity();
    SmartDashboard.putNumber("Proximity", proximity);

    CommandScheduler.getInstance().run();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    m_robotContainer.drivetrain.frontRightTalon.set(.2);
    m_robotContainer.drivetrain.rearRightTalon.set(.2);

  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
