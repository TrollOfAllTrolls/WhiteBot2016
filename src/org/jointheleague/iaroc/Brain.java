package org.jointheleague.iaroc;

import android.os.SystemClock;

import org.jointheleague.iaroc.sensors.UltraSonicSensors;
import org.wintrisstech.irobot.ioio.IRobotCreateAdapter;
import org.wintrisstech.irobot.ioio.IRobotCreateInterface;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

public class Brain extends IRobotCreateAdapter {
    private final Dashboard dashboard;
    public UltraSonicSensors sonar;
    int distance = 4000;
    long startTime = System.currentTimeMillis();

    public Brain(IOIO ioio, IRobotCreateInterface create, Dashboard dashboard)
            throws ConnectionLostException {
        super(create);
        sonar = new UltraSonicSensors(ioio);
        this.dashboard = dashboard;
    }

    /* This method is executed when the robot first starts up. */
    public void initialize() throws ConnectionLostException {

    }

    /* This method is called repeatedly. */
    public void loop() throws ConnectionLostException {
        dashboard.log((System.currentTimeMillis() - startTime) / 1000f + " Seconds");
        readSensors(6);
        distance = getWallSignal();
        if (distance < 10) {
            driveDirect(75, 400);
        } else {
            driveDirect(400, 75);
        }
        readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
        if (isBumpLeft()) {
            driveDirect(-500, -500);
            SystemClock.sleep(100);
            driveDirect(400, -400);
            SystemClock.sleep(500);
        }
    }
}