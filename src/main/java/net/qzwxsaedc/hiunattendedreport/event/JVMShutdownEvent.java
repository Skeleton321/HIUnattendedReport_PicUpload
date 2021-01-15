package net.qzwxsaedc.hiunattendedreport.event;

import java.util.ArrayList;
import java.util.List;

public class JVMShutdownEvent extends Thread{
    private List<ShutdownEventAdapter> list;
    private boolean isHooked;
    private static final JVMShutdownEvent instance = new JVMShutdownEvent();

    private JVMShutdownEvent(){
        list = new ArrayList<>();
        isHooked = false;
    }

    private void hook(){
        Runtime.getRuntime().addShutdownHook(instance);
        isHooked = true;
    }

    public static void register(ShutdownEventAdapter func){
        if(!instance.isHooked)
            instance.hook();
        instance.list.add(func);
    }

    @Override
    public void run() {
        for(ShutdownEventAdapter func : list)
            try{
                func.called();
            }catch (Exception e){
                e.printStackTrace();
            }
    }
}
