# ArduinoPCTemp
Simple Java console program for monitoring CPU and GPU temperatures on your Arduino board

### **Functionality**
The program gathers data from the [jSensor](https://github.com/profesorfalken/jSensors) library and send it to a COM port for use on Arduino boards. [JSSC](https://github.com/java-native/jssc) library used for data transfer.

### **Usage**
In order to send data over the COM port to your Arduino device it is necessary to connect it before starting the program.

Once the program starts a prompt will ask which COM port to send data to:

```
Available com-ports:
COM1
COM3
COM6
COM7
Type port name of connected device:
```

### **Output**
The program outputs data to the console in the following format:

```
Found CPU component: Intel Core i7-6700K
Sensors:
Temp CPU Core #1: 62.0 C
Temp CPU Core #2: 64.0 C
Temp CPU Core #3: 62.0 C
Temp CPU Core #4: 62.0 C
Temp CPU Package: 64.0 C
Found GPU component: NVIDIA NVIDIA GeForce GTX 1060 6GB
Sensors:
Temp GPU Core: 73.0 C
String wrote to port
```

The format of the data sent via COM port is:

```
eg: *59.0#78.0%
```
The symbol * marks the start of the CPU temperature data, # marks the start of the GPU temperature data and % marks the end of the data stream.