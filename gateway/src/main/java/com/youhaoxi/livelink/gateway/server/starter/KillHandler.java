package com.youhaoxi.livelink.gateway.server.starter;

import sun.misc.Signal;
import sun.misc.SignalHandler;

public class KillHandler implements SignalHandler {
    public void registerSignal(String signalName) {
        Signal signal = new Signal(signalName);
        Signal.handle(signal, this);
    }

    @Override
    public void handle(Signal signal) {

        if (signal.getName().equals("TERM")) {
            //
            System.out.println("收到关闭信号 signal = " + signal);
            System.exit(0);
        } else if (signal.getName().equals("INT") || signal.getName().equals("HUP")) {
            //
        } else {
            //
        }
    }
}
