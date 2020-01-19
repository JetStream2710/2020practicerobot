/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;

import com.revrobotics.ColorSensorV3;
import com.kauailabs.navx.frc.AHRS;

public class Robot extends TimedRobot {

  public static AHRS ahrs = new AHRS(SPI.Port.kMXP);

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);

//  private Vision vision;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
//    vision = new Vision();
  }

  @Override
  public void robotPeriodic() {


    // limelight test code
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    double targetOffsetAngle_Horizontal = table.getEntry("tx").getDouble(0);
    double targetOffsetAngle_Vertical = table.getEntry("ty").getDouble(0);
    double targetArea = table.getEntry("ta").getDouble(0);
    double targetSkew = table.getEntry("ts").getDouble(0);

    System.out.println("horizontal: " + targetOffsetAngle_Horizontal);
    System.out.println("vertical: " + targetOffsetAngle_Vertical);
    System.out.println("area: " + targetArea);
    System.out.println("horizontal: " + targetSkew);

//    vision.readCameraData();

//    System.out.println("constant: " + Constants.ADC_TO_RGB_CONSTANT);

//    System.out.println((float) 256/262144);
//    Color detectedColor = m_colorSensor.getColor();
    double IR = colorSensor.getIR();
    double red = colorSensor.getRed();
    double green = colorSensor.getGreen();
    double blue = colorSensor.getBlue();

    SmartDashboard.putNumber("ADC Red", red);
    SmartDashboard.putNumber("ADC Green", green);
    SmartDashboard.putNumber("ADC Blue", blue);

    SmartDashboard.putNumber("IR", IR);

    int proximity = colorSensor.getProximity();
    SmartDashboard.putNumber("Proximity", proximity);

    CommandScheduler.getInstance().run();

    double max = red;
    if (green > max){
      max = green;
    }
    if (blue > max){
      max = blue;
    }

/**
 * blue: blue and green within 10 percent,, red less than 50%
 * green: green :D  w red and blue similar (less than 50&)
 * red: red slightly higher than green
 * yellow: red at 50% value of green,, blue much smaller than red
 */

    boolean hasRed = red == max;
    boolean hasBlue = blue >= max*0.9;
    boolean hasGreen = green == max && red < max*0.5 && blue < max*0.5;
    boolean hasYellow = green == max && red > max*0.5 && red < max*0.8 && blue < max*0.5;

/*
    // Test code for the color sensor
    if (hasYellow){
      System.out.println("yellow");
    }
    else if (hasRed){
      System.out.println("red");
    }
    else if (hasGreen){
      System.out.println("green");
    }
    else if (hasBlue){
      System.out.println("blue");
    }
    else {
      System.out.println("no color detected");
    }
*/
/*
    // Test Code for the NavX
		System.out.println("Angle: " + ahrs.getAngle() + " Yaw: " + ahrs.getYaw() +
				" Pitch: " + ahrs.getPitch() + " Roll: " + ahrs.getRoll());
    System.out.println("x: " + ahrs.getRawGyroX() + " y: " + ahrs.getRawGyroY() + " z: " + ahrs.getRawGyroZ());
*/

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
//    m_robotContainer.drivetrain.frontRightTalon.set(.2);
//    m_robotContainer.drivetrain.rearRightTalon.set(.2);

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
