/* CS/COE 1501
   Primitive chat client. 
   This client connects to a server so that messages can be typed and forwarded
   to all other clients.  Try it out in conjunction with ChatServer.java.
   You will need to modify / update this program to incorporate the secure elements
   as specified in the Assignment sheet.  Note that the PORT used below is not the
   one required in the assignment -- be sure to change that and also be sure to
   check on the location of the server program regularly (it may change).
*/
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.BigInteger;

public class SecureChatClient extends JFrame implements Runnable, ActionListener {

	public static final int PORT = 8765;

	ObjectInputStream in;
	ObjectOutputStream out;
	JTextArea outputArea;
	JLabel prompt;
	JTextField inputField;
	String myName, serverName;
	Socket connection;

	BigInteger pubKey, modVal;
	SymCipher cipher;
	BigInteger key;

	public SecureChatClient () {
		try {

			myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
			serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
			InetAddress addr = InetAddress.getByName(serverName);
			connection = new Socket(addr, PORT);   // Connect to server with new socket

			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();

			in = new ObjectInputStream(connection.getInputStream());

			pubKey = (BigInteger) in.readObject();
			modVal = (BigInteger) in.readObject();
			String ct = (String) in.readObject();

			System.out.printf("Public key: %s\nMod val: %s\nCrypto type: %s\n", 
				pubKey.toString(), modVal.toString(), ct);

			if (ct.equals("Sub")) 
				cipher = new Substitute();
			else if (ct.equals("Add"))
				cipher = new Add128();
			else
				throw new IllegalArgumentException();

			key = new BigInteger(1, cipher.getKey());
			System.out.printf("Symmetric key: %s\n", key);
			out.writeObject(key.modPow(pubKey, modVal));		// sending symmetric key
			out.flush();
			out.writeObject(cipher.encode(myName));   
			out.flush();

			this.setTitle(myName);	  			// Set title to identify chatter
			Box b = Box.createHorizontalBox();  // Set up graphical environment for
			outputArea = new JTextArea(8, 30);  // user
			outputArea.setEditable(false);
			b.add(new JScrollPane(outputArea));

			outputArea.append("Welcome to the Chat Group, " + myName + "\n");

			inputField = new JTextField("");  // This is where user will type input
			inputField.addActionListener(this);

			prompt = new JLabel("Type your messages below:");
			Container c = getContentPane();

			c.add(b, BorderLayout.NORTH);
			c.add(prompt, BorderLayout.CENTER);
			c.add(inputField, BorderLayout.SOUTH);

			Thread outputThread = new Thread(this);  // Thread is to receive strings
			outputThread.start();					// from Server

			addWindowListener(
					new WindowAdapter() {
						public void windowClosing(WindowEvent e) { 
							try {
								out.write(cipher.encode("CLIENT CLOSING"));
								out.flush();
							} catch(IOException ioe) {}
						  	System.exit(0);
						}
					}
				);

			setSize(500, 200);
			setVisible(true);

		}
		catch (Exception e)
		{
			System.out.println("Problem starting client!");
			System.exit(1);
		}
	}

	public void run()
	{
		while (true)
		{
			 try {
			 	byte[] bytesIn = (byte[]) in.readObject();
			 	System.out.println("New message: ");
			 	System.out.println("Ecrypted BigInt: " + new BigInteger(bytesIn).toString());
				String currMsg = cipher.decode(bytesIn);
			 	System.out.println("Decrypted BigInt: " + new BigInteger(currMsg.getBytes()).toString());
			 	System.out.println("Message: " + currMsg);
				outputArea.append(currMsg+"\n");
			 }
			 catch (Exception e)
			 {
			 	e.printStackTrace();
				System.out.println(e +  ", closing client!");
				break;
			 }
		}
		System.exit(0);
	}

	public void actionPerformed(ActionEvent e)
	{
		String currMsg = e.getActionCommand();	  // Get input value
		inputField.setText("");
		try {
			String message = myName + ":" + currMsg;
			System.out.println("Sending new message: ");
			System.out.println("Message: " + message);
			System.out.println("Unencrypted BigInt: " + new BigInteger(currMsg.getBytes()).toString());
			byte[] bytesOut = cipher.encode(message);
			System.out.println("Encrypted BigInt: " + new BigInteger(bytesOut).toString());
			out.writeObject(bytesOut);   
			out.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
	}											 	

	public static void main(String [] args)
	{
		 SecureChatClient JR = new SecureChatClient();
		 JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}


