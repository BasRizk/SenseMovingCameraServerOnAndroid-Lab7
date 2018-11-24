# Lab_7

**Tasks:**

*Disable any firewall or security software that you might have on your system.

*Find and use a simple java or python implementation of a TCP server to run a listening server on your laptop (Fastest way to run a webserver on Linux: python -m SimpleHTTPServer)

*When the user moves the device to the left, the string "Left" should be sent to the server. When the user moves the device to the right, the string "Right" should be sent to the server. Hint: Could be done from accelerometer sensor on emulator.

*To connect to your laptop server from the emulator, you will need to use a special IP address for your client socket. That IP is: 10.0.2.2. (An HTTP server running on your laptop would be also ok. In this case you could send a GET request to either /right or /left)

*Run a simple Server on the emulator which listens for incoming connections. Whenever a connection is made to it, the application should take a picture using the CameraAPI and save it on the device. You should also send the string "Picture taken" to the server running on your laptop @ 10.0.2.2 (not the android device.)
