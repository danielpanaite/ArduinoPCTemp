import java.util.List;
import java.util.Scanner;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.*;
import com.profesorfalken.jsensors.model.sensors.Temperature;

import jssc.*;

public class getTemps {
    
    boolean CPU, GPU;
    int i;
    private static SerialPort serialPort;
    private static double CPUTemp, GPUTemp;

    public getTemps(boolean CPU, boolean GPU){
        this.CPU = CPU;
        this.GPU = GPU;

        String[] portNames = SerialPortList.getPortNames();
        System.out.println("Available com-ports:");
        for (int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
        }

        System.out.println("Type port name, which you want to use, and press Enter...");
        Scanner in = new Scanner(System.in);
        String portName = in.next();
        serialPort = new SerialPort(portName);
        in.close();

        try{
            serialPort.openPort();
            
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                 SerialPort.DATABITS_8,
                 SerialPort.STOPBITS_1,
                 SerialPort.PARITY_NONE,false,true);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        }catch (SerialPortException ex) {
            System.out.println("Error opening port:" + ex);
        }
        

        for(i=0; i<10000; i++){
            Components components = JSensors.get.components();

            if(CPU == true){
                List<Cpu> cpus = components.cpus;
    
                if (cpus != null) {
                    for (final Cpu cpu : cpus) {
                        System.out.println("Found CPU component: " + cpu.name);
                        if (cpu.sensors != null) {
                        System.out.println("Sensors: ");
            
                        //Print temperatures
                        List<Temperature> temps = cpu.sensors.temperatures;
                        for (final Temperature temp : temps) {
                            if(temp.name.equals("Temp CPU Package")){
                                CPUTemp = temp.value;
                            }
                            System.out.println(temp.name + ": " + temp.value + " C");
                        }
            
                        }
                    }
                }
            }
            
            if(GPU == true){
                List<Gpu> gpus = components.gpus;
    
                if (gpus != null) {
                    for (final Gpu gpu : gpus) {
                        System.out.println("Found GPU component: " + gpu.name);
                        if (gpu.sensors != null) {
                          System.out.println("Sensors: ");
              
                          //Print temperatures
                          List<Temperature> temps = gpu.sensors.temperatures;
                          for (final Temperature temp : temps) {
                            if(temp.name.equals("Temp GPU Core")){
                                GPUTemp = temp.value;
                              }
                              System.out.println(temp.name + ": " + temp.value + " C");
                          }
        
                        }
                    }
                }
            }

            try {
            
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                //serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
                // writing string to port
                serialPort.writeString("*" + (int)CPUTemp + "#" + (int)GPUTemp + "%");
                System.out.println("*" + CPUTemp + "#" + GPUTemp + "%");
    
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("String wrote to port");
                
            }
            catch (SerialPortException ex) {
                System.out.println("Error in writing data to port: " + ex);
            }
            

        }

        try {
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println("Error in writing data to port: " + ex);
        }

    }

    private static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    // получение ответа от порта
                    String receivedData = serialPort.readString(event.getEventValue());
                    System.out.println("Received response from port: " + receivedData);
                }
                catch (SerialPortException ex) {
                    System.out.println("Error in receiving response from port: " + ex);
                }
            }
        }
    }

}
