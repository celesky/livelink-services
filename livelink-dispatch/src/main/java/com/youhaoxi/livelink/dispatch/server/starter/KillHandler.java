package com.youhaoxi.livelink.dispatch.server.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class KillHandler implements SignalHandler {
    private static final Logger logger = LoggerFactory.getLogger(KillHandler.class);

    public void registerSignal(String signalName) {
        Signal signal = new Signal(signalName);
        Signal.handle(signal, this);
    }

    @Override
    public void handle(Signal signal) {

        if (signal.getName().equals("TERM")) {
            //
            logger.info(">>>收到JVM关闭信号 signal = " + signal);
            System.exit(0);
        } else if (signal.getName().equals("INT") || signal.getName().equals("HUP")) {
            //
        } else {
            //
        }
    }
}
